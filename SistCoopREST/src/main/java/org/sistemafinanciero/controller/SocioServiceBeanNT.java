package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.HistorialAportesSP;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Socio;
import org.sistemafinanciero.entity.SocioView;
import org.sistemafinanciero.entity.TipoDocumento;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.service.nt.SocioServiceNT;

@Named
@Stateless
@Remote(SocioServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SocioServiceBeanNT implements SocioServiceNT {

	@Inject
	private DAO<Object, Socio> socioDAO;

	@Inject
	private DAO<Object, SocioView> socioViewDAO;

	@Inject
	private DAO<Object, CuentaBancariaView> cuentaBancariaViewDAO;

	@Inject
	private DAO<Object, Beneficiario> beneficiarioDAO;
	
	@Override
	public SocioView findById(BigInteger id) {
		return socioViewDAO.find(id);
	}

	@Override
	public List<SocioView> findAll() {
		return socioViewDAO.findAll();
	}

	@Override
	public int count() {
		return socioViewDAO.count();
	}

	@Override
	public List<SocioView> findAllView(Boolean estadoCuentaAporte, Boolean estadoSocio) {
		return findAllView(estadoCuentaAporte, estadoSocio, null, null);
	}

	@Override
	public List<SocioView> findAllView(Boolean estadoCuentaAporte, Boolean estadoSocio, Integer offset, Integer limit) {
		return findAllView(null, estadoCuentaAporte, estadoSocio, null, null);
	}

	@Override
	public List<SocioView> findAllView(String filterText) {
		return findAllView(filterText, null, null, null, null);
	}

	@Override
	public List<SocioView> findAllView(String filterText, Boolean estadoCuentaAporte, Boolean estadoSocio) {
		return findAllView(filterText, estadoCuentaAporte, estadoSocio, null, null);
	}

	@Override
	public List<SocioView> findAllView(String filterText, Integer offset, Integer limit) {
		Boolean estadoCuentaAporte = null;
		Boolean estadoSocio = null;
		return findAllView(filterText, estadoCuentaAporte, estadoSocio, offset, limit);
	}

	@Override
	public List<SocioView> findAllView(String filterText, Boolean estadoCuentaAporte, Boolean estadoSocio, Integer offset, Integer limit) {
		List<SocioView> result = null;

		if (filterText == null)
			filterText = "";
		if (offset == null) {
			offset = 0;
		}
		offset = Math.abs(offset);
		if (limit != null) {
			limit = Math.abs(limit);
		}
		Integer offSetInteger = offset.intValue();
		Integer limitInteger = (limit != null ? limit.intValue() : null);

		// parametros de busqueda de estado socio
		if (estadoCuentaAporte == null)
			estadoCuentaAporte = true;

		List<Boolean> listEstado = new ArrayList<>();
		if (estadoSocio != null) {
			listEstado.add(estadoSocio);
		} else {
			listEstado.add(true);
			listEstado.add(false);
		}

		QueryParameter queryParameter = QueryParameter.with("modeEstado", listEstado).and("filtertext", '%' + filterText.toUpperCase() + '%');
		if (offSetInteger != null) {
			if (estadoCuentaAporte) {
				result = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioView, queryParameter.parameters(), offSetInteger, limitInteger);
			} else {
				result = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioViewAllHaveCuentaAporte, queryParameter.parameters(), offSetInteger, limitInteger);
			}
		} else {
			if (estadoCuentaAporte) {
				result = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioView, queryParameter.parameters());
			} else {
				result = socioViewDAO.findByNamedQuery(SocioView.FindByFilterTextSocioViewAllHaveCuentaAporte, queryParameter.parameters());
			}
		}
		return result;
	}

	@Override
	public SocioView find(TipoPersona tipoPersona, BigInteger idTipoDocumento, String numeroDocumento) {
		QueryParameter queryParameter = QueryParameter.with("tipoPersona", tipoPersona).and("idTipoDocumento", idTipoDocumento).and("numeroDocumento", numeroDocumento);
		List<SocioView> list = socioViewDAO.findByNamedQuery(SocioView.FindByTipoAndNumeroDocumento, queryParameter.parameters());
		if (list.size() <= 1) {
			SocioView socio = null;
			for (SocioView socioView : list) {
				socio = socioView;
			}
			return socio;
		} else {
			throw new EJBException("Se encontró mas de un socio activo");
		}
	}

	@Override
	public PersonaNatural getApoderado(BigInteger idSocio) {
		Socio socio = socioDAO.find(idSocio);
		if (socio == null)
			return null;
		PersonaNatural persona = socio.getApoderado();
		if (persona != null) {
			Hibernate.initialize(persona);
			TipoDocumento documento = persona.getTipoDocumento();
			Hibernate.initialize(documento);
		}
		return persona;
	}

	@Override
	public CuentaAporte getCuentaAporte(BigInteger idSocio) {
		Socio socio = socioDAO.find(idSocio);
		if (socio == null)
			return null;
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		if (cuentaAporte != null) {
			Hibernate.initialize(cuentaAporte);
			Moneda moneda = cuentaAporte.getMoneda();
			Hibernate.initialize(moneda);
		}
		return cuentaAporte;
	}

	@Override
	public List<CuentaBancariaView> getCuentasBancarias(BigInteger idSocio) {
		QueryParameter queryParameter = QueryParameter.with("idSocio", idSocio);
		List<CuentaBancariaView> list = cuentaBancariaViewDAO.findByNamedQuery(CuentaBancariaView.findByIdSocio, queryParameter.parameters());
		return list;
	}

	@Override
	public List<HistorialAportesSP> getHistorialAportes(BigInteger idSocio, Date desde, Date hasta, BigInteger offset, BigInteger limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Beneficiario> getBeneficiarios(BigInteger idSocio) {
		Socio socio = socioDAO.find(idSocio);
		if (socio == null)
			return null;
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		Set<Beneficiario> result = cuentaAporte.getBeneficiarios();
		Hibernate.initialize(result);
		return result;
	}

	@Override
	public Beneficiario findBeneficiarioById(BigInteger id) {
		return beneficiarioDAO.find(id);
	}

}
