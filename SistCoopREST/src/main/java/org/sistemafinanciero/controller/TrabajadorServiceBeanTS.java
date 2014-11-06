package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
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
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.TipoDocumento;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TrabajadorCaja;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.ts.TrabajadorServiceTS;

@Named
@Stateless
@Remote(TrabajadorServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrabajadorServiceBeanTS implements TrabajadorServiceTS {

	@Inject
	private DAO<Object, Trabajador> trabajadorDAO;

	@Inject
	private DAO<Object, TrabajadorCaja> trabajadorCajaDAO;
	
	@Inject
	private DAO<Object, Agencia> agenciaDAO;

	@Inject
	private Validator validator;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	// validar dos trabajadores no existen con estado activo, tampoco con dos
	// usuarios
	@Override
	public BigInteger create(Trabajador t) throws PreexistingEntityException, RollbackFailureException {
		Agencia agencia = agenciaDAO.find(t.getAgencia().getIdAgencia());
		t.setAgencia(agencia);

		// buscar persona natural
		TipoDocumento tipoDocumento = t.getPersonaNatural().getTipoDocumento();
		String numeroDocumento = t.getPersonaNatural().getNumeroDocumento();
		PersonaNatural personaNatural = personaNaturalServiceNT.find(tipoDocumento.getIdTipoDocumento(), numeroDocumento);
		if (personaNatural == null)
			throw new RollbackFailureException("Persona natural no encontrada para el trabajador");

		// verificar que solo hay un trabajador por persona activo
		QueryParameter queryParameter1 = QueryParameter.with("idPersonaNatural", personaNatural.getIdPersonaNatural()).and("estado", true);
		List<Trabajador> list1 = trabajadorDAO.findByNamedQuery(Trabajador.findByIdPersonaAndEstado, queryParameter1.parameters());
		if (list1.size() > 0)
			throw new RollbackFailureException("El trabajador ya existe, no puede registrarlo nuevamente");

		// verificar que solo haya un usuario por trabajador
		QueryParameter queryParameter2 = QueryParameter.with("usuario", t.getUsuario()).and("estado", true);
		List<Trabajador> list2 = trabajadorDAO.findByNamedQuery(Trabajador.findByUsuarioAndEstado, queryParameter2.parameters());
		if (list2.size() > 0)
			throw new RollbackFailureException("El usuario para el trabajador ya fue asignado");

		// creando trabajador
		Set<ConstraintViolation<Trabajador>> violations = validator.validate(t);
		if (violations.isEmpty()) {
			trabajadorDAO.create(t);
			return t.getIdTrabajador();
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	@Override
	public void update(BigInteger id, Trabajador t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		Trabajador trabajadorDB = trabajadorDAO.find(id);
		if (trabajadorDB == null)
			throw new NonexistentEntityException("Trabajador no existente, no puede ser editado");		
		
		//buscar agencia
		Agencia agencia = agenciaDAO.find(t.getAgencia().getIdAgencia());
		t.setAgencia(agencia);
				
		// verificar que solo haya un usuario por trabajador
		QueryParameter queryParameter2 = QueryParameter.with("usuario", t.getUsuario()).and("estado", true);
		List<Trabajador> list2 = trabajadorDAO.findByNamedQuery(Trabajador.findByUsuarioAndEstado, queryParameter2.parameters());
		boolean ban = false;
		for (Trabajador trabajador : list2) {
			if(trabajador.equals(trabajadorDB)){
				ban = true;
			}
		}
		if(list2.size() > 0 && ban == false)
			throw new RollbackFailureException("El usuario ya fue asignado a otro trabajador");

		// actualizando trabajador
		trabajadorDB.setAgencia(agencia);
		trabajadorDB.setUsuario(t.getUsuario());
		
		Set<ConstraintViolation<Trabajador>> violations = validator.validate(t);
		if (violations.isEmpty()) {
			trabajadorDAO.update(trabajadorDB);			
		} else {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}				
	}

	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Trabajador trabajador = trabajadorDAO.find(id);
		if (trabajador != null) {
			trabajadorDAO.delete(trabajador);
		} else {
			throw new NonexistentEntityException("Trabajador no existente, DELETE no ejecutado");
		}
	}

	@Override
	public void desactivar(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		Trabajador trabajador = trabajadorDAO.find(id);
		if(trabajador == null)
			throw new NonexistentEntityException("Trabajador no encontrado");
		
		trabajador.setEstado(false);
		trabajadorDAO.update(trabajador);
		
		Set<TrabajadorCaja> cajas = trabajador.getTrabajadorCajas();
		for (TrabajadorCaja trabajadorCaja : cajas) {
			trabajadorCajaDAO.delete(trabajadorCaja);
		}
				
	}

}
