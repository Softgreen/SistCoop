package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.PersonaJuridica;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Socio;
import org.sistemafinanciero.entity.type.EstadoCuentaAporte;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.SocioServiceTS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@Stateless
@Remote(SocioServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SocioServiceBeanTS implements SocioServiceTS {

	private static Logger LOGGER = LoggerFactory.getLogger(SocioServiceBeanTS.class);

	@Inject
	private DAO<Object, Socio> socioDAO;

	@Inject
	private DAO<Object, CuentaAporte> cuentaAporteDAO;

	@Inject
	private DAO<Object, PersonaNatural> personaNaturalDAO;

	@Override
	public BigInteger create(Socio t) throws PreexistingEntityException, RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(BigInteger id, Socio t) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public BigInteger create(BigInteger idAgencia, TipoPersona tipoPersona, BigInteger idDocSocio, String numDocSocio, BigInteger idDocApoderado, String numDocApoderado) throws RollbackFailureException {
		return null;
	}

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

}
