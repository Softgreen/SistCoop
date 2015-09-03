package org.sistemafinanciero.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

	private String password = "198122";
	
	@Override
	public boolean authenticateAsAdministrator(String username, String passwordMD5) {
		try {
			String md5 = getMd5(password);
			if(md5.equals(passwordMD5)){
				return true;
			} else {
				return false;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private String getMd5(String cadena) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(cadena.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}

		return sb.toString();
	}
}
