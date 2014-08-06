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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.SessionREST;
import org.sistemafinanciero.service.nt.SessionServiceNT;
import org.sistemafinanciero.service.ts.SessionServiceTS;

public class SessionRESTService implements SessionREST {

	@EJB
	private SessionServiceNT sessionServiceNT;

	@EJB
	private SessionServiceTS sessionServiceTS;

	@Override
	public Response getCajaOfSession() {
		Caja caja = sessionServiceNT.getCajaOfSession();
		Response response = Response.status(Response.Status.OK).entity(caja).build();
		return response;
	}

	@Override
	public Response getAgenciaOfSession() {
		Agencia agencia = sessionServiceNT.getAgenciaOfSession();
		Response response = Response.status(Response.Status.OK).entity(agencia).build();
		return response;
	}

	@Override
	public Response getUsuario(SecurityContext context) {
		KeycloakPrincipal p = (KeycloakPrincipal) context.getUserPrincipal();
		KeycloakSecurityContext kcSecurityContext = p.getKeycloakSecurityContext();
		String username = kcSecurityContext.getToken().getPreferredUsername();
		Response response = Response.status(Response.Status.OK).entity(username).build();
		return response;
	}

	@Override
	public Response getPersonaOfSession() {
		PersonaNatural personaNatural = sessionServiceNT.getPersonaOfSession();
		Response response = Response.status(Response.Status.OK).entity(personaNatural).build();
		return response;
	}

	@Override
	public Response crearPendiente(BigInteger idboveda, BigDecimal monto, String observacion) {
		try {
			sessionServiceTS.crearPendiente(idboveda, monto, observacion);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response crearTransaccionCuentaAporte() {
		try {
			sessionServiceTS.crearAporte(null, null, 1, 10, null);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response crearTransaccionCompraVenta() {
		try {
			sessionServiceTS.crearTransaccionCompraVenta(null, null, null, null, null, null, null);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response crearTransaccionBancaria() {
		try {
			sessionServiceTS.crearTransaccionBancaria(null, null, null);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response crearTransferencia() {
		try {
			sessionServiceTS.crearTransferenciaBancaria(null, null, null, null);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response cancelarCuentaBancariaConRetiro(BigInteger id) {
		try {
			sessionServiceTS.cancelarCuentaBancariaConRetiro(id);
			return Response.status(Response.Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response createTransaccionCajaCaja() {
		try {
			sessionServiceTS.crearTransaccionCajaCaja(null, null, null, null);
			return Response.status(Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response confirmarTransaccionCajaCaja(BigInteger id) {
		try {
			sessionServiceTS.confirmarTransaccionCajaCaja(id);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response cancelarTransaccionCajaCaja(BigInteger id) {
		try {
			sessionServiceTS.cancelarTransaccionCajaCaja(id);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response createTransaccionBovedaCaja(Set<GenericDetalle> detalleTransaccion, BigInteger idboveda) {
		try {
			sessionServiceTS.crearTransaccionBovedaCaja(null, null);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response confirmarTransaccionBovedaCaja(BigInteger id) {
		try {
			sessionServiceTS.confirmarTransaccionBovedaCaja(id);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response cancelarTransaccionBovedaCaja(BigInteger id) {
		try {
			sessionServiceTS.cancelarTransaccionBovedaCaja(id);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
