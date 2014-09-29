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
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.TrabajadorServiceTS;

@Named
@Stateless
@Remote(TrabajadorServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrabajadorServiceBeanTS implements TrabajadorServiceTS {

	@Inject
	private DAO<Object, Trabajador> trabajadorDAO;

	@Inject
	private DAO<Object, Agencia> agenciaDAO;
	
	@Inject
	private Validator validator;		

	@Override
	public BigInteger create(Trabajador t) throws PreexistingEntityException, RollbackFailureException {
		Agencia agencia = agenciaDAO.find(t.getAgencia().getIdAgencia());
		t.setAgencia(agencia);
		
		Set<ConstraintViolation<Trabajador>> violations = validator.validate(t);
		if (violations.isEmpty()) {
			trabajadorDAO.create(t);
			return t.getIdTrabajador();
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@Override
	public void update(BigInteger id, Trabajador t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		Trabajador trabajador = trabajadorDAO.find(id);
		if (trabajador != null) {
			Set<ConstraintViolation<Trabajador>> violations = validator.validate(t);
			if (violations.isEmpty()) {
				t.setIdTrabajador(id);;
				trabajadorDAO.update(trabajador);
			} else {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
		} else {
			throw new NonexistentEntityException("Trabajador no existente, UPDATE no ejecutado");
		}
	}
	
	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Trabajador trabajador = trabajadorDAO.find(id);
		if (trabajador != null) {
			trabajadorDAO.delete(trabajador);
		} else {
			throw new NonexistentEntityException("Trabajador no existente, DELETE no ejecutado");
		}
	}


}
