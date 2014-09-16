package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
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
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.PersonaJuridica;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Socio;
import org.sistemafinanciero.entity.SocioView;
import org.sistemafinanciero.entity.type.EstadoCuentaAporte;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.nt.PersonaJuridicaServiceNT;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.nt.SocioServiceNT;
import org.sistemafinanciero.service.ts.SocioServiceTS;
import org.sistemafinanciero.util.ProduceObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Stateless
@Remote(SocioServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SocioServiceBeanTS implements SocioServiceTS {

	private static Logger LOGGER = LoggerFactory.getLogger(SocioServiceBeanTS.class);

	@Inject
	private DAO<Object, Agencia> agenciaDAO;

	@Inject
	private DAO<Object, Socio> socioDAO;

	@Inject
	private DAO<Object, CuentaAporte> cuentaAporteDAO;

	@Inject
	private DAO<Object, Beneficiario> beneficiarioDAO;

	@Inject
	private DAO<Object, PersonaNatural> personaNaturalDAO;

	@EJB
	private SocioServiceNT socioServiceNT;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	@EJB
	private PersonaJuridicaServiceNT personaJuridicaServiceNT;

	@Inject
	private Validator validator;

	@Override
	public BigInteger create(SocioView t) throws PreexistingEntityException, RollbackFailureException {
		PersonaNatural personaNatural = null;
		PersonaJuridica personaJuridica = null;
		PersonaNatural apoderado = null;
		Agencia agencia = null;

		QueryParameter queryParameter = QueryParameter.with("codigo", t.getCodigoAgencia());
		Collection<Agencia> list = agenciaDAO.findByNamedQuery(Agencia.findByCodigo, queryParameter.parameters());
		if (list.size() <= 1) {
			for (Agencia ag : list) {
				agencia = ag;
			}
		} else {
			LOGGER.error("Mas de una agencia con el mismo codigo.");
		}
		if (agencia == null)
			throw new RollbackFailureException("Agencia no encontrada");

		if (t.getIdApoderado() != null)
			apoderado = personaNaturalDAO.find(t.getIdApoderado());
		if (t.getIdApoderado() != null && apoderado == null)
			throw new RollbackFailureException("Apoderado no encontrado");

		Calendar calendar = Calendar.getInstance();
		switch (t.getTipoPersona()) {
		case NATURAL:
			personaNatural = personaNaturalServiceNT.find(t.getIdTipoDocumento(), t.getNumeroDocumento());
			if (personaNatural == null)
				throw new RollbackFailureException("Persona para socio no encontrado");
			if (apoderado != null)
				if (personaNatural.equals(apoderado))
					throw new RollbackFailureException("Apoderado y socio deben de ser diferentes");
			break;
		case JURIDICA:
			personaJuridica = personaJuridicaServiceNT.find(t.getIdTipoDocumento(), t.getNumeroDocumento());
			if (personaJuridica == null)
				throw new RollbackFailureException("Persona para socio no encontrado");
			if (apoderado != null)
				if (personaJuridica.getRepresentanteLegal().equals(apoderado))
					throw new RollbackFailureException("Apoderado y representante legal deben de ser diferentes");
			break;
		default:
			throw new RollbackFailureException("Tipo de persona no valido");
		}

		// verificar si el socio ya existe
		SocioView socioView = socioServiceNT.find(t.getTipoPersona(), t.getIdTipoDocumento(), t.getNumeroDocumento());
		Socio socio = null;
		if (socioView != null) {
			if (socioView.getEstadoSocio()) {
				BigInteger idCuentaAporte = socioView.getIdCuentaAporte();
				if (idCuentaAporte == null) {
					CuentaAporte cuentaAporte = new CuentaAporte();
					cuentaAporte.setNumeroCuenta(agencia.getCodigo());
					cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
					cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
					cuentaAporte.setSaldo(BigDecimal.ZERO);
					cuentaAporte.setSocios(null);
					cuentaAporteDAO.create(cuentaAporte);

					String numeroCuenta = ProduceObject.getNumeroCuenta(cuentaAporte);
					cuentaAporte.setNumeroCuenta(numeroCuenta);
					cuentaAporteDAO.update(cuentaAporte);

					socio = socioDAO.find(socioView.getIdsocio());
					socio.setCuentaAporte(cuentaAporte);
					socio.setApoderado(apoderado);
					socioDAO.update(socio);
				} else {
					throw new RollbackFailureException("Socio ya existente, y tiene cuenta aportes activa");
				}
			} else {
				CuentaAporte cuentaAporte = new CuentaAporte();
				cuentaAporte.setNumeroCuenta(agencia.getCodigo());
				cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
				cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
				cuentaAporte.setSaldo(BigDecimal.ZERO);
				cuentaAporte.setSocios(null);
				cuentaAporteDAO.create(cuentaAporte);

				String numeroCuenta = ProduceObject.getNumeroCuenta(cuentaAporte);
				cuentaAporte.setNumeroCuenta(numeroCuenta);
				cuentaAporteDAO.update(cuentaAporte);

				socio = new Socio();
				socio.setPersonaNatural(personaNatural);
				socio.setPersonaJuridica(personaJuridica);
				socio.setApoderado(apoderado);
				socio.setCuentaAporte(cuentaAporte);
				socio.setEstado(true);
				socio.setFechaInicio(calendar.getTime());
				socio.setFechaFin(null);
				socioDAO.create(socio);
			}
		} else {
			CuentaAporte cuentaAporte = new CuentaAporte();
			cuentaAporte.setNumeroCuenta(agencia.getCodigo());
			cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
			cuentaAporte.setMoneda(ProduceObject.getMonedaPrincipal());
			cuentaAporte.setSaldo(BigDecimal.ZERO);
			cuentaAporte.setSocios(null);
			cuentaAporteDAO.create(cuentaAporte);

			String numeroCuenta = ProduceObject.getNumeroCuenta(cuentaAporte);
			cuentaAporte.setNumeroCuenta(numeroCuenta);
			cuentaAporteDAO.update(cuentaAporte);

			socio = new Socio();
			socio.setPersonaNatural(personaNatural);
			socio.setPersonaJuridica(personaJuridica);
			socio.setApoderado(apoderado);
			socio.setCuentaAporte(cuentaAporte);
			socio.setEstado(true);
			socio.setFechaInicio(calendar.getTime());
			socio.setFechaFin(null);
			socioDAO.create(socio);
		}

		return socio.getIdSocio();
	}

	@Override
	public void update(BigInteger id, SocioView t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		throw new RollbackFailureException("El metodo no esta implementado");
	}

	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		throw new RollbackFailureException("No se puede eliminar un socio");
	}

	/*
	 * @Override public BigInteger create(BigInteger idAgencia, TipoPersona
	 * tipoPersona, BigInteger idDocSocio, String numDocSocio, BigInteger
	 * idDocApoderado, String numDocApoderado) throws RollbackFailureException {
	 * return null; }
	 */

	@Override
	public void congelarCuentaAporte(BigInteger idSocio) throws RollbackFailureException {
		Socio socio = socioDAO.find(idSocio);
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		if (cuentaAporte == null)
			throw new RollbackFailureException("Socio no tiene cuenta de aportes");
		EstadoCuentaAporte estadoCuentaAporte = cuentaAporte.getEstadoCuenta();
		switch (estadoCuentaAporte) {
		case ACTIVO:
			cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.CONGELADO);
			cuentaAporteDAO.update(cuentaAporte);
			break;
		case CONGELADO:
			throw new RollbackFailureException("Cuenta de aportes ya esta congelada");
		case INACTIVO:
			throw new RollbackFailureException("Cuenta de aportes inactiva, no se puede congelar");
		default:
			throw new RollbackFailureException("Estado de cuenta de aportes no definida");
		}
	}

	@Override
	public void descongelarCuentaAporte(BigInteger idSocio) throws RollbackFailureException {
		Socio socio = socioDAO.find(idSocio);
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		if (cuentaAporte == null)
			throw new RollbackFailureException("Socio no tiene cuenta de aportes");
		EstadoCuentaAporte estadoCuentaAporte = cuentaAporte.getEstadoCuenta();
		switch (estadoCuentaAporte) {
		case ACTIVO:
			throw new RollbackFailureException("Cuenta de aportes activa, no se puede descongelar");
		case CONGELADO:
			cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.ACTIVO);
			cuentaAporteDAO.update(cuentaAporte);
			break;
		case INACTIVO:
			throw new RollbackFailureException("Cuenta de aportes inactiva, no se puede descongelar");
		default:
			throw new RollbackFailureException("Estado de cuenta de aportes no definida");
		}
	}

	@Override
	public void inactivarSocio(BigInteger idSocio) throws RollbackFailureException {
		Socio socio = socioDAO.find(idSocio);
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		Set<CuentaBancaria> cuentasBancarias = socio.getCuentaBancarias();
		if (!socio.getEstado()) {
			throw new RollbackFailureException("Socio ya esta inactivo, no se puede inactivar nuevamente");
		}
		for (CuentaBancaria cuentaBancaria : cuentasBancarias) {
			EstadoCuentaBancaria estadoCuentaBancaria = cuentaBancaria.getEstado();
			if (!estadoCuentaBancaria.equals(EstadoCuentaBancaria.INACTIVO))
				throw new RollbackFailureException("Socio tiene cuentas bancarias activas, primero inactive cuentas bancarias");
		}
		BigDecimal saldoCuentaAporte = cuentaAporte.getSaldo();
		if (saldoCuentaAporte.compareTo(BigDecimal.ZERO) != 0) {
			throw new RollbackFailureException("Cuenta de aportes tiene saldo, retire el dinero antes de desactivar");
		}

		// inactivar socio
		socio.setEstado(false);
		socio.setFechaFin(Calendar.getInstance().getTime());
		socioDAO.update(socio);

		cuentaAporte.setEstadoCuenta(EstadoCuentaAporte.INACTIVO);
		cuentaAporteDAO.update(cuentaAporte);
	}

	@Override
	public void cambiarApoderado(BigInteger idSocio, BigInteger idPersonaNatural) throws RollbackFailureException {
		Socio socio = socioDAO.find(idSocio);
		if (socio == null)
			throw new RollbackFailureException("Socio no encontrado");
		PersonaNatural apoderado = personaNaturalDAO.find(idPersonaNatural);
		if (apoderado == null)
			throw new RollbackFailureException("Apoderado no encontrado");
		PersonaNatural personaNaturalSocio = socio.getPersonaNatural();
		PersonaJuridica personaJuridicaSocio = socio.getPersonaJuridica();
		if (personaNaturalSocio != null) {
			if (apoderado.equals(personaNaturalSocio))
				throw new RollbackFailureException("El apoderado no puede ser el titular de la cuenta");
		}
		if (personaJuridicaSocio != null) {
			if (apoderado.equals(personaJuridicaSocio.getRepresentanteLegal()))
				throw new RollbackFailureException("El apoderado no puede ser el titular de la cuenta");
		}
		socio.setApoderado(apoderado);
		socioDAO.update(socio);
	}

	@Override
	public void deleteApoderado(BigInteger idSocio) throws RollbackFailureException {
		Socio socio = socioDAO.find(idSocio);
		if (socio == null)
			throw new RollbackFailureException("Socio no encontrado");
		socio.setApoderado(null);
		socioDAO.update(socio);
	}

	@Override
	public BigInteger addBeneficiario(BigInteger idSocio, Beneficiario beneficiario) throws RollbackFailureException {
		Socio socio = socioDAO.find(idSocio);
		if (socio == null)
			throw new RollbackFailureException("Socio no encotrado");
		if (!socio.getEstado())
			throw new RollbackFailureException("Socio INACTIVO, no se puede modificar beneficiarios");

		CuentaAporte cuentaAporte = socio.getCuentaAporte();

		beneficiario.setIdBeneficiario(null);
		beneficiario.setCuentaAporte(cuentaAporte);

		// validar beneficiario
		Set<ConstraintViolation<Beneficiario>> violations = validator.validate(beneficiario);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}

		beneficiarioDAO.create(beneficiario);
		return beneficiario.getIdBeneficiario();
	}

	@Override
	public void deleteBeneficiario(BigInteger idBeneficiario) throws NonexistentEntityException, RollbackFailureException {
		Beneficiario beneficiario = beneficiarioDAO.find(idBeneficiario);
		if (beneficiario == null)
			throw new NonexistentEntityException("Beneficiario no encontrado");
		CuentaAporte cuentaAporte = beneficiario.getCuentaAporte();

		if (cuentaAporte.getEstadoCuenta().equals(EstadoCuentaAporte.INACTIVO))
			throw new RollbackFailureException("Cuenta INACTIVA, no se puede modificar los beneficiarios");

		Set<Socio> socios = cuentaAporte.getSocios();
		if (socios.size() != 1)
			throw new RollbackFailureException("beneficiario asociado a mas de un Socio o a ninguno");
		
		Socio socio = null;
		for (Socio s : socios) {
			socio = s;
		}
		
		if(!socio.getEstado())
			throw new RollbackFailureException("Socio inactivo, no se puede modificar beneficiarios");

		beneficiarioDAO.delete(beneficiario);
	}

	@Override
	public void updateBeneficiario(BigInteger idBeneficiario,
			Beneficiario beneficiario) throws NonexistentEntityException,
			PreexistingEntityException, RollbackFailureException {
		
		Beneficiario beneficiarioDB = beneficiarioDAO.find(idBeneficiario);
		if (beneficiarioDB == null)
			throw new NonexistentEntityException("Beneficiario no encontrado");
		
		CuentaAporte cuentaAporte = beneficiarioDB.getCuentaAporte();
		if (cuentaAporte.getEstadoCuenta().equals(EstadoCuentaAporte.INACTIVO))
			throw new NonexistentEntityException("Cuenta INACTIVA, no se puede modificar los beneficiarios");
		
		if (cuentaAporte.getEstadoCuenta() == null)
			throw new NonexistentEntityException("Error modificar los beneficiarios");
		
		beneficiario.setIdBeneficiario(idBeneficiario);
		beneficiario.setCuentaAporte(cuentaAporte);
		beneficiarioDAO.update(beneficiario);
		
	}

}
