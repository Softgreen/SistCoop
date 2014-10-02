package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Sucursal;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.SucursalREST;
import org.sistemafinanciero.service.nt.SucursalServiceNT;
import org.sistemafinanciero.service.ts.SucursalServiceTS;

public class SucursalRESTService implements SucursalREST {

	@EJB
	private SucursalServiceNT sucursalServiceNT;

	@EJB
	private SucursalServiceTS sucursalServiceTS;

	@Override
	public Response findAll() {
		List<Sucursal> list = sucursalServiceNT.findAll();
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		Sucursal sucursal = sucursalServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(sucursal).build();
		return response;
	}

	@Override
	public Response create(Sucursal sucursal) {
		Response response;
		try {
			BigInteger id = sucursalServiceTS.create(sucursal);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(id)).build();
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response update(BigInteger id, Sucursal sucursal) {
		Response response;
		try {
			sucursalServiceTS.update(id, sucursal);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response desactivar(BigInteger id) {
		Response response;
		try {
			sucursalServiceTS.desactivar(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}
	
	@Override
	public Response getAgenciasOfSucursales(BigInteger id) {
		List<Agencia> list = sucursalServiceNT.getAgencias(id);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getAgenciaById(BigInteger id, BigInteger idAgencia) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createAgencia(BigInteger id, Agencia agencia) {
		Response response;
		try {
			BigInteger idAgencia = sucursalServiceTS.createAgencia(id, agencia);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idAgencia)).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response updateAgencia(BigInteger id, BigInteger idAgencia, Agencia agencia) {
		Response response;
		try {
			sucursalServiceTS.updateAgencia(id, idAgencia, agencia);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response desactivarAgencia(BigInteger id, BigInteger idAgencia) {
		Response response;
		try {
			sucursalServiceTS.desactivarAgencia(id, idAgencia);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	

}
