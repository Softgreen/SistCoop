package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/agencias")
public interface AgenciaREST {

	@GET
	@Path("/{id}/bovedas")
	@Produces({ "application/json" })
	public Response getBovedasOfAgencia(@PathParam("id") BigInteger id);
	
	@GET
	@Path("/{id}/cajas")
	@Produces({ "application/json" })
	public Response getCajasOfAgencia(@PathParam("id") BigInteger id);

}
