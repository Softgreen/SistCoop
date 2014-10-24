package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Sucursal;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.SucursalServiceTS;

@Named
@Stateless
@Remote(SucursalServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SucursalServiceBeanTS implements SucursalServiceTS {

	@Inject
	private DAO<Object, Sucursal> sucursalDAO;

	@Inject
	private DAO<Object, Agencia> agenciaDAO;

	@Inject
	private Validator validator;

	@Override
	public BigInteger create(Sucursal t) throws PreexistingEntityException, RollbackFailureException {
		Set<ConstraintViolation<Sucursal>> violations = validator.validate(t);
		if (violations.isEmpty()) {
			t.setIdSucursal(null);
			t.setEstado(true);
			t.setAgencias(null);
			sucursalDAO.create(t);
			return t.getIdSucursal();
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@Override
	public void update(BigInteger id, Sucursal t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		Sucursal sucursal = sucursalDAO.find(id);
		if (sucursal != null) {
			if (!sucursal.getEstado())
				throw new RollbackFailureException("Sucursal inactiva, no se puede editar");
			Set<ConstraintViolation<Sucursal>> violations = validator.validate(t);
			if (violations.isEmpty()) {
				t.setIdSucursal(id);
				sucursal.setDenominacion(t.getDenominacion());
				sucursal.setAbreviatura(t.getAbreviatura());
				sucursalDAO.update(sucursal);
			} else {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
		} else {
			throw new NonexistentEntityException("Sucursal no existente, UPDATE no ejecutado");
		}
	}

	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Sucursal agencia = sucursalDAO.find(id);
		if (agencia != null) {
			sucursalDAO.delete(agencia);
		} else {
			throw new NonexistentEntityException("Sucursal no existente, DELETE no ejecutado");
		}
	}

	@Override
	public void desactivar(BigInteger idSucursal) throws NonexistentEntityException, RollbackFailureException {
		Sucursal sucursal = sucursalDAO.find(idSucursal);
		if (sucursal == null)
			throw new NonexistentEntityException("Sucursal no encontrada");
		if (!sucursal.getEstado()) {
			throw new RollbackFailureException("Sucursal inactiva, no se puede desactivar nuevamente");
		}

		Set<Agencia> agencias = sucursal.getAgencias();
		for (Agencia agencia : agencias) {
			if (agencia.getEstado())
				throw new RollbackFailureException("Agencia:" + agencia.getDenominacion() + " activa, desactive todas las agencias");
		}

		sucursal.setEstado(false);
		sucursalDAO.update(sucursal);
	}

	@Override
	public BigInteger createAgencia(BigInteger idSucursal, Agencia agencia) throws NonexistentEntityException, RollbackFailureException {
		Sucursal sucursal = sucursalDAO.find(idSucursal);
		if (sucursal == null)
			throw new NonexistentEntityException("Sucursal no encontrada");
		if (!sucursal.getEstado()) {
			throw new RollbackFailureException("Sucursal inactiva, no se puede hacer modificaciones");
		}

		QueryParameter queryParameter = QueryParameter.with("codigo", agencia.getCodigo());
		List<Agencia> list = agenciaDAO.findByNamedQuery(Agencia.findByCodigo, queryParameter.parameters());
		if (list.size() != 0) {
			throw new RollbackFailureException("Codigo de agencia ya registrado, no se puede crear");						
		}			

		agencia.setIdAgencia(null);
		agencia.setBovedas(null);
		agencia.setTrabajadores(null);

		agencia.setEstado(true);
		agencia.setSucursal(sucursal);
		agenciaDAO.create(agencia);
		return agencia.getIdAgencia();
	}

	@Override
	public void updateAgencia(BigInteger idSucursal, BigInteger idAgencia, Agencia agencia) throws NonexistentEntityException, RollbackFailureException {
		Sucursal sucursal = sucursalDAO.find(idSucursal);
		Agencia agenciaDB = agenciaDAO.find(idAgencia);
		if (sucursal == null)
			throw new NonexistentEntityException("Sucursal no encontrada");
		if (agenciaDB == null)
			throw new NonexistentEntityException("Agencia no encontrada");
		if (!sucursal.getEstado())
			throw new RollbackFailureException("Sucursal inactiva, no se puede hacer modificaciones");
		if (!agenciaDB.getEstado())
			throw new RollbackFailureException("Agencia inactiva, no se puede hacer modificaciones");
		
		if (!sucursal.equals(agenciaDB.getSucursal()))
			throw new RollbackFailureException("Agencia no pertenece a sucursal indicada");

		QueryParameter queryParameter = QueryParameter.with("codigo", agencia.getCodigo());
		List<Agencia> list = agenciaDAO.findByNamedQuery(Agencia.findByCodigo, queryParameter.parameters());
		if (list.size() != 0){
			for (Agencia ag : list) {
				if(!ag.getIdAgencia().equals(agencia.getIdAgencia()))
					throw new RollbackFailureException("Codigo de agencia ya registrado, no se puede actualizar");
			}
		}			

		agenciaDB.setAbreviatura(agencia.getAbreviatura());
		agenciaDB.setDenominacion(agencia.getDenominacion());
		agenciaDB.setCodigo(agencia.getCodigo());
		agenciaDB.setUbigeo(agencia.getUbigeo());

		agenciaDAO.update(agenciaDB);
	}

	@Override
	public void desactivarAgencia(BigInteger idSucursal, BigInteger idAgencia) throws NonexistentEntityException, RollbackFailureException {
		Sucursal sucursal = sucursalDAO.find(idSucursal);
		Agencia agenciaDB = agenciaDAO.find(idAgencia);
		if (sucursal == null)
			throw new NonexistentEntityException("Sucursal no encontrada");
		if (agenciaDB == null)
			throw new NonexistentEntityException("Agencia no encontrada");
		if (!sucursal.getEstado())
			throw new RollbackFailureException("Sucursal inactiva, no se puede hacer modificaciones");
		if (!agenciaDB.getEstado())
			throw new RollbackFailureException("Agencia inactiva, no se puede desactivar nuevamente");
		
		if (!sucursal.equals(agenciaDB.getSucursal()))
			throw new RollbackFailureException("Agencia no pertenece a sucursal indicada");

		agenciaDB.setEstado(false);		
		agenciaDAO.update(agenciaDB);
	}

}
