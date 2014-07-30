package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/monedas")
public interface MonedaREST {

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response findAll();

	@GET
	@Path("{id}/denominaciones")
	@Produces({ "application/xml", "application/json" })
	public Response getDenominaciones(@PathParam("id") BigInteger idMoneda);

}
