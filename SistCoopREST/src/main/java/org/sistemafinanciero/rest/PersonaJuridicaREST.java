package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.rest.dto.PersonaJuridicaDTO;

@Path("/personas/juridicas")
public interface PersonaJuridicaREST {

	@GET
	@Path("/tipoDocumentos")
	@Produces({ "application/xml", "application/json" })
	public Response getTipoDocumentoPersonaJuridica();

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response findAll(@QueryParam("filterText") String filterText, @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

	@GET
	@Path("/buscar")
	@Produces({ "application/xml", "application/json" })
	public Response findByTipoNumeroDocumento(@QueryParam("idTipoDocumento") BigInteger idtipodocumento, @QueryParam("numeroDocumento") String numerodocumento);

	@GET
	@Path("/count")
	@Produces({ "application/xml", "application/json" })
	public Response count();

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findById(@PathParam("id") BigInteger id);

	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response create(PersonaJuridicaDTO persona);

	@PUT
	@Path("/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response update(@PathParam("id") BigInteger id, PersonaJuridicaDTO persona);

}
