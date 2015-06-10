package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.VariableSistema;
import org.sistemafinanciero.entity.type.Variable;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.VariableSistemaServiceTS;

@Named
@Stateless
@Remote(VariableSistemaServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VariableSistemaServiceBeanTS implements VariableSistemaServiceTS {

	@Inject
	private DAO<Object, VariableSistema> variableSistemaDAO;

	@Inject
	private DAO<Object, Moneda> monedaDAO;

	@Override
	public BigInteger create(VariableSistema t)
			throws PreexistingEntityException, RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(BigInteger id, VariableSistema t)
			throws NonexistentEntityException, PreexistingEntityException,
			RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(BigInteger id) throws NonexistentEntityException,
			RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTasaCambio(BigInteger idMonedaRecibida,
			BigInteger idMonedaEntregada, BigDecimal tasa) throws NonexistentEntityException, RollbackFailureException {
		
		Moneda monedaRecibida = monedaDAO.find(idMonedaRecibida);
		Moneda monedaEntregada = monedaDAO.find(idMonedaEntregada);
		if (monedaRecibida == null)
			throw new NonexistentEntityException("Moneda recibida no encontrada");
		if (monedaEntregada == null)
			throw new NonexistentEntityException("Moneda entregada no encontrada");
		
		String simboloMonedaRecibida = monedaRecibida.getSimbolo().toUpperCase();
		String simboloMonedaEntregada = monedaEntregada.getSimbolo().toUpperCase();		

		switch (simboloMonedaRecibida) {
		case "S/.":
			if (simboloMonedaEntregada.equalsIgnoreCase("$")) {
				VariableSistema var = getVariable(Variable.TASA_COMPRA_DOLAR);
				var.setValor(tasa);
				variableSistemaDAO.update(var);
			}
			if (simboloMonedaEntregada.equalsIgnoreCase("€")) {
				VariableSistema var = getVariable(Variable.TASA_COMPRA_EURO);
				var.setValor(tasa);
				variableSistemaDAO.update(var);
			}
			break;
		case "$":
			if (simboloMonedaEntregada.equalsIgnoreCase("S/.")) {
				VariableSistema var = getVariable(Variable.TASA_VENTA_DOLAR);
				var.setValor(tasa);
				variableSistemaDAO.update(var);
			}
			if (simboloMonedaEntregada.equalsIgnoreCase("€")) {
				throw new RollbackFailureException("Operacion no permitida");
			}
			break;
		case "€":
			if (simboloMonedaEntregada.equalsIgnoreCase("S/.")) {
				VariableSistema var = getVariable(Variable.TASA_VENTA_EURO);
				var.setValor(tasa);
				variableSistemaDAO.update(var);
			}
			if (simboloMonedaEntregada.equalsIgnoreCase("$")) {
				throw new RollbackFailureException("Operacion no permitida");
			}
			break;
		default:
			break;
		}
		
	}
	
	public VariableSistema getVariable(Variable variable) {
		QueryParameter queryParameter = QueryParameter.with("denominacion", variable.toString());
		List<VariableSistema> list = variableSistemaDAO.findByNamedQuery(VariableSistema.findByDenominacion, queryParameter.parameters());
		for (VariableSistema variableSistema : list) {
			return variableSistema;
		}
		return null;
	}
	
}
