package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.sistemafinanciero.entity.BovedaCajaId;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.CajaServiceTS;

@Named
@Stateless
@Remote(CajaServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBeanTS implements CajaServiceTS {

	@Inject
	private DAO<Object, Caja> cajaDAO;

	@Inject
	private DAO<Object, Boveda> bovedaDAO;

	@Inject
	private DAO<Object, BovedaCaja> bovedaCajaDAO;

	@Inject
	private Validator validator;

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

				Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();

				Map<BigInteger, Boveda> total = new HashMap<BigInteger, Boveda>();
				for (BovedaCaja bovedaCaja : bovedaCajas) {
					Boveda boveda = bovedaCaja.getBoveda();
					BigInteger idBoveda = boveda.getIdBoveda();
					total.put(idBoveda, boveda);
				}

				Set<BigInteger> union = new HashSet<BigInteger>(total.keySet());
				union.addAll(idBovedas);

				Set<BigInteger> restDelete = new HashSet<BigInteger>(union);
				restDelete.removeAll(idBovedas);

				Set<BigInteger> restCreate = new HashSet<BigInteger>(union);
				restCreate.removeAll(total.keySet());

				// operaciones
				for (BigInteger idDel : restDelete) {
					Boveda boveda = total.get(idDel);

					BovedaCajaId pk = new BovedaCajaId();
					pk.setIdBoveda(boveda.getIdBoveda());
					pk.setIdCaja(caja.getIdCaja());
					BovedaCaja bovedaCaja = bovedaCajaDAO.find(pk);

					if (bovedaCaja.getSaldo().compareTo(BigDecimal.ZERO) != 0)
						throw new RollbackFailureException("Boveda con saldo diferente de cero, no se puede quitar de la caja");
					else
						bovedaCajaDAO.delete(bovedaCaja);
				}

				for (BigInteger idDel : restCreate) {
					Boveda boveda = total.get(idDel);

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

}
