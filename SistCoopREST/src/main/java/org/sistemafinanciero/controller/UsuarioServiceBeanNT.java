package org.sistemafinanciero.controller;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

import org.sistemafinanciero.service.nt.UsuarioServiceNT;

@Named
@Stateless
@Remote(UsuarioServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UsuarioServiceBeanNT implements UsuarioServiceNT {

	@Resource
	private SessionContext context;

	@Override
	public boolean authenticateAsAdministrator(String username, String passwordMD5) {
		return true;
	}

}
