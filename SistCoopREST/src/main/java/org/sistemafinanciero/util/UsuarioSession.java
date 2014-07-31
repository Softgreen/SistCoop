package org.sistemafinanciero.util;

import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.SessionContext;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

public class UsuarioSession {

	@Resource
	private SessionContext context;

	public boolean haveSession() {
		Principal principal = context.getCallerPrincipal();
		String name = principal.getName();
		return name == null;
	}

	public String getUsername() {
		KeycloakPrincipal p = (KeycloakPrincipal) context.getCallerPrincipal();
		KeycloakSecurityContext kcSecurityContext = p.getKeycloakSecurityContext();
		String username = kcSecurityContext.getToken().getPreferredUsername();
		return username;
	}

}
