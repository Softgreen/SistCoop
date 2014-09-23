package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.HashSet;
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
import org.sistemafinanciero.entity.Sucursal;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.SucursalServiceTS;

@Named
@Stateless
@Remote(SucursalServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SucursalServiceBeanTS implements SucursalServiceTS {

	@Inject
	private DAO<Object, Sucursal> sucursalDAO;

	@Inject
	private Validator validator;

	@Override
	public BigInteger create(Sucursal t) throws PreexistingEntityException, RollbackFailureException {
		Set<ConstraintViolation<Sucursal>> violations = validator.validate(t);
		if (violations.isEmpty()) {
			sucursalDAO.create(t);
			return t.getIdSucursal();
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@Override
	public void update(BigInteger id, Sucursal t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		Sucursal agencia = sucursalDAO.find(id);
		if (agencia != null) {
			Set<ConstraintViolation<Sucursal>> violations = validator.validate(t);
			if (violations.isEmpty()) {
				t.setIdSucursal(id);
				sucursalDAO.update(agencia);
			} else {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
		} else {
			throw new NonexistentEntityException("Sucursal no existente, UPDATE no ejecutado");
		}
	}
	
	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Sucursal agencia = sucursalDAO.find(id);
		if (agencia != null) {
			sucursalDAO.delete(agencia);
		} else {
			throw new NonexistentEntityException("Sucursal no existente, DELETE no ejecutado");
		}
	}

}
