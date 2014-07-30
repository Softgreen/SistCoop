package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.rest.AgenciaREST;
import org.sistemafinanciero.service.nt.AgenciaServiceNT;

public class AgenciaRESTService implements AgenciaREST {

	@EJB
	private AgenciaServiceNT agenciaServiceNT;

	@Override
	public Response getCajasOfAgencia(BigInteger id) {
		Set<Caja> cajas = agenciaServiceNT.getCajas(id);
		Response response = Response.status(Response.Status.OK).entity(cajas)
				.build();
		return response;
	}
}
