package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.rest.MonedaREST;
import org.sistemafinanciero.service.nt.MonedaServiceNT;

public class MonedaRESTService implements MonedaREST {

	@EJB
	private MonedaServiceNT monedaServiceNT;
	
	@Override
	public Response findAll() {
		List<Moneda> list = monedaServiceNT.findAll();
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getDenominaciones(BigInteger idMoneda) {
		List<MonedaDenominacion> list = monedaServiceNT.getDenominaciones(idMoneda);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	
}
