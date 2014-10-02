package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Sucursal;

@Path("/sucursales")
public interface SucursalREST {

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response findAll();

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findById(@PathParam("id") BigInteger id);

	@POST
	@Produces({ "application/xml", "application/json" })
	public Response create(Sucursal sucursal);

	@PUT
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response update(@PathParam("id") BigInteger id, Sucursal sucursal);

	@POST
	@Path("/{id}/desactivar")
	@Produces({ "application/xml", "application/json" })
	public Response desactivar(@PathParam("id") BigInteger id);
	
	@GET
	@Path("/{id}/agencias")
	@Produces({ "application/json" })
	public Response getAgenciasOfSucursales(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/agencias/{idAgencia}")
	@Produces({ "application/json" })
	public Response getAgenciaById(@PathParam("id") BigInteger id, @PathParam("idAgencia") BigInteger idAgencia);

	@POST
	@Path("/{id}/agencias")
	@Produces({ "application/json" })
	public Response createAgencia(@PathParam("id") BigInteger id, Agencia agencia);

	@PUT
	@Path("/{id}/agencias/{idAgencia}")
	@Produces({ "application/json" })
	public Response updateAgencia(@PathParam("id") BigInteger id, @PathParam("idAgencia") BigInteger idAgencia, Agencia agencia);

	@POST
	@Path("/{id}/agencias/{idAgencia}/desactivar")
	@Produces({ "application/xml", "application/json" })
	public Response desactivarAgencia(@PathParam("id") BigInteger id, @PathParam("idAgencia") BigInteger idAgencia);
}
