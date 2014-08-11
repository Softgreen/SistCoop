package org.sistemafinanciero.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/usuarios")
public interface UsuarioREST {

	@POST
	@Path("/authenticateAsAdministrator")
	@Produces({ "application/json" })
	public Response authenticateAsAdministrator(@FormParam("username") String username, @FormParam("password") String password);

}
