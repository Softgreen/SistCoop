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
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Caja;
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
	public Trabajador findByUsuario(BigInteger idusuario) {
		return null;
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
		return trabajadorDAO.find(id);
	}

	@Override
	public List<Trabajador> findAll() {
		return trabajadorDAO.findAll();
	}

	@Override
	public int count() {
		return trabajadorDAO.count();
	}

}
