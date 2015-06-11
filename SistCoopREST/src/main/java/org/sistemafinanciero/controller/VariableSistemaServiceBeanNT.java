package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
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
import org.sistemafinanciero.service.nt.VariableSistemaServiceNT;

@Named
@Stateless
@Remote(VariableSistemaServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VariableSistemaServiceBeanNT implements VariableSistemaServiceNT {

	@Inject
	private DAO<Object, VariableSistema> variableSistemaDAO;

	@Inject
	private DAO<Object, Moneda> monedaDAO;

	@Override
	public BigDecimal getTasaCambio(BigInteger idMonedaRecibida, BigInteger idMonedaEntregada) {
		Moneda monedaRecibida = monedaDAO.find(idMonedaRecibida);
		Moneda monedaEntregada = monedaDAO.find(idMonedaEntregada);
		if (monedaRecibida == null)
			return null;
		if (monedaEntregada == null)
			return null;

		String simboloMonedaRecibida = monedaRecibida.getSimbolo().toUpperCase();
		String simboloMonedaEntregada = monedaEntregada.getSimbolo().toUpperCase();

		BigDecimal result = null;

		switch (simboloMonedaRecibida) {
		case "S/.":
			if (simboloMonedaEntregada.equalsIgnoreCase("$")) {
				VariableSistema var = getVariable(Variable.TASA_VENTA_DOLAR);
				result = var.getValor();
			}
			if (simboloMonedaEntregada.equalsIgnoreCase("€")) {
				VariableSistema var = getVariable(Variable.TASA_VENTA_EURO);
				result = var.getValor();
			}
			break;
		case "$":
			if (simboloMonedaEntregada.equalsIgnoreCase("S/.")) {
				VariableSistema var = getVariable(Variable.TASA_COMPRA_DOLAR);
				result = var.getValor();
			}
			if (simboloMonedaEntregada.equalsIgnoreCase("€")) {
				VariableSistema var1 = getVariable(Variable.TASA_COMPRA_DOLAR);
				VariableSistema var2 = getVariable(Variable.TASA_VENTA_EURO);
				result = var1.getValor().divide(var2.getValor(), 3, RoundingMode.FLOOR);
			}
			break;
		case "€":
			if (simboloMonedaEntregada.equalsIgnoreCase("S/.")) {
				VariableSistema var = getVariable(Variable.TASA_COMPRA_EURO);
				result = var.getValor();
			}
			if (simboloMonedaEntregada.equalsIgnoreCase("$")) {
				VariableSistema var1 = getVariable(Variable.TASA_COMPRA_EURO);
				VariableSistema var2 = getVariable(Variable.TASA_VENTA_DOLAR);
				result = var1.getValor().divide(var2.getValor(), 3, RoundingMode.FLOOR);
			}
			break;
		default:
			break;
		}
		return result;
	}

	@Override
	public VariableSistema findById(BigInteger id) {
		return variableSistemaDAO.find(id);
	}

	@Override
	public List<VariableSistema> findAll() {
		return variableSistemaDAO.findAll();
	}

	@Override
	public int count() {
		return variableSistemaDAO.count();
	}

	@Override
	public VariableSistema getVariable(Variable variable) {
		QueryParameter queryParameter = QueryParameter.with("denominacion", variable.toString());
		List<VariableSistema> list = variableSistemaDAO.findByNamedQuery(VariableSistema.findByDenominacion, queryParameter.parameters());
		for (VariableSistema variableSistema : list) {
			return variableSistema;
		}
		return null;
	}

	@Override
	public BigDecimal getMayorCuantia(BigInteger idMoneda) {
		Moneda moneda = monedaDAO.find(idMoneda);
		if(moneda == null)
			return null;		
		
		VariableSistema variableSistema = new VariableSistema();
		
		if(moneda.getSimbolo().toUpperCase().equals("S/.")){
			variableSistema = getVariable(Variable.TRANSACCION_MAYOR_CUANTIA_NUEVOS_SOLES);			
		} else if(moneda.getSimbolo().toUpperCase().equals("$")){
			variableSistema = getVariable(Variable.TRANSACCION_MAYOR_CUANTIA_DOLARES);
		} else if(moneda.getSimbolo().toUpperCase().equals("€")){
			variableSistema = getVariable(Variable.TRANSACCION_MAYOR_CUANTIA_EUROS);
		}			
					
		return variableSistema.getValor();
	}

}
