/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sistemafinanciero.rest.impl;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.sistemafinanciero.rest.UsuarioREST;
import org.sistemafinanciero.service.nt.UsuarioServiceNT;

public class UsuarioRESTService implements UsuarioREST {

	@EJB
	private UsuarioServiceNT usuarioServiceNT;

	@Override
	public Response authenticateAsAdministrator(SecurityContext context, String username, String password) {
		boolean result = usuarioServiceNT.authenticateAsAdministrator(username, password);
		if (result == true)
			return Response.status(Response.Status.NO_CONTENT).build();
		else
			return Response.status(Response.Status.UNAUTHORIZED).build();
	}

}
