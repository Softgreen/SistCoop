package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.entity.Entidad;
import org.sistemafinanciero.service.nt.EntidadServiceNT;

@Named
@Stateless
@Remote(EntidadServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EntidadBeanNT implements EntidadServiceNT {

	@Inject
	private DAO<Object, Entidad> entidadDAO;

	@Override
	public Entidad findById(BigInteger id) {
		return entidadDAO.find(id);
	}

	@Override
	public List<Entidad> findAll() {
		return entidadDAO.findAll();
	}

	@Override
	public int count() {
		return entidadDAO.count();
	}

}
