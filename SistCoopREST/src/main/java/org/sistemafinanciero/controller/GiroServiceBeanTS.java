package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.Calendar;
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
import org.sistemafinanciero.entity.Giro;
import org.sistemafinanciero.entity.type.EstadoGiro;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.GiroServiceTS;

@Named
@Stateless
@Remote(GiroServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GiroServiceBeanTS implements GiroServiceTS {

	@Inject
	private DAO<Object, Giro> giroDAO;

	@Inject
	private Validator validator;

	@Override
	public BigInteger create(Giro t) throws PreexistingEntityException, RollbackFailureException {
		Set<ConstraintViolation<Giro>> violations = validator.validate(t);
		if (violations.isEmpty()) {
			giroDAO.create(t);
			return t.getIdGiro();
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@Override
	public void update(BigInteger id, Giro t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		Giro giro = giroDAO.find(id);		
		if (giro != null) {
		    
		    if(giro.getEstado().equals(EstadoGiro.COBRADO)){
	            throw new RollbackFailureException("Giro ya fue desembolsado, no se puede modificar");
	        }
	        if(giro.getEstado().equals(EstadoGiro.CANCELADO)){
	            throw new RollbackFailureException("Giro fue extornado, no se puede modificar");
	        }
	        
			Set<ConstraintViolation<Giro>> violations = validator.validate(t);
			if (violations.isEmpty()) {
				giro.setIdGiro(id);
				giro.setEstado(t.getEstado());
				giro.setEstadoPagoComision(t.getEstadoPagoComision());
				if(t.getEstado().equals(EstadoGiro.COBRADO)){
				    giro.setFechaDesembolso(Calendar.getInstance().getTime());
				}
				giroDAO.update(giro);
			} else {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
		} else {
			throw new NonexistentEntityException("Giro no existente, UPDATE no ejecutado");
		}
	}

	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Giro giro = giroDAO.find(id);
		if (giro != null) {
			giroDAO.delete(giro);
		} else {
			throw new NonexistentEntityException("Giro no existente, DELETE no ejecutado");
		}
	}

}
