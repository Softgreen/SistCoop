package org.sistemafinanciero.controller;

import java.math.BigInteger;
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
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.service.nt.BovedaServiceNT;

@Named
@Stateless
@Remote(BovedaServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BovedaServiceBeanNT implements BovedaServiceNT {

	@Inject
	private DAO<Object, Boveda> bovedaDAO;

	@Override
	public Boveda findById(BigInteger id) {
		return bovedaDAO.find(id);
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

}
