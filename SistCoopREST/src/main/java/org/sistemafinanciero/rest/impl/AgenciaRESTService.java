package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.AgenciaREST;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.service.nt.AgenciaServiceNT;
import org.sistemafinanciero.service.ts.AgenciaServiceTS;

public class AgenciaRESTService implements AgenciaREST {

	@EJB
	private AgenciaServiceNT agenciaServiceNT;

	@EJB
	private AgenciaServiceTS agenciaServiceTS;
	
	@Override
	public Response findAll() {
		List<Agencia> list = agenciaServiceNT.findAll();
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		Agencia result = agenciaServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(result).build();
		return response;
	}

	@Override
	public Response create(Agencia agencia) {
		Response response;
		try {
			BigInteger id = agenciaServiceTS.create(agencia);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(id)).build();
		} catch (PreexistingEntityException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
		return response;
	}

	@Override
	public Response update(BigInteger id, Agencia agencia) {
		Response response;
		try {
			agenciaServiceTS.update(id, agencia);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(id)).build();
		} catch (PreexistingEntityException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (NonexistentEntityException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
		return response;
	}

	@Override
	public Response getBovedasOfAgencia(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getCajasOfAgencia(BigInteger id) {
		Set<Caja> cajas = agenciaServiceNT.getCajasOfAgencia(id);
		Response response = Response.status(Response.Status.OK).entity(cajas).build();
		return response;
	}

}
