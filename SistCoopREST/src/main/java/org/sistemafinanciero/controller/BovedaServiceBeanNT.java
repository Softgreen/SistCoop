package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
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
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.DetalleHistorialBoveda;
import org.sistemafinanciero.entity.HistorialBoveda;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.sistemafinanciero.service.nt.BovedaServiceNT;

@Named
@Stateless
@Remote(BovedaServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BovedaServiceBeanNT implements BovedaServiceNT {

	@Inject
	private DAO<Object, Boveda> bovedaDAO;
	
	@Inject
	private DAO<Object, Agencia> agenciaDAO;

	@Inject
	private DAO<Object, HistorialBoveda> historialBovedaDAO;
	
	@Inject
	private DAO<Object, TransaccionBovedaCajaView> transaccionBovedaCajaViewDAO;

	public HistorialBoveda getHistorialActivo(BigInteger idBoveda) {
		Boveda boveda = bovedaDAO.find(idBoveda);
		if (boveda == null)
			return null;
		HistorialBoveda bovedaHistorial = null;
		QueryParameter queryParameter = QueryParameter.with("idboveda", idBoveda);
		List<HistorialBoveda> list = historialBovedaDAO.findByNamedQuery(HistorialBoveda.findByHistorialActivo, queryParameter.parameters());
		for (HistorialBoveda c : list) {
			bovedaHistorial = c;
			break;
		}
		return bovedaHistorial;
	}

	@Override
	public Boveda findById(BigInteger id) {
		Boveda boveda = bovedaDAO.find(id);
		Moneda moneda = boveda.getMoneda();
		Hibernate.initialize(moneda);
		return boveda;
	}

	@Override
	public List<Boveda> findAll() {
		return bovedaDAO.findAll();
	}

	@Override
	public int count() {
		return bovedaDAO.count();
	}

	@Override
	public List<Boveda> findAll(BigInteger idAgencia) {
		if (idAgencia == null)
			return null;
		QueryParameter queryParameter = QueryParameter.with("idAgencia", idAgencia);
		List<Boveda> list = bovedaDAO.findByNamedQuery(Boveda.findAllByIdAgencia, queryParameter.parameters());
		for (Boveda boveda : list) {
			Moneda moneda = boveda.getMoneda();
			Hibernate.initialize(moneda);
		}
		return list;
	}

	@Override
	public Set<GenericDetalle> getDetallePenultimo(BigInteger idBoveda) {
		Set<GenericDetalle> result = null;

		Boveda boveda = bovedaDAO.find(idBoveda);
		if (boveda == null)
			return null;

		// recuperando el historial activo
		result = new TreeSet<GenericDetalle>();
		
		//recuperando el historial penultimo		
		HistorialBoveda bovedaHistorial = null;
		QueryParameter queryParameter = QueryParameter.with("idboveda", idBoveda);
		List<HistorialBoveda> list = historialBovedaDAO.findByNamedQuery(HistorialBoveda.findByHistorialActivoPenultimo, queryParameter.parameters(), 1);
		for (HistorialBoveda c : list) {
			bovedaHistorial = c;
			break;
		}				

		if (bovedaHistorial != null) {
			Set<DetalleHistorialBoveda> detalle = bovedaHistorial.getDetalleHistorialBovedas();
			for (DetalleHistorialBoveda det : detalle) {
				BigInteger cantidad = det.getCantidad();
				BigDecimal valor = det.getMonedaDenominacion().getValor();

				GenericDetalle gen = new GenericDetalle();
				gen.setCantidad(cantidad);
				gen.setValor(valor);

				result.add(gen);
			}
		} else {
			Moneda moneda = boveda.getMoneda();
			Set<MonedaDenominacion> denominaciones = moneda.getMonedaDenominacions();
			for (MonedaDenominacion denom : denominaciones) {
				BigInteger cantidad = BigInteger.ZERO;
				BigDecimal valor = denom.getValor();

				GenericDetalle gen = new GenericDetalle();
				gen.setCantidad(cantidad);
				gen.setValor(valor);

				result.add(gen);
			}

		}
		return result;
	}

	@Override
	public Set<GenericDetalle> getDetalle(BigInteger idBoveda) {
		Set<GenericDetalle> result = null;

		Boveda boveda = bovedaDAO.find(idBoveda);
		if (boveda == null)
			return null;

		// recuperando el historial activo
		result = new TreeSet<GenericDetalle>();
		HistorialBoveda bovedaHistorial = this.getHistorialActivo(idBoveda);

		if (bovedaHistorial != null) {
			Set<DetalleHistorialBoveda> detalle = bovedaHistorial.getDetalleHistorialBovedas();
			for (DetalleHistorialBoveda det : detalle) {
				BigInteger cantidad = det.getCantidad();
				BigDecimal valor = det.getMonedaDenominacion().getValor();

				GenericDetalle gen = new GenericDetalle();
				gen.setCantidad(cantidad);
				gen.setValor(valor);

				result.add(gen);
			}
		} else {
			Moneda moneda = boveda.getMoneda();
			Set<MonedaDenominacion> denominaciones = moneda.getMonedaDenominacions();
			for (MonedaDenominacion denom : denominaciones) {
				BigInteger cantidad = BigInteger.ZERO;
				BigDecimal valor = denom.getValor();

				GenericDetalle gen = new GenericDetalle();
				gen.setCantidad(cantidad);
				gen.setValor(valor);

				result.add(gen);
			}

		}
		return result;
	}

	@Override
	public Set<GenericDetalle> getDetalle(BigInteger idBoveda, BigInteger idHistorialBoveda) {
		Set<GenericDetalle> result = null;

		Boveda boveda = bovedaDAO.find(idBoveda);
		if (boveda == null || idHistorialBoveda == null)
			return null;

		// recuperando el historial activo
		HistorialBoveda bovedaHistorial = historialBovedaDAO.find(idHistorialBoveda);
		if (!bovedaHistorial.getBoveda().equals(boveda))
			return null;

		// recorrer por todas las bovedas
		result = new HashSet<GenericDetalle>();

		Set<DetalleHistorialBoveda> detalle = bovedaHistorial.getDetalleHistorialBovedas();
		for (DetalleHistorialBoveda det : detalle) {
			BigInteger cantidad = det.getCantidad();
			BigDecimal valor = det.getMonedaDenominacion().getValor();

			GenericDetalle gen = new GenericDetalle();
			gen.setCantidad(cantidad);
			gen.setValor(valor);

			result.add(gen);
		}

		return result;
	}

	@Override
	public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja(BigInteger idAgencia) {
		Agencia agencia = agenciaDAO.find(idAgencia);
		if (agencia == null) {
			return null;
		}
		QueryParameter queryParameter = QueryParameter.with("idAgencia", agencia.getIdAgencia()).and("origen", TransaccionBovedaCajaOrigen.BOVEDA);
		List<TransaccionBovedaCajaView> list = transaccionBovedaCajaViewDAO.findByNamedQuery(TransaccionBovedaCajaView.findByAgenciaBovedaEnviados, queryParameter.parameters());
		return list;
	}

	@Override
	public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja(BigInteger idAgencia) {
		Agencia agencia = agenciaDAO.find(idAgencia);
		if (agencia == null) {
			return null;
		}
		QueryParameter queryParameter = QueryParameter.with("idAgencia", agencia.getIdAgencia()).and("origen", TransaccionBovedaCajaOrigen.CAJA);
		List<TransaccionBovedaCajaView> list = transaccionBovedaCajaViewDAO.findByNamedQuery(TransaccionBovedaCajaView.findByAgenciaBovedaRecibidos, queryParameter.parameters());
		return list;
	}
}
