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
import org.sistemafinanciero.entity.BovedaCaja;
import org.sistemafinanciero.entity.BovedaCajaId;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.DetalleHistorialCaja;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TrabajadorCaja;
import org.sistemafinanciero.entity.TrabajadorCajaId;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.nt.MonedaServiceNT;
import org.sistemafinanciero.service.ts.CajaServiceTS;
import org.sistemafinanciero.util.EntityManagerProducer;
import org.sistemafinanciero.util.UsuarioSession;

@Named
@Stateless
@Remote(CajaServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBeanTS implements CajaServiceTS {

	@Inject
	private DAO<Object, Caja> cajaDAO;

	@Inject
	private DAO<Object, HistorialCaja> historialCajaDAO;

	@Inject
	private DAO<Object, DetalleHistorialCaja> detalleHistorialCajaDAO;
	
	@Inject
	private DAO<Object, Boveda> bovedaDAO;

	@Inject
	private DAO<Object, BovedaCaja> bovedaCajaDAO;

	@Inject
	private DAO<Object, TrabajadorCaja> trabajadorCajaDAO;

	@Inject
	private DAO<Object, Trabajador> trabajadorDAO;

	@Inject
	private EntityManagerProducer em;

	@EJB
	private MonedaServiceNT monedaServiceNT;
	
	@Inject
	private Validator validator;

	@Inject
	private UsuarioSession usuarioSession;
	
	private HistorialCaja getHistorialActivo(BigInteger idCaja) {
		Caja caja = cajaDAO.find(idCaja);
		HistorialCaja cajaHistorial = null;
		QueryParameter queryParameter = QueryParameter.with("idcaja", caja.getIdCaja());
		List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo, queryParameter.parameters());
		for (HistorialCaja c : list) {
			cajaHistorial = c;
			break;
		}
		return cajaHistorial;
	}
	
	private Trabajador getTrabajador() {
		String username = usuarioSession.getUsername();

		QueryParameter queryParameter = QueryParameter.with("username", username);
		List<Trabajador> list = trabajadorDAO.findByNamedQuery(Trabajador.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			Trabajador trabajador = null;
			for (Trabajador t : list) {
				trabajador = t;
			}
			return trabajador;
		} else {
			System.out.println("Error: mas de un usuario registrado");
			return null;
		}
	}
	
	@Override
	public BigInteger create(Caja t) throws PreexistingEntityException, RollbackFailureException {
		Set<ConstraintViolation<Caja>> violations = validator.validate(t);
		if (violations.isEmpty()) {
			cajaDAO.create(t);
			return t.getIdCaja();
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@Override
	public void update(BigInteger id, Caja t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(id);
		if (caja != null) {
			Set<ConstraintViolation<Caja>> violations = validator.validate(t);
			if (violations.isEmpty()) {
				caja.setDenominacion(t.getDenominacion());
				caja.setAbreviatura(t.getAbreviatura());
				cajaDAO.update(caja);
			} else {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
		} else {
			throw new NonexistentEntityException("caja no existente, UPDATE no ejecutado");
		}
	}

	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(id);
		if (caja != null) {
			cajaDAO.delete(caja);
		} else {
			throw new NonexistentEntityException("caja no existente, DELETE no ejecutado");
		}
	}

	@Override
	public BigInteger create(Caja caja, List<BigInteger> idBovedas) throws RollbackFailureException {
		Set<ConstraintViolation<Caja>> violations = validator.validate(caja);
		if (violations.isEmpty()) {
			caja.setAbierto(false);
			caja.setEstado(true);
			caja.setEstadoMovimiento(false);			
			cajaDAO.create(caja);
			for (BigInteger idBoveda : idBovedas) {
				Boveda boveda = bovedaDAO.find(idBoveda);
				if (boveda != null) {
					BovedaCaja bovedaCaja = new BovedaCaja();
					bovedaCaja.setId(null);
					bovedaCaja.setBoveda(boveda);
					bovedaCaja.setCaja(caja);
					bovedaCaja.setSaldo(BigDecimal.ZERO);

					BovedaCajaId id = new BovedaCajaId();
					id.setIdBoveda(boveda.getIdBoveda());
					id.setIdCaja(caja.getIdCaja());

					bovedaCaja.setId(id);

					bovedaCajaDAO.create(bovedaCaja);
				} else {
					throw new RollbackFailureException("Boveda no encontrada");
				}
			}

			return caja.getIdCaja();
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@Override
	public void update(BigInteger id, Caja t, List<BigInteger> idBovedas) throws RollbackFailureException {
		Caja caja = cajaDAO.find(id);
		if (caja != null) {
			Set<ConstraintViolation<Caja>> violations = validator.validate(t);
			if (violations.isEmpty()) {

				// List<Boveda> bovedasOfAgencia =
				// bovedaServiceNT.findAll(idAgencia);
				Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();

				// A=bovedas de la vista; B=bovedas de la BD
				Set<BigInteger> A = new HashSet<BigInteger>();
				A.addAll(idBovedas);
				Set<BigInteger> B = new HashSet<BigInteger>();
				for (BovedaCaja bc : bovedaCajas) {
					Boveda boveda = bc.getBoveda();
					B.add(boveda.getIdBoveda());
				}

				// A-B
				Set<BigInteger> bovedasParaCrear = new HashSet<BigInteger>(A);
				bovedasParaCrear.removeAll(B);

				// B-A
				Set<BigInteger> bovedasParaEliminar = new HashSet<BigInteger>(B);
				bovedasParaEliminar.removeAll(A);

				// elimando bovedas
				for (BigInteger idBoveda : bovedasParaEliminar) {
					Boveda boveda = bovedaDAO.find(idBoveda);

					BovedaCajaId pk = new BovedaCajaId();
					pk.setIdBoveda(boveda.getIdBoveda());
					pk.setIdCaja(caja.getIdCaja());
					BovedaCaja bovedaCaja = bovedaCajaDAO.find(pk);

					if (bovedaCaja.getSaldo().compareTo(BigDecimal.ZERO) != 0)
						throw new RollbackFailureException("Boveda:" + boveda.getDenominacion() + " con saldo diferente de cero, no se puede quitar de la caja");
					else
						bovedaCajaDAO.delete(bovedaCaja);
				}
				// creando bovedas
				for (BigInteger idBoveda : bovedasParaCrear) {
					Boveda boveda = bovedaDAO.find(idBoveda);

					BovedaCaja bovedaCaja = new BovedaCaja();
					bovedaCaja.setId(null);
					bovedaCaja.setBoveda(boveda);
					bovedaCaja.setCaja(caja);
					bovedaCaja.setSaldo(BigDecimal.ZERO);

					BovedaCajaId idCre = new BovedaCajaId();
					idCre.setIdBoveda(boveda.getIdBoveda());
					idCre.setIdCaja(caja.getIdCaja());

					bovedaCaja.setId(idCre);

					bovedaCajaDAO.create(bovedaCaja);
				}

				caja.setDenominacion(t.getDenominacion());
				caja.setAbreviatura(t.getAbreviatura());
				cajaDAO.update(caja);
			} else {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
		}
	}

	@Override
	public void desactivar(BigInteger idCaja) throws RollbackFailureException {
		Caja caja = cajaDAO.find(idCaja);
		if (caja == null)
			throw new RollbackFailureException("Caja no encontrada");
		boolean abierto = caja.getAbierto();
		if (abierto)
			throw new RollbackFailureException("Caja abierta");
		caja.setEstado(false);
		caja.setEstadoMovimiento(false);
		caja.setAbierto(false);
		cajaDAO.update(caja);
	}

	@Override
	public BigInteger createTrabajadorCaja(BigInteger idCaja, BigInteger idTrabajador) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(idCaja);
		Trabajador trabajador = trabajadorDAO.find(idTrabajador);
		if (caja != null && trabajador != null) {
			//verificando que el trabajador no exista
			TrabajadorCajaId id = new TrabajadorCajaId();
			id.setIdCaja(caja.getIdCaja());
			id.setIdTrabajador(trabajador.getIdTrabajador());
			TrabajadorCaja trabajadorCaja = trabajadorCajaDAO.find(id);
			if(trabajadorCaja != null)
				throw new RollbackFailureException("El trabajador ya fue asignado a la caja");
			
			//creanndo trabajador caja
			trabajadorCaja = new TrabajadorCaja();			
			trabajadorCaja.setId(id);
			trabajadorCaja.setCaja(caja);
			trabajadorCaja.setTrabajador(trabajador);
			trabajadorCajaDAO.create(trabajadorCaja);

			return idTrabajador;
		} else {
			throw new NonexistentEntityException("Caja o trabajador no encontrado");
		}
	}

	@Override
	public void deleteTrabajadorCaja(BigInteger idCaja, BigInteger idTrabajador) throws NonexistentEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(idCaja);
		Trabajador trabajador = trabajadorDAO.find(idTrabajador);
		if (caja != null && trabajador != null) {
			
			TrabajadorCajaId id = new TrabajadorCajaId();
			id.setIdCaja(caja.getIdCaja());
			id.setIdTrabajador(trabajador.getIdTrabajador());

			TrabajadorCaja trabajadorCaja = trabajadorCajaDAO.find(id);
			if(trabajadorCaja != null){
				trabajadorCajaDAO.delete(trabajadorCaja);
			} else {
				throw new NonexistentEntityException("No existe el trabajador para la caja indicada");
			}
			
		} else {
			throw new NonexistentEntityException("Caja o trabajador no encontrado");
		}
	}

	@Override
	public BigInteger abrir(BigInteger idCaja) throws NonexistentEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(idCaja);
		if (caja == null)
			throw new NonexistentEntityException("No se encontró caja");
		
		Trabajador trabajador = getTrabajador();
		if (trabajador == null)
			throw new RollbackFailureException("No se encontró un trabajador para la caja");

		Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedaCajas) {
			Boveda boveda = bovedaCaja.getBoveda();
			if (!boveda.getAbierto())
				throw new RollbackFailureException("Debe de abrir las bovedas asociadas a la caja(" + boveda.getDenominacion() + ")");
		}

		try {
			HistorialCaja historialCajaOld = this.getHistorialActivo(idCaja);

			// abriendo caja
			caja.setAbierto(true);
			caja.setEstadoMovimiento(true);
			Set<ConstraintViolation<Caja>> violationsCaja = validator.validate(caja);
			if (!violationsCaja.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsCaja));
			} else {
				cajaDAO.update(caja);
			}

			if (historialCajaOld != null) {
				historialCajaOld.setEstado(false);
				Set<ConstraintViolation<HistorialCaja>> violationsHistorialOld = validator.validate(historialCajaOld);
				if (!violationsHistorialOld.isEmpty()) {
					throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialOld));
				} else {
					historialCajaDAO.update(historialCajaOld);
				}
			}

			Calendar calendar = Calendar.getInstance();
			HistorialCaja historialCajaNew = new HistorialCaja();
			historialCajaNew.setCaja(caja);
			historialCajaNew.setFechaApertura(calendar.getTime());
			historialCajaNew.setHoraApertura(calendar.getTime());
			historialCajaNew.setEstado(true);
			historialCajaNew.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno() + " " + trabajador.getPersonaNatural().getApellidoMaterno() + ", " + trabajador.getPersonaNatural().getNombres());
			Set<ConstraintViolation<HistorialCaja>> violationsHistorialNew = validator.validate(historialCajaNew);
			if (!violationsHistorialNew.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialNew));
			} else {
				historialCajaDAO.create(historialCajaNew);
			}

			if (historialCajaOld != null) {
				Set<DetalleHistorialCaja> detalleHistorialCajas = historialCajaOld.getDetalleHistorialCajas();
				for (DetalleHistorialCaja detalleHistorialCaja : detalleHistorialCajas) {
					this.em.getEm().detach(detalleHistorialCaja);
					detalleHistorialCaja.setIdDetalleHistorialCaja(null);
					detalleHistorialCaja.setHistorialCaja(historialCajaNew);

					Set<ConstraintViolation<DetalleHistorialCaja>> violationsHistorialDetalle = validator.validate(detalleHistorialCaja);
					if (!violationsHistorialDetalle.isEmpty()) {
						throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialDetalle));
					} else {
						detalleHistorialCajaDAO.create(detalleHistorialCaja);
					}
				}
			} else {
				for (BovedaCaja bovedaCaja : bovedaCajas) {
					Moneda moneda = bovedaCaja.getBoveda().getMoneda();
					List<MonedaDenominacion> denominaciones = monedaServiceNT.getDenominaciones(moneda.getIdMoneda());
					for (MonedaDenominacion monedaDenominacion : denominaciones) {
						DetalleHistorialCaja detalleHistorialCaja = new DetalleHistorialCaja();
						detalleHistorialCaja.setCantidad(BigInteger.ZERO);
						detalleHistorialCaja.setHistorialCaja(historialCajaNew);
						detalleHistorialCaja.setMonedaDenominacion(monedaDenominacion);

						Set<ConstraintViolation<DetalleHistorialCaja>> violationsHistorialDetalle = validator.validate(detalleHistorialCaja);
						if (!violationsHistorialDetalle.isEmpty()) {
							throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsHistorialDetalle));
						} else {
							detalleHistorialCajaDAO.create(detalleHistorialCaja);
						}
					}
				}
			}
			return historialCajaNew.getIdHistorialCaja();
		} catch (ConstraintViolationException e) {			
			throw new EJBException(e);
		}
	}

	@Override
	public void congelar(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(id);
		if (caja == null)
			throw new RollbackFailureException("Caja no encontrada");
		if (!caja.getEstado())
			throw new RollbackFailureException("Caja inactiva, no se puede congelar");
		if (!caja.getAbierto())
			throw new RollbackFailureException("Caja cerrada, no se puede congelar");
		if (!caja.getEstadoMovimiento())
			throw new RollbackFailureException("Caja congelada, no se puede congelar nuevamente");
		caja.setEstadoMovimiento(false);
		cajaDAO.update(caja);
	}

	@Override
	public void descongelar(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Caja caja = cajaDAO.find(id);
		if (caja == null)
			throw new RollbackFailureException("Caja no Encontrada");
		if (!caja.getEstado())
			throw new RollbackFailureException("Caja inactiva, no se puede descongelar");
		if (!caja.getAbierto())
			throw new RollbackFailureException("Caja cerrada, no se puede descongelar");
		if (caja.getEstadoMovimiento())
			throw new RollbackFailureException("Caja descongelada, no se puede descongelar nuevamente");
		caja.setEstadoMovimiento(true);
		cajaDAO.update(caja);
	}

}
