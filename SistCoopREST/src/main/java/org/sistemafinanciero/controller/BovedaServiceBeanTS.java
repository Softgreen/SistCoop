package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.BovedaCaja;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.DetalleHistorialCaja;
import org.sistemafinanciero.entity.HistorialBoveda;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.BovedaServiceTS;

@Named
@Stateless
@Remote(BovedaServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BovedaServiceBeanTS implements BovedaServiceTS {

	@Inject
	private DAO<Object, Boveda> bovedaDAO;

	@Inject
	private Validator validator;

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
/*
		try {
			HistorialBoveda historialCajaOld = this.getClass();

			// abriendo caja
			boveda.setAbierto(true);
			boveda.setEstadoMovimiento(true);
			Set<ConstraintViolation<Caja>> violationsCaja = validator.validate(boveda);
			if (!violationsCaja.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsCaja));
			} else {
				cajaDAO.update(boveda);
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
			historialCajaNew.setCaja(boveda);
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
			LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
			throw new EJBException(e);
		}*/
		return null;
	}

}
