package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.SessionServiceTS;

@Stateless
@Named
@Remote(SessionServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SessionServiceBeanTS implements SessionServiceTS {

	@Override
	public BigInteger abrirCaja() throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger cerrarCaja(Set<GenericMonedaDetalle> detalleCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearAporte(BigInteger idSocio, BigDecimal monto, int mes, int anio, String referencia) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger retiroCuentaAporte(BigInteger idSocio) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransaccionBancaria(String numeroCuenta, BigDecimal monto, String referencia) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransaccionCompraVenta(Tipotransaccioncompraventa tipoTransaccion, BigInteger idMonedaRecibido, BigInteger idMonedaEntregado, BigDecimal montoRecibido, BigDecimal montoEntregado, BigDecimal tasaCambio, String referencia) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransferenciaBancaria(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto, String referencia) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void extornarTransaccion(BigInteger idTransaccion) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public BigInteger[] crearCuentaBancariaPlazoFijoConDeposito(String codigo, BigInteger idMoneda, TipoPersona tipoPersona, BigInteger idPersona, int cantRetirantes, BigDecimal monto, int periodo, BigDecimal tasaInteres, List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger cancelarCuentaBancariaConRetiro(BigInteger id) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger cancelarSocioConRetiro(BigInteger idSocio) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearPendiente(BigInteger idBoveda, BigDecimal monto, String observacion) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransaccionBovedaCaja(BigInteger idBoveda, Set<GenericDetalle> detalleTransaccion) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransaccionCajaCaja(BigInteger idCajadestino, BigInteger idMoneda, BigDecimal monto, String observacion) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void confirmarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelarTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void confirmarTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

}