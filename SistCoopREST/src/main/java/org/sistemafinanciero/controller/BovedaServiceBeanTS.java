package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.DetalleHistorialBoveda;
import org.sistemafinanciero.entity.Entidad;
import org.sistemafinanciero.entity.HistorialBoveda;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.entity.TransaccionBovedaBoveda;
import org.sistemafinanciero.entity.TransaccionBovedaBovedaDetalle;
import org.sistemafinanciero.entity.TransaccionBovedaOtro;
import org.sistemafinanciero.entity.TransaccionBovedaOtroDetall;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.type.TransaccionEntidadBovedaOrigen;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.nt.MonedaServiceNT;
import org.sistemafinanciero.service.ts.BovedaServiceTS;
import org.sistemafinanciero.util.EntityManagerProducer;

@Named
@Stateless
@Remote(BovedaServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BovedaServiceBeanTS implements BovedaServiceTS {

	@Inject
	private DAO<Object, Boveda> bovedaDAO;

	@Inject
	private DAO<Object, Entidad> entidadDAO;

	@Inject
	private DAO<Object, HistorialBoveda> historialBovedaDAO;

	@Inject
	private DAO<Object, DetalleHistorialBoveda> detalleHistorialBovedaDAO;

	@Inject
	private DAO<Object, TransaccionBovedaOtro> transaccionBovedaOtroDAO;

	@Inject
	private DAO<Object, TransaccionBovedaOtroDetall> detalleTransaccionBovedaOtroDAO;

	@Inject
	private DAO<Object, TransaccionBovedaBoveda> transaccionBovedaBovedaDAO;

	@Inject
	private DAO<Object, TransaccionBovedaBovedaDetalle> detalleTransaccionBovedaBovedaDAO;

	@Inject
	private Validator validator;

	@Inject
	private EntityManagerProducer em;

	@EJB
	private MonedaServiceNT monedaServiceNT;

	public HistorialBoveda getHistorialActivo(BigInteger idBoveda) {
		Boveda boveda = bovedaDAO.find(idBoveda);
		if (boveda == null)
			return null;
		HistorialBoveda bovedaHistorial = null;
		QueryParameter queryParameter = QueryParameter.with("idboveda", idBoveda);
		List<HistorialBoveda> list = historialBovedaDAO.findByNamedQuery(HistorialBoveda.findByHistorialActivo, queryParameter.parameters());
		for (HistorialBoveda c : list) {
			bovedaHistorial = c;
			break;
		}
		return bovedaHistorial;
	}

	@Override
	public BigInteger create(Boveda t) throws PreexistingEntityException, RollbackFailureException {
		Set<ConstraintViolation<Boveda>> violations = validator.validate(t);
		if (violations.isEmpty()) {
			bovedaDAO.create(t);
			return t.getIdBoveda();
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@Override
	public void update(BigInteger id, Boveda t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		Boveda boveda = bovedaDAO.find(id);
		if (boveda != null) {
			Set<ConstraintViolation<Boveda>> violations = validator.validate(boveda);
			if (violations.isEmpty()) {
				String denominacioon = t.getDenominacion();
				boveda.setDenominacion(denominacioon);
				bovedaDAO.update(boveda);
			} else {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
		} else {
			throw new NonexistentEntityException("Boveda no existente, UPDATE no ejecutado");
		}
	}

	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Boveda boveda = bovedaDAO.find(id);
		if (boveda != null) {
			bovedaDAO.delete(boveda);
		} else {
			throw new NonexistentEntityException("Boveda no existente, DELETE no ejecutado");
		}
	}

	@Override
	public BigInteger abrir(BigInteger id) throws RollbackFailureException {
		Boveda boveda = bovedaDAO.find(id);
		// Trabajador trabajador = getTrabajador();
		/*
		 * if (trabajador == null) throw new
		 * RollbackFailureException("No se encontró un trabajador para la caja"
		 * );
		 */

		if (boveda != null) {
			if (!boveda.getEstado())
				throw new RollbackFailureException("Boveda inactiva, no se puede abrir");
			if (boveda.getAbierto())
				throw new RollbackFailureException("Boveda abierta, no se puede abrir nuevamente");

		} else {
			throw new RollbackFailureException("Boveda no encontrada");
		}
		try {
			HistorialBoveda historialBovedaOld = this.getHistorialActivo(id);

			// abriendo caja
			boveda.setAbierto(true);
			boveda.setEstadoMovimiento(true);
			Set<ConstraintViolation<Boveda>> violationsCaja = validator.validate(boveda);
			if (!violationsCaja.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsCaja));
			} else {
				bovedaDAO.update(boveda);
			}

			if (historialBovedaOld != null) {
				historialBovedaOld.setEstado(false);
				Set<ConstraintViolation<HistorialBoveda>> violationsHistorialOld = validator.validate(historialBovedaOld);
				if (!violationsHistorialOld.isEmpty()) {
					throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialOld));
				} else {
					historialBovedaDAO.update(historialBovedaOld);
				}
			}

			Calendar calendar = Calendar.getInstance();
			HistorialBoveda historialBovedaNew = new HistorialBoveda();
			historialBovedaNew.setBoveda(boveda);
			historialBovedaNew.setFechaApertura(calendar.getTime());
			historialBovedaNew.setHoraApertura(calendar.getTime());
			historialBovedaNew.setEstado(true);
			// historialBovedaNew.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno()
			// + " " + trabajador.getPersonaNatural().getApellidoMaterno() +
			// ", " + trabajador.getPersonaNatural().getNombres());
			Set<ConstraintViolation<HistorialBoveda>> violationsHistorialNew = validator.validate(historialBovedaNew);
			if (!violationsHistorialNew.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialNew));
			} else {
				historialBovedaDAO.create(historialBovedaNew);
			}

			if (historialBovedaOld != null) {
				Set<DetalleHistorialBoveda> detalleHistorialBovedas = historialBovedaOld.getDetalleHistorialBovedas();
				for (DetalleHistorialBoveda detalleHistorialBoveda : detalleHistorialBovedas) {
					this.em.getEm().detach(detalleHistorialBoveda);
					detalleHistorialBoveda.setIdDetalleHistorialBoveda(null);
					detalleHistorialBoveda.setHistorialBoveda(historialBovedaNew);

					Set<ConstraintViolation<DetalleHistorialBoveda>> violationsHistorialDetalle = validator.validate(detalleHistorialBoveda);
					if (!violationsHistorialDetalle.isEmpty()) {
						throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialDetalle));
					} else {
						detalleHistorialBovedaDAO.create(detalleHistorialBoveda);
					}
				}
			} else {
				Moneda moneda = boveda.getMoneda();
				List<MonedaDenominacion> denominaciones = monedaServiceNT.getDenominaciones(moneda.getIdMoneda());
				for (MonedaDenominacion monedaDenominacion : denominaciones) {
					DetalleHistorialBoveda detalleHistorialBoveda = new DetalleHistorialBoveda();
					detalleHistorialBoveda.setCantidad(BigInteger.ZERO);
					detalleHistorialBoveda.setHistorialBoveda(historialBovedaNew);
					detalleHistorialBoveda.setMonedaDenominacion(monedaDenominacion);

					Set<ConstraintViolation<DetalleHistorialBoveda>> violationsHistorialDetalle = validator.validate(detalleHistorialBoveda);
					if (!violationsHistorialDetalle.isEmpty()) {
						throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialDetalle));
					} else {
						detalleHistorialBovedaDAO.create(detalleHistorialBoveda);
					}
				}
			}

			return historialBovedaNew.getIdHistorialBoveda();
		} catch (ConstraintViolationException e) {
			throw new EJBException(e);
		}
	}

	@Override
	public BigInteger cerrar(BigInteger id) throws RollbackFailureException {
		Boveda boveda = bovedaDAO.find(id);
		if (boveda == null)
			throw new RollbackFailureException("Boveda no encontrada");

		try {
			Calendar calendar = Calendar.getInstance();
			HistorialBoveda historialBoveda = this.getHistorialActivo(id);
			historialBoveda.setEstado(true);
			historialBoveda.setFechaCierre(calendar.getTime());
			historialBoveda.setHoraCierre(calendar.getTime());

			Set<ConstraintViolation<HistorialBoveda>> violationsHistorial = validator.validate(historialBoveda);
			if (!violationsHistorial.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorial));
			} else {
				historialBovedaDAO.update(historialBoveda);
			}

			// cerrando caja
			boveda.setAbierto(false);
			boveda.setEstadoMovimiento(false);
			Set<ConstraintViolation<Boveda>> violationsCaja = validator.validate(boveda);
			if (!violationsHistorial.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsCaja));
			} else {
				bovedaDAO.update(boveda);
			}
			return historialBoveda.getIdHistorialBoveda();
		} catch (ConstraintViolationException e) {
			throw new EJBException(e);
		}
	}

	@Override
	public void congelar(BigInteger id) throws RollbackFailureException {
		Boveda boveda = bovedaDAO.find(id);
		if (boveda == null)
			throw new RollbackFailureException("Boveda no encontrada");
		if (!boveda.getEstado())
			throw new RollbackFailureException("Boveda inactiva, no se puede congelar");
		if (!boveda.getAbierto())
			throw new RollbackFailureException("Boveda cerrada, no se puede congelar");
		if (boveda.getEstadoMovimiento())
			throw new RollbackFailureException("Boveda congelada, no se puede congelar nuevamente");
		boveda.setEstadoMovimiento(false);
		bovedaDAO.update(boveda);
	}

	@Override
	public void descongelar(BigInteger id) throws RollbackFailureException {
		Boveda boveda = bovedaDAO.find(id);
		if (boveda == null)
			throw new RollbackFailureException("Boveda no encontrada");
		if (!boveda.getEstado())
			throw new RollbackFailureException("Boveda inactiva, no se puede congelar");
		if (!boveda.getAbierto())
			throw new RollbackFailureException("Boveda cerrada, no se puede congelar");
		if (!boveda.getEstadoMovimiento())
			throw new RollbackFailureException("Boveda descongelada, no se puede descongelar nuevamente");
		boveda.setEstadoMovimiento(true);
		bovedaDAO.update(boveda);
	}

	@Override
	public BigInteger crearTransaccionEntidadBoveda(TransaccionEntidadBovedaOrigen origen, Set<GenericDetalle> detalleTransaccion, BigInteger idEntidad, BigInteger idBoveda) throws NonexistentEntityException, RollbackFailureException {
		Entidad entidad = entidadDAO.find(idEntidad);
		Boveda boveda = bovedaDAO.find(idBoveda);
		if (entidad == null)
			throw new NonexistentEntityException("Entidad no encontrada");
		if (boveda == null)
			throw new NonexistentEntityException("Boveda no encontrada");

		BigInteger factor = null;
		switch (origen) {
		case ENTIDAD:
			factor = BigInteger.ONE;
			break;
		case BOVEDA:
			factor = BigInteger.ONE.negate();
			break;
		default:
			throw new RollbackFailureException("Origen de transaccion no valido");
		}

		// sacando historial
		HistorialBoveda historialBoveda = getHistorialActivo(boveda.getIdBoveda());
		if (historialBoveda == null)
			throw new RollbackFailureException("No se encontro un historial activo de boveda");
		Set<DetalleHistorialBoveda> detalleHistorialBoveda = historialBoveda.getDetalleHistorialBovedas();

		// sacando saldos totales
		BigDecimal saldoActual = BigDecimal.ZERO;
		BigDecimal montoTransaccion = BigDecimal.ZERO;

		for (DetalleHistorialBoveda det : detalleHistorialBoveda) {
			BigInteger cantidad = det.getCantidad();
			BigDecimal valor = det.getMonedaDenominacion().getValor();
			saldoActual.add(valor.multiply(new BigDecimal(cantidad)));
		}
		for (GenericDetalle det : detalleTransaccion) {
			BigInteger cantidad = det.getCantidad();
			BigDecimal valor = det.getValor();
			montoTransaccion.add(valor.multiply(new BigDecimal(cantidad)));
		}

		Set<TransaccionBovedaOtroDetall> detalleTransaccionBovOtro = new HashSet<TransaccionBovedaOtroDetall>();
		// restando los detalles
		for (DetalleHistorialBoveda detBoveda : detalleHistorialBoveda) {
			BigDecimal valorMoneda = detBoveda.getMonedaDenominacion().getValor();
			for (GenericDetalle detTrans : detalleTransaccion) {
				BigDecimal valorMonedaTrans = detTrans.getValor();
				if (valorMoneda.equals(valorMonedaTrans)) {
					// restar los valores de cantidad
					BigInteger cantidadActual = detBoveda.getCantidad();
					BigInteger cantidadTrans = detTrans.getCantidad();

					BigInteger cantidadFinal = cantidadActual.add(cantidadTrans.multiply(factor));
					if (cantidadFinal.compareTo(BigInteger.ZERO) == -1)
						throw new RollbackFailureException("Saldo insuficiente, no se puede modificar el saldo de boveda");
					detBoveda.setCantidad(cantidadFinal);

					// creando el detalle para la base de datos
					TransaccionBovedaOtroDetall det = new TransaccionBovedaOtroDetall();
					det.setCantidad(detTrans.getCantidad());
					det.setMonedaDenominacion(detBoveda.getMonedaDenominacion());
					detalleTransaccionBovOtro.add(det);
					break;
				}
			}
		}

		// verificando el que saldoActual - montoTransaccion == sumatoria final
		// de detalleHistorialBoveda
		BigDecimal saldoFinalConResta = saldoActual.subtract(montoTransaccion);
		BigDecimal saldoFinalConHistorial = BigDecimal.ZERO;
		for (DetalleHistorialBoveda det : detalleHistorialBoveda) {
			BigInteger cantidad = det.getCantidad();
			BigDecimal valor = det.getMonedaDenominacion().getValor();
			saldoFinalConHistorial.add(valor.multiply(new BigDecimal(cantidad)));
		}
		if (saldoFinalConResta.compareTo(saldoFinalConHistorial) != 0)
			throw new RollbackFailureException("No se pudo realizar la transaccion, el detalle de transaccion enviado no coincide con el historial de boveda, verifique que ninguna MONEDA DENOMINACION este inactiva");

		for (DetalleHistorialBoveda det : detalleHistorialBoveda) {
			detalleHistorialBovedaDAO.update(det);
		}

		// crear Transaccion Entidad boveda
		Calendar calendar = Calendar.getInstance();

		TransaccionBovedaOtro transaccionBovedaOtro = new TransaccionBovedaOtro();
		transaccionBovedaOtro.setEntidad(entidad);
		transaccionBovedaOtro.setEstado(true);
		transaccionBovedaOtro.setFecha(calendar.getTime());
		transaccionBovedaOtro.setHistorialBoveda(historialBoveda);
		transaccionBovedaOtro.setHora(calendar.getTime());
		transaccionBovedaOtro.setObservacion(null);
		transaccionBovedaOtro.setSaldoDisponible(saldoFinalConResta);
		transaccionBovedaOtro.setTipoTransaccion(factor.compareTo(BigInteger.ZERO) >= 1 ? "INGRESO" : "EGRESO");

		transaccionBovedaOtroDAO.create(transaccionBovedaOtro);

		for (TransaccionBovedaOtroDetall detalle : detalleTransaccionBovOtro) {
			detalle.setTransaccionBovedaOtro(transaccionBovedaOtro);
			detalleTransaccionBovedaOtroDAO.create(detalle);

		}

		return transaccionBovedaOtro.getIdTransaccionBovedaOtro();
	}

	@Override
	public BigInteger crearTransaccionBovedaBoveda(BigInteger idBovedaOrigen, BigInteger idBovedaDestino, Set<GenericDetalle> detalleTransaccion) throws NonexistentEntityException, RollbackFailureException {
		Boveda bovedaOrigen = bovedaDAO.find(idBovedaOrigen);
		Boveda bovedaDestino = bovedaDAO.find(idBovedaDestino);
		if (bovedaOrigen == null)
			throw new NonexistentEntityException("Boveda origen no encontrada");
		if (bovedaDestino == null)
			throw new NonexistentEntityException("Boveda destino no encontrada");

		HistorialBoveda historialBovedaOrigen = getHistorialActivo(bovedaOrigen.getIdBoveda());
		HistorialBoveda historialBovedaDestino = getHistorialActivo(bovedaDestino.getIdBoveda());
		if (historialBovedaOrigen == null)
			throw new NonexistentEntityException("Boveda origen no tiene historial activo");
		if (historialBovedaDestino == null)
			throw new NonexistentEntityException("Boveda destino no tiene historial activo");

		// sacando historial
		Set<DetalleHistorialBoveda> detalleHistorialBovedaOrigen = historialBovedaOrigen.getDetalleHistorialBovedas();
		Set<DetalleHistorialBoveda> detalleHistorialBovedaDestino = historialBovedaDestino.getDetalleHistorialBovedas();

		// sacando saldos totales
		BigDecimal saldoActualOrigen = BigDecimal.ZERO;
		BigDecimal saldoActualDestino = BigDecimal.ZERO;

		BigDecimal montoTransaccion = BigDecimal.ZERO;

		for (DetalleHistorialBoveda det : detalleHistorialBovedaOrigen) {
			BigInteger cantidad = det.getCantidad();
			BigDecimal valor = det.getMonedaDenominacion().getValor();
			saldoActualDestino.add(valor.multiply(new BigDecimal(cantidad)));
		}
		for (DetalleHistorialBoveda det : detalleHistorialBovedaDestino) {
			BigInteger cantidad = det.getCantidad();
			BigDecimal valor = det.getMonedaDenominacion().getValor();
			saldoActualDestino.add(valor.multiply(new BigDecimal(cantidad)));
		}
		for (GenericDetalle det : detalleTransaccion) {
			BigInteger cantidad = det.getCantidad();
			BigDecimal valor = det.getValor();
			montoTransaccion.add(valor.multiply(new BigDecimal(cantidad)));
		}

		Set<TransaccionBovedaBovedaDetalle> detalleTransaccionBovedaBoveda = new HashSet<TransaccionBovedaBovedaDetalle>();
		// restando los detalles
		for (DetalleHistorialBoveda detBoveda : detalleHistorialBovedaOrigen) {
			BigDecimal valorMoneda = detBoveda.getMonedaDenominacion().getValor();
			for (GenericDetalle detTrans : detalleTransaccion) {
				BigDecimal valorMonedaTrans = detTrans.getValor();
				if (valorMoneda.equals(valorMonedaTrans)) {
					TransaccionBovedaBovedaDetalle transDet = new TransaccionBovedaBovedaDetalle();
					transDet.setCantidad(detTrans.getCantidad());
					transDet.setMonedaDenominacion(detBoveda.getMonedaDenominacion());

					detalleTransaccionBovedaBoveda.add(transDet);
					break;
				}
			}
		}

		// crear transaccion boveda boveda
		Calendar calendar = Calendar.getInstance();
		TransaccionBovedaBoveda transaccionBovedaBoveda = new TransaccionBovedaBoveda();

		transaccionBovedaBoveda.setEstadoSolicitud(true);
		transaccionBovedaBoveda.setEstadoConfirmacion(false);
		transaccionBovedaBoveda.setFecha(calendar.getTime());
		transaccionBovedaBoveda.setHora(calendar.getTime());
		transaccionBovedaBoveda.setHistorialBovedaOrigen(historialBovedaOrigen);
		transaccionBovedaBoveda.setHistorialBovedaDestino(historialBovedaDestino);
		transaccionBovedaBoveda.setSaldoDisponibleOrigen(saldoActualOrigen.add(montoTransaccion));
		transaccionBovedaBoveda.setSaldoDisponibleDestino(null);
		transaccionBovedaBoveda.setTrabajadorOrigen(null);
		transaccionBovedaBoveda.setTrabajadorDestino(null);
		transaccionBovedaBoveda.setObservacion("Transaccion agencia/agencia");

		transaccionBovedaBovedaDAO.create(transaccionBovedaBoveda);

		for (TransaccionBovedaBovedaDetalle det : detalleTransaccionBovedaBoveda) {
			det.setTransaccionBovedaBoveda(transaccionBovedaBoveda);
			detalleTransaccionBovedaBovedaDAO.create(det);
		}
		
		return transaccionBovedaBoveda.getIdTransaccionBovedaBoveda();
	}

}
