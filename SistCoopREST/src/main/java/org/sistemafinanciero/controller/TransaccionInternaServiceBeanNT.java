package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.BovedaCaja;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.Entidad;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.sistemafinanciero.entity.TransaccionBovedaCajaDetalle;
import org.sistemafinanciero.entity.TransaccionBovedaOtro;
import org.sistemafinanciero.entity.TransaccionBovedaOtroDetall;
import org.sistemafinanciero.entity.TransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBovedaBoveda;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBovedaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionEntidadBoveda;
import org.sistemafinanciero.service.nt.TransaccionInternaServiceNT;

@Named
@Stateless
@Remote(TransaccionInternaServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TransaccionInternaServiceBeanNT implements TransaccionInternaServiceNT {

	@Inject
	private DAO<Object, TransaccionBovedaCaja> transaccionBovedaCajaDAO;

	@Inject
	private DAO<Object, TransaccionCajaCaja> transaccionCajaCajaDAO;

	@Inject
	private DAO<Object, TransaccionBovedaOtro> transaccionBovedaOtroDAO;

	@Override
	public VoucherTransaccionCajaCaja getVoucherTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) {
		TransaccionCajaCaja transaccion = transaccionCajaCajaDAO.find(idTransaccionCajaCaja);
		if (transaccion == null) {
			return null;
		}

		// recuperando datos
		Caja cajaOrigen = transaccion.getHistorialCajaOrigen().getCaja();
		Caja cajaDestino = transaccion.getHistorialCajaDestino().getCaja();
		Moneda moneda = transaccion.getMoneda();
		BigDecimal montoTransaccion = transaccion.getMonto();

		Set<BovedaCaja> listCO = cajaOrigen.getBovedaCajas();
		Agencia agenciaCajaOrigen = null;
		for (BovedaCaja bovedaCaja : listCO) {
			agenciaCajaOrigen = bovedaCaja.getBoveda().getAgencia();
			break;
		}

		Set<BovedaCaja> listCD = cajaDestino.getBovedaCajas();
		Agencia agenciaCajaDestino = null;
		for (BovedaCaja bovedaCaja : listCD) {
			agenciaCajaDestino = bovedaCaja.getBoveda().getAgencia();
			break;
		}

		Hibernate.initialize(moneda);
		VoucherTransaccionCajaCaja voucherTransaccionCajaCaja = new VoucherTransaccionCajaCaja();

		if (agenciaCajaOrigen == agenciaCajaDestino) {
			voucherTransaccionCajaCaja.setId(transaccion.getIdTransaccionCajaCaja());// 18
			voucherTransaccionCajaCaja.setEstadoConfirmacion(transaccion.getEstadoConfirmacion());
			voucherTransaccionCajaCaja.setEstadoSolicitud(transaccion.getEstadoSolicitud());
			voucherTransaccionCajaCaja.setFecha(transaccion.getFecha());
			voucherTransaccionCajaCaja.setHora(transaccion.getHora());
			voucherTransaccionCajaCaja.setObservacion(transaccion.getObservacion());
			voucherTransaccionCajaCaja.setCajaOrigenDenominacion(cajaOrigen.getDenominacion());
			voucherTransaccionCajaCaja.setCajaOrigenAbreviatura(cajaOrigen.getAbreviatura());
			voucherTransaccionCajaCaja.setCajaDestinoDenominacion(cajaDestino.getDenominacion());
			voucherTransaccionCajaCaja.setCajaDestinoAbrevitura(cajaDestino.getAbreviatura());
			voucherTransaccionCajaCaja.setSaldoDisponibleOrigen(transaccion.getSaldoDisponibleOrigen());
			voucherTransaccionCajaCaja.setSaldoDisponibleDestino(transaccion.getSaldoDisponibleDestino());
			voucherTransaccionCajaCaja.setMoneda(moneda);
			voucherTransaccionCajaCaja.setMonto(montoTransaccion);
			voucherTransaccionCajaCaja.setTrabajadorCajaOrigen(transaccion.getHistorialCajaOrigen().getTrabajador());
			voucherTransaccionCajaCaja.setTrabajadorCajaDestino(transaccion.getHistorialCajaDestino().getTrabajador());
			voucherTransaccionCajaCaja.setAgenciaDenominacion(agenciaCajaOrigen.getDenominacion());
			voucherTransaccionCajaCaja.setAgenciaAbreviatura(agenciaCajaOrigen.getAbreviatura());
		} else {
			voucherTransaccionCajaCaja = null;
		}
		return voucherTransaccionCajaCaja;
	}

	@Override
	public VoucherTransaccionBovedaCaja getVoucherTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) {
		TransaccionBovedaCaja transaccion = transaccionBovedaCajaDAO.find(idTransaccionBovedaCaja);
		if (transaccion == null)
			return null;

		// recuperando datos
		Caja caja = transaccion.getHistorialCaja().getCaja();
		Boveda boveda = transaccion.getHistorialBoveda().getBoveda();
		Moneda moneda = boveda.getMoneda();

		Set<TransaccionBovedaCajaDetalle> detalleTransaccion = transaccion.getTransaccionBovedaCajaDetalls();
		BigDecimal totalTransaccion = BigDecimal.ZERO;
		Set<BovedaCaja> list = caja.getBovedaCajas();
		Agencia agencia = null;
		for (BovedaCaja bovedaCaja : list) {
			agencia = bovedaCaja.getBoveda().getAgencia();
			break;
		}
		for (TransaccionBovedaCajaDetalle det : detalleTransaccion) {
			MonedaDenominacion denominacion = det.getMonedaDenominacion();
			BigDecimal valor = denominacion.getValor();
			BigInteger cantidad = det.getCantidad();
			BigDecimal subtotal = valor.multiply(new BigDecimal(cantidad));
			totalTransaccion = totalTransaccion.add(subtotal);
		}

		Hibernate.initialize(moneda);
		VoucherTransaccionBovedaCaja voucher = new VoucherTransaccionBovedaCaja();

		voucher.setId(transaccion.getIdTransaccionBovedaCaja());
		voucher.setAgenciaAbreviatura(agencia.getAbreviatura());
		voucher.setAgenciaDenominacion(agencia.getDenominacion());
		voucher.setEstadoConfirmacion(transaccion.getEstadoConfirmacion());
		voucher.setEstadoSolicitud(transaccion.getEstadoSolicitud());
		voucher.setFecha(transaccion.getFecha());
		voucher.setHora(transaccion.getHora());
		voucher.setMoneda(moneda);
		voucher.setMonto(totalTransaccion);
		voucher.setObservacion(transaccion.getObservacion());
		voucher.setOrigen(transaccion.getOrigen());
		voucher.setCajaDenominacion(caja.getDenominacion());
		voucher.setCajaAbreviatura(caja.getAbreviatura());
		voucher.setTrabajador(transaccion.getHistorialCaja().getTrabajador());

		if (voucher.getOrigen().toString().equals("CAJA")) {
			voucher.setOrigenTransaccion(caja.getDenominacion() + " (" + caja.getAbreviatura() + ")");
			voucher.setDestinoTransaccion(boveda.getDenominacion());
		} else {
			if (voucher.getOrigen().toString().equals("BOVEDA")) {
				voucher.setOrigenTransaccion(boveda.getDenominacion());
				voucher.setDestinoTransaccion(caja.getDenominacion() + " (" + caja.getAbreviatura() + ")");
			}
		}
		return voucher;
	}

	@Override
	public TreeSet<GenericDetalle> getDetalleTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) {
		TransaccionBovedaCaja transaccionBovedaCaja = transaccionBovedaCajaDAO.find(idTransaccionBovedaCaja);
		if (transaccionBovedaCaja == null)
			return null;

		Set<TransaccionBovedaCajaDetalle> set = transaccionBovedaCaja.getTransaccionBovedaCajaDetalls();

		TreeSet<GenericDetalle> detalle = new TreeSet<GenericDetalle>();
		for (TransaccionBovedaCajaDetalle genericDetalle : set) {
			GenericDetalle d = new GenericDetalle();
			d.setCantidad(genericDetalle.getCantidad());
			d.setValor(genericDetalle.getMonedaDenominacion().getValor());
			detalle.add(d);
		}
		return detalle;
	}

	@Override
	public VoucherTransaccionEntidadBoveda getVoucherTransaccionEntidadBoveda(BigInteger idTransaccionEntidadBoveda) {
		TransaccionBovedaOtro transaccion = transaccionBovedaOtroDAO.find(idTransaccionEntidadBoveda);
		if (transaccion == null)
			return null;

		// recuperando datos
		Entidad entidad = transaccion.getEntidad();
		Boveda boveda = transaccion.getHistorialBoveda().getBoveda();
		Moneda moneda = boveda.getMoneda();

		Set<TransaccionBovedaOtroDetall> detalleTransaccion = transaccion.getTransaccionBovedaOtroDetalls();
		BigDecimal totalTransaccion = BigDecimal.ZERO;
		Agencia agencia = boveda.getAgencia();
		for (TransaccionBovedaOtroDetall det : detalleTransaccion) {
			MonedaDenominacion denominacion = det.getMonedaDenominacion();
			BigDecimal valor = denominacion.getValor();
			BigInteger cantidad = det.getCantidad();
			BigDecimal subtotal = valor.multiply(new BigDecimal(cantidad));
			totalTransaccion = totalTransaccion.add(subtotal);
		}

		Hibernate.initialize(moneda);

		VoucherTransaccionEntidadBoveda voucher = new VoucherTransaccionEntidadBoveda();

		voucher.setId(transaccion.getIdTransaccionBovedaOtro());
		voucher.setAgenciaAbreviatura(agencia.getAbreviatura());
		voucher.setAgenciaDenominacion(agencia.getDenominacion());
		voucher.setEstado(transaccion.getEstado());
		voucher.setFecha(transaccion.getFecha());
		voucher.setHora(transaccion.getHora());
		voucher.setMoneda(moneda);
		voucher.setMonto(totalTransaccion);
		voucher.setObservacion(transaccion.getObservacion());
		voucher.setTipoTransaccion(transaccion.getTipoTransaccion().toString());

		voucher.setBovedaDenominacion(boveda.getDenominacion());
		voucher.setEntidad(entidad.getDenominacion());

		voucher.setTrabajador(transaccion.getObservacion());

		return voucher;
	}

	@Override
	public VoucherTransaccionBovedaBoveda getVoucherTransaccionBovedaBoveda(BigInteger idTransaccionBovedaBoveda) {
		// TODO Auto-generated method stub
		return null;
	}

}
