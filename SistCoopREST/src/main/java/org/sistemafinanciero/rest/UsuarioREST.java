package org.sistemafinanciero.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/usuarios")
public interface UsuarioREST {

	@GET
	@Path("/authenticate/administrator")
	@Produces({ "application/json" })
	public Response authenticateAsAdministrator();

}
