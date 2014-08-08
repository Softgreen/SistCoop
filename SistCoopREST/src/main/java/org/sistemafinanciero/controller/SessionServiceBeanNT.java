package org.sistemafinanciero.controller;

import java.util.List;

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
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.service.nt.SessionServiceNT;
import org.sistemafinanciero.util.UsuarioSession;

@Stateless
@Named
@Remote(SessionServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SessionServiceBeanNT implements SessionServiceNT {

	@Inject
	private DAO<Object, Caja> cajaDAO;

	@Inject
	private DAO<Object, Trabajador> trabajadorDAO;

	@Inject
	private UsuarioSession usuarioSession;

	@Override
	public PersonaNatural getPersonaOfSession() {
		String username = usuarioSession.getUsername();
		QueryParameter queryParameter = QueryParameter.with("username", username);
		List<Trabajador> list = trabajadorDAO.findByNamedQuery(Trabajador.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			Trabajador trabajador = null;
			for (Trabajador t : list) {
				trabajador = t;
			}
			if (trabajador != null) {
				PersonaNatural persona = trabajador.getPersonaNatural();
				Hibernate.initialize(persona);
				return persona;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public Caja getCajaOfSession() {
		String username = usuarioSession.getUsername();
		QueryParameter queryParameter = QueryParameter.with("username", username);
		List<Caja> list = cajaDAO.findByNamedQuery(Caja.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			Caja caja = null;
			for (Caja c : list) {
				caja = c;
			}
			return caja;
		} else {
			return null;
		}
	}

	@Override
	public Agencia getAgenciaOfSession() {
		String username = usuarioSession.getUsername();
		QueryParameter queryParameter = QueryParameter.with("username", username);
		List<Trabajador> list = trabajadorDAO.findByNamedQuery(Trabajador.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			Trabajador trabajador = null;
			for (Trabajador t : list) {
				trabajador = t;
			}
			if (trabajador != null) {
				Agencia agencia = trabajador.getAgencia();
				Hibernate.initialize(agencia);
				return agencia;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}