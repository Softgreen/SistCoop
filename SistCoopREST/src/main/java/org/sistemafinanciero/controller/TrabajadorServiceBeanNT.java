package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

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
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Sucursal;
import org.sistemafinanciero.entity.TipoDocumento;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TrabajadorCaja;
import org.sistemafinanciero.exception.IllegalResultException;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.service.nt.TrabajadorServiceNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Stateless
@Remote(TrabajadorServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrabajadorServiceBeanNT implements TrabajadorServiceNT {

	private static Logger LOGGER = LoggerFactory.getLogger(TrabajadorServiceNT.class);

	@Inject
	private DAO<Object, Trabajador> trabajadorDAO;
	
	@Override
	public Caja findByTrabajador(BigInteger idTrabajador) throws NonexistentEntityException {
		Caja result = null;
		try {
			Trabajador trabajador = trabajadorDAO.find(idTrabajador);
			if (trabajador == null)
				throw new NonexistentEntityException("Trabajador no existente");
			Set<TrabajadorCaja> cajas = trabajador.getTrabajadorCajas();
			if (cajas.size() >= 2)
				throw new IllegalResultException("Trabajador tiene " + cajas.size() + " asignadas");
			for (TrabajadorCaja trabajadorCaja : cajas) {
				result = trabajadorCaja.getCaja();
				Hibernate.initialize(result);
				break;
			}
		} catch (IllegalResultException e) {
			LOGGER.error(e.getMessage(), e.getLocalizedMessage(), e.getCause());
		}
		return result;
	}

	@Override
	public Trabajador findByUsuario(String username) {
		QueryParameter queryParameter = QueryParameter.with("username", username);
		List<Trabajador> list = trabajadorDAO.findByNamedQuery(Trabajador.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			Trabajador trabajador = null;
			for (Trabajador t : list) {
				trabajador = t;
			}
			return trabajador;
		} else {
			System.out.println("Error: mas de un usuario registrado");
			return null;
		}
	}

	@Override
	public Agencia getAgencia(BigInteger idTrabajador) throws NonexistentEntityException {
		Agencia result = null;
		Trabajador trabajador = findById(idTrabajador);
		if (trabajador != null) {
			result = trabajador.getAgencia();
			Hibernate.initialize(result);
		} else {
			throw new NonexistentEntityException("trabajador no encontrado");
		}
		return result;
	}

	@Override
	public Trabajador findById(BigInteger id) {
		Trabajador trabajador = trabajadorDAO.find(id);
		PersonaNatural personaNatural = trabajador.getPersonaNatural();
		TipoDocumento tipoDocumento = personaNatural.getTipoDocumento();
		
		Agencia agencia = trabajador.getAgencia();
		Sucursal sucursal = agencia.getSucursal();
		
		Hibernate.initialize(personaNatural);
		Hibernate.initialize(tipoDocumento);
		Hibernate.initialize(agencia);
		Hibernate.initialize(sucursal);
		
		return trabajador;
	}

	@Override
	public List<Trabajador> findAll() {
		return trabajadorDAO.findAll();
	}

	@Override
	public int count() {
		return trabajadorDAO.count();
	}

	@Override
	public List<Trabajador> findAllByFilterTextAndAgencia(String filterText, BigInteger idAgencia) {
		if(filterText == null)
			filterText = "";
		List<Trabajador> list = null;
		if(idAgencia != null){
			QueryParameter queryParameter = QueryParameter.with("filterText", "%" + filterText + "%").and("idAgencia", idAgencia);
			list = trabajadorDAO.findByNamedQuery(Trabajador.findByFilterTextAndIdAgencia, queryParameter.parameters());
		} else {
			QueryParameter queryParameter = QueryParameter.with("filterText", "%" + filterText + "%");
			list = trabajadorDAO.findByNamedQuery(Trabajador.findByFilterText, queryParameter.parameters());
		}
		if(list != null){
			for (Trabajador trabajador : list) {
				PersonaNatural personaNatural = trabajador.getPersonaNatural();
				TipoDocumento tipoDocumento = personaNatural.getTipoDocumento();
				Agencia agencia = trabajador.getAgencia();
				Hibernate.initialize(personaNatural);
				Hibernate.initialize(tipoDocumento);
				Hibernate.initialize(agencia);
			}
		}
		return list;
	}

}
