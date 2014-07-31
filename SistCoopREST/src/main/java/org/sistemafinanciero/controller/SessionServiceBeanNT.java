package org.sistemafinanciero.controller;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.service.nt.SessionServiceNT;

@Stateless
@Named
@Remote(SessionServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SessionServiceBeanNT implements SessionServiceNT {

	@Override
	public PersonaNatural getPersonaOfSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Caja getCajaOfSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Agencia getAgenciaOfSession() {
		// TODO Auto-generated method stub
		return null;
	}

}