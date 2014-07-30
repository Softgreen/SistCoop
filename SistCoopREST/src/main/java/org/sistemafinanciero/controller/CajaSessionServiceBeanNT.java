package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.hibernate.Hibernate;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.BovedaCaja;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PendienteCaja;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.service.nt.CajaSessionServiceNT;
import org.sistemafinanciero.service.nt.MonedaServiceNT;
import org.sistemafinanciero.util.Guard;
import org.sistemafinanciero.util.UsuarioSession;

@Stateless
@Named
@Interceptors(Guard.class)
@Remote(CajaSessionServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaSessionServiceBeanNT implements CajaSessionServiceNT {

	@Inject
	private UsuarioSession usuarioSession;

	@Inject
	private DAO<Object, Caja> cajaDAO;

	@Inject
	private DAO<Object, HistorialCaja> historialCajaDAO;

	@Inject
	private DAO<Object, HistorialTransaccionCaja> historialTransaccionCajaDAO;

	@Inject
	private DAO<Object, TransaccionBovedaCajaView> transaccionBovedaCajaViewDAO;

	@EJB
	private MonedaServiceNT monedaService;

	private Caja getCaja() {
		String username = usuarioSession.getUsername();
		QueryParameter queryParameter = QueryParameter.with("usuario", username);
		List<Caja> list = cajaDAO.findByNamedQuery(Caja.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			Caja caja = null;
			for (Caja c : list) {
				caja = c;
			}
			return caja;
		} else {
			System.out.println("Error: mas de un usuario registrado");
			return null;
		}
	}

	private HistorialCaja getHistorialActivo() {
		Caja caja = getCaja();
		HistorialCaja cajaHistorial = null;
		QueryParameter queryParameter = QueryParameter.with("idcaja", caja.getIdCaja());
		List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo, queryParameter.parameters());
		for (HistorialCaja c : list) {
			cajaHistorial = c;
			break;
		}
		return cajaHistorial;
	}

	@Override
	public Map<Boveda, BigDecimal> getDiferenciaSaldoCaja(Set<GenericMonedaDetalle> detalleCaja) {
		Map<Boveda, BigDecimal> result = new HashMap<Boveda, BigDecimal>();
		Caja caja = getCaja();
		Set<BovedaCaja> bovedas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedas) {
			Moneda moneda = bovedaCaja.getBoveda().getMoneda();
			for (GenericMonedaDetalle detalle : detalleCaja) {
				if (moneda.equals(detalle.getMoneda())) {
					if (bovedaCaja.getSaldo().compareTo(detalle.getTotal()) != 0) {
						Boveda boveda = bovedaCaja.getBoveda();
						Hibernate.initialize(boveda);
						BigDecimal diferencia = bovedaCaja.getSaldo().subtract(detalle.getTotal());
						result.put(boveda, diferencia);
					}
					break;
				}
			}
		}
		return result;
	}

	@Override
	public Set<PendienteCaja> getPendientesCaja() {
		HistorialCaja historial = getHistorialActivo();
		Set<PendienteCaja> result = historial.getPendienteCajas();
		for (PendienteCaja pendienteCaja : result) {
			Moneda moneda = pendienteCaja.getMoneda();
			Hibernate.initialize(pendienteCaja);
			Hibernate.initialize(moneda);
		}
		return result;
	}

	@Override
	public List<HistorialTransaccionCaja> getHistorialTransaccion() {
		HistorialCaja historial = this.getHistorialActivo();
		QueryParameter queryParameter = QueryParameter.with("idHistorialCaja", historial.getIdHistorialCaja());
		List<HistorialTransaccionCaja> list = historialTransaccionCajaDAO.findByNamedQuery(HistorialTransaccionCaja.findByHistorialCaja, queryParameter.parameters());
		return list;
	}

	@Override
	public List<HistorialTransaccionCaja> getHistorialTransaccion(String filterText) {
		List<HistorialTransaccionCaja> list = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("filterText", '%' + filterText + '%');
		list = historialTransaccionCajaDAO.findByNamedQuery(HistorialTransaccionCaja.findByTransaccion, parameters);
		return list;
	}

	@Override
	public PersonaNatural getPersonaOfSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Caja getCajaOfSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Agencia getAgenciaOfSession() {
		// TODO Auto-generated method stub
		return null;
	}

}