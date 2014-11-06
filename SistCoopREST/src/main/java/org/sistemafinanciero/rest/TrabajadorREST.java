package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.rest.dto.TrabajadorDTO;

@Path("/trabajadores")
public interface TrabajadorREST {

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response listAll(@QueryParam("filterText") String filterText, @QueryParam("idAgencia") BigInteger idAgencia);

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findById(@PathParam("id") BigInteger id);

	@POST
	@Produces({ "application/xml", "application/json" })
	public Response create(TrabajadorDTO trabajadorDTO);

	@PUT
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response update(@PathParam("id") BigInteger id, TrabajadorDTO trabajadorDTO);
	
	@POST
	@Path("/{id}/desactivar")
	@Produces({ "application/xml", "application/json" })
	public Response desactivar(@PathParam("id") BigInteger id);

}
