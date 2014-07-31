package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.TransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.dto.ResumenOperacionesCaja;
import org.sistemafinanciero.entity.dto.VoucherCompraVenta;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCuentaAporte;
import org.sistemafinanciero.entity.dto.VoucherTransferenciaBancaria;
import org.sistemafinanciero.service.nt.CajaServiceNT;

@Named
@Stateless
@Remote(CajaServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBeanNT implements CajaServiceNT {

	@Override
	public Caja findById(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Caja> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<Boveda> getBovedas(BigInteger idCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Moneda> getMonedas(BigInteger idCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<HistorialCaja> getHistorialCaja(BigInteger idCaja, Date dateDesde, Date dateHasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<GenericMonedaDetalle> getDetalleCaja(BigInteger idCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<GenericMonedaDetalle> getDetalleCaja(BigInteger idCaja, BigInteger idHistorialCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja(BigInteger idCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja(BigInteger idCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja(BigInteger idCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja(BigInteger idCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja(BigInteger idCaja, BigInteger idHistorialCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja(BigInteger idCaja, BigInteger idHistorialCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja(BigInteger idCaja, BigInteger idHistorialCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja(BigInteger idCaja, BigInteger idHistorialCaja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CajaCierreMoneda> getVoucherCierreCaja(BigInteger idHistorial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResumenOperacionesCaja getResumenOperacionesCaja(BigInteger idHistorial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoucherTransaccionCuentaAporte getVoucherCuentaAporte(BigInteger idTransaccion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoucherTransaccionBancaria getVoucherTransaccionBancaria(BigInteger idTransaccionBancaria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoucherTransferenciaBancaria getVoucherTransferenciaBancaria(BigInteger idTransferencia) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VoucherCompraVenta getVoucherCompraVenta(BigInteger idTransaccionCompraVenta) {
		// TODO Auto-generated method stub
		return null;
	}

}
