package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Sucursal;
import org.sistemafinanciero.service.nt.SucursalServiceNT;

@Named
@Stateless
@Remote(SucursalServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SucursalServiceBeanNT implements SucursalServiceNT {

	@Inject
	private DAO<Object, Sucursal> sucursalDAO;

	@Override
	public Sucursal findById(BigInteger id) {
		return sucursalDAO.find(id);
	}

	@Override
	public List<Sucursal> findAll() {
		return sucursalDAO.findAll();
	}

	@Override
	public int count() {
		return sucursalDAO.count();
	}

	@Override
	public List<Agencia> getAgencias(BigInteger idSucursal) {
		Sucursal sucursal = sucursalDAO.find(idSucursal);
		Set<Agencia> agencias = sucursal.getAgencias();
		return new ArrayList<Agencia>(agencias);
	}

}
