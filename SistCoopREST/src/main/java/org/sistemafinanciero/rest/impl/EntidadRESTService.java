package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Entidad;
import org.sistemafinanciero.rest.EntidadREST;
import org.sistemafinanciero.service.nt.EntidadServiceNT;

public class EntidadRESTService implements EntidadREST {

	@EJB
	private EntidadServiceNT entidadServiceNT;

	@Override
	public Response findAll() {
		List<Entidad> list = entidadServiceNT.findAll();
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		Entidad entidad = entidadServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(entidad).build();
		return response;
	}

}
