package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/entidades")
public interface EntidadREST {

	@GET
	@Produces({ "application/json" })
	public Response findAll();

	@GET
	@Path("/{id}")
	@Produces({ "application/json" })
	public Response findById(@PathParam("id") BigInteger id);

}
