package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;

import javax.ws.rs.core.Response;

import org.sistemafinanciero.rest.PersonaJuridicaREST;

public class PersonaJuridicaRESTService implements PersonaJuridicaREST {

	@Override
	public Response findAll(String filterText, BigInteger offset,
			BigInteger limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findByTipoNumeroDocumento(BigInteger idtipodocumento,
			String numerodocumento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response count() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findById(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response create() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response update(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

}
