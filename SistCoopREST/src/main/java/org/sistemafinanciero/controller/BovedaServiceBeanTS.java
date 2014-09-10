package org.sistemafinanciero.controller;

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
import org.sistemafinanciero.entity.HistorialBoveda;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
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
	private DAO<Object, HistorialBoveda> historialBovedaDAO;

	@Inject
	private DAO<Object, DetalleHistorialBoveda> detalleHistorialBovedaDAO;
	
	@Inject
	private Validator validator;
	
	@Inject
	private EntityManagerProducer em;

	@EJB
	private MonedaServiceNT monedaServiceNT;
	
	public HistorialBoveda getHistorialActivo(BigInteger idBoveda) {
		Boveda boveda = bovedaDAO.find(idBoveda);
		if(boveda == null)
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
		//Trabajador trabajador = getTrabajador();
		/*if (trabajador == null)
			throw new RollbackFailureException("No se encontró un trabajador para la caja");*/		

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
			//historialBovedaNew.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno() + " " + trabajador.getPersonaNatural().getApellidoMaterno() + ", " + trabajador.getPersonaNatural().getNombres());
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

}
