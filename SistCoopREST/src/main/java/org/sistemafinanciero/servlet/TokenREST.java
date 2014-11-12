package org.sistemafinanciero.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.keycloak.KeycloakSecurityContext;

@Path("/token")
public class TokenREST {

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response getToken(@Context HttpServletRequest context) {
		KeycloakSecurityContext session = (KeycloakSecurityContext) context.getAttribute(KeycloakSecurityContext.class.getName());
		String tokenId = session.getTokenString();
		return Response.ok(tokenId).build();
	}

}
