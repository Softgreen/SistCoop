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
	private SessionServiceNT cajaSessionServiceNT;

	@EJB
	private SessionServiceTS cajaSessionServiceTS;

	@Override
	public Response getCajaOfSession() {
		Caja caja = cajaSessionServiceNT.getCajaOfSession();
		Response response = Response.status(Response.Status.OK).entity(caja)
				.build();
		return response;
	}

	@Override
	public Response getAgenciaOfSession() {
		Agencia agencia = cajaSessionServiceNT.getAgenciaOfSession();
		Response response = Response.status(Response.Status.OK).entity(agencia)
				.build();
		return response;
	}

	@Override
	public Response getUsuario(SecurityContext context) {
		return null;
	}

	@Override
	public Response getPersonaOfSession() {
		PersonaNatural personaNatural = cajaSessionServiceNT
				.getPersonaOfSession();
		Response response = Response.status(Response.Status.OK)
				.entity(personaNatural).build();
		return response;
	}

	@Override
	public Response crearPendiente(BigInteger idboveda, BigDecimal monto,
			String observacion) {
		try {
			cajaSessionServiceTS.crearPendiente(idboveda, monto, observacion);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response crearTransaccionCuentaAporte() {
		try {
			cajaSessionServiceTS.crearAporte(null, null, 1, 10, null);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response crearTransaccionCompraVenta() {
		try {
			cajaSessionServiceTS.crearTransaccionCompraVenta(null, null, null, null, null,
					null, null);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response crearTransaccionBancaria() {
		try {
			cajaSessionServiceTS.crearTransaccionBancaria(null, null, null);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response crearTransferencia() {
		try {
			cajaSessionServiceTS.crearTransferenciaBancaria(null, null, null,
					null);
			return Response.status(Response.Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response cancelarCuentaBancariaConRetiro(BigInteger id) {
		try {
			cajaSessionServiceTS.cancelarCuentaBancariaConRetiro(id);
			return Response.status(Response.Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response createTransaccionCajaCaja() {
		try {
			cajaSessionServiceTS.crearTransaccionCajaCaja(null, null, null,
					null);
			return Response.status(Status.CREATED).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response confirmarTransaccionCajaCaja(BigInteger id) {
		try {
			cajaSessionServiceTS.confirmarTransaccionCajaCaja(id);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response cancelarTransaccionCajaCaja(BigInteger id) {
		try {
			cajaSessionServiceTS.cancelarTransaccionCajaCaja(id);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response createTransaccionBovedaCaja(
			Set<GenericDetalle> detalleTransaccion, BigInteger idboveda) {
		try {
			cajaSessionServiceTS.crearTransaccionBovedaCaja(null, null);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response confirmarTransaccionBovedaCaja(BigInteger id) {
		try {
			cajaSessionServiceTS.confirmarTransaccionBovedaCaja(id);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@Override
	public Response cancelarTransaccionBovedaCaja(BigInteger id) {
		try {
			cajaSessionServiceTS.cancelarTransaccionBovedaCaja(id);
			return Response.status(Status.OK).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

}
