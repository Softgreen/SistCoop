package org.sistemafinanciero.service.ts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.type.LugarPagoComision;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPendienteCaja;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface SessionServiceTS {

	public BigInteger abrirCaja() throws RollbackFailureException;

	public BigInteger cerrarCaja(Set<GenericMonedaDetalle> detalleCaja) throws RollbackFailureException;

	public Map<Boveda, BigDecimal> getDiferenciaSaldoCaja(Set<GenericMonedaDetalle> detalle);

	public BigInteger crearAporte(BigInteger idSocio, BigDecimal monto, int mes, int anio, String referencia) throws RollbackFailureException;

	public BigInteger retiroCuentaAporte(BigInteger idSocio) throws RollbackFailureException;

	public BigInteger crearTransaccionBancaria(String numeroCuenta, BigDecimal monto, String referencia, BigDecimal interes) throws RollbackFailureException;

	public BigInteger crearTransaccionCompraVenta(Tipotransaccioncompraventa tipoTransaccion, BigInteger idMonedaRecibido, BigInteger idMonedaEntregado, BigDecimal montoRecibido, BigDecimal montoEntregado, BigDecimal tasaCambio, String referencia) throws RollbackFailureException;

	public BigInteger crearTransferenciaBancaria(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto, String referencia) throws RollbackFailureException;

	public BigInteger crearTransaccionCheque(String numeroChequeUnico, BigDecimal monto, String tipoDocumento, String numeroDocumento, String persona, String observacion) throws RollbackFailureException;
	
	public BigInteger crearTransaccionGiro(BigInteger idAgenciaOrigen, BigInteger idAgenciaDestino, String numeroDocumentoEmisor, String clienteEmisor, String numeroDocumentoReceptor, String clienteReceptor, BigInteger idMoneda, BigDecimal monto, BigDecimal comision, LugarPagoComision lugarPagoComision, boolean estadoPagoComision) throws RollbackFailureException;
	
	public BigInteger crearTransaccionSobreGiro(BigInteger idSocio, BigInteger idMoneda, BigDecimal monto, BigDecimal interes, Date fechaLimitePago) throws RollbackFailureException;
	
	public BigInteger crearTransaccionHistorialSobreGiro(BigInteger idSobreGiro, BigDecimal monto) throws RollbackFailureException;
	  
	public void extornarTransaccion(BigInteger idTransaccion) throws RollbackFailureException;
	
	public void extornarGiro(BigInteger idGiro) throws RollbackFailureException;

	public BigInteger[] crearCuentaBancariaPlazoFijoConDeposito(TipoCuentaBancaria tipoCuentaBancaria, String codigoAgencia, BigInteger idMoneda, BigDecimal monto, BigDecimal tasaInteres, TipoPersona tipoPersona, BigInteger idPersona, Integer periodo, int cantRetirantes, List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException;

	public BigInteger cancelarCuentaBancariaConRetiro(BigInteger id) throws RollbackFailureException;

	public BigInteger cancelarSocioConRetiro(BigInteger idSocio) throws RollbackFailureException;

	public BigInteger crearPendienteCaja(TipoPendienteCaja tipoPendienteCaja, BigInteger idBoveda, BigDecimal monto, String observacion, BigInteger idPendienteRelacionado) throws RollbackFailureException;

	public BigInteger crearTransaccionBovedaCaja(BigInteger idBoveda, Set<GenericDetalle> detalleTransaccion, TransaccionBovedaCajaOrigen origen) throws RollbackFailureException;
	
	public BigInteger crearTransaccionBovedaCajaOrigenBoveda(BigInteger idBoveda, BigInteger idCaja, Set<GenericDetalle> detalleTransaccion, TransaccionBovedaCajaOrigen origen) throws RollbackFailureException;

	public BigInteger crearTransaccionCajaCaja(BigInteger idCajadestino, BigInteger idMoneda, BigDecimal monto, String observacion) throws RollbackFailureException;

	public void cancelarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) throws RollbackFailureException;

	public void confirmarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) throws RollbackFailureException;

	public void cancelarTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) throws RollbackFailureException;

	public void confirmarTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) throws RollbackFailureException;

}
