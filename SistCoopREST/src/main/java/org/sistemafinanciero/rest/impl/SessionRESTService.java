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
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.SessionREST;
import org.sistemafinanciero.rest.dto.TransaccionBancariaDTO;
import org.sistemafinanciero.rest.dto.TransaccionCompraVentaDTO;
import org.sistemafinanciero.rest.dto.TransaccionCuentaAporteDTO;
import org.sistemafinanciero.rest.dto.TransferenciaBancariaDTO;
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
	public Response abrirCajaOfSession() {
		Response response;
		try {
			BigInteger idHistorial = sessionServiceTS.abrirCaja();
			response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idHistorial)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response cerrarCajaOfSession(Set<GenericMonedaDetalle> detalle) {
		Response response;
		try {
			Map<Boveda, BigDecimal> diferencia = sessionServiceTS.getDiferenciaSaldoCaja(detalle);
			if (diferencia.size() > 0) {
				JsonArrayBuilder result = Json.createArrayBuilder();
				for (Boveda boveda : diferencia.keySet()) {
					BigDecimal dif = diferencia.get(boveda);
					JsonObject obj = Json.createObjectBuilder().add("idboveda", boveda.getIdBoveda()).add("boveda", boveda.getDenominacion()).add("monto", dif).build();
					result.add(obj);
				}
				return Response.status(Response.Status.BAD_REQUEST).entity(result.build()).build();
			}
			BigInteger idHistorialCaja = sessionServiceTS.cerrarCaja(detalle);
			JsonObject model = Json.createObjectBuilder().add("message", "Caja cerrada").add("id", idHistorialCaja).build();
			return Response.status(Response.Status.OK).entity(model).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		} catch (EJBException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
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
	public Response desactivarSocio(@PathParam("id") BigInteger id) {
		Response response;
		try {
			BigInteger idTransaccion = sessionServiceTS.cancelarSocioConRetiro(id);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response crearPendiente(BigInteger idboveda, BigDecimal monto, String observacion) {
		Response response;
		try {
			BigInteger idPendiente = sessionServiceTS.crearPendiente(idboveda, monto, observacion);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idPendiente)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response crearTransaccionCuentaAporte(TransaccionCuentaAporteDTO transaccion) {
		Response response;
		try {
			BigInteger idSocio = transaccion.getIdSocio();
			BigDecimal monto = transaccion.getMonto();
			int mes = transaccion.getMes();
			int anio = transaccion.getAnio();
			String referencia = transaccion.getReferencia();
			BigInteger idAporte = sessionServiceTS.crearAporte(idSocio, monto, mes, anio, referencia);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idAporte)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response crearTransaccionCompraVenta(TransaccionCompraVentaDTO transaccion) {
		Response response;
		try {
			Tipotransaccioncompraventa tipoTransaccion = transaccion.getTipoOperacion();
			BigInteger idMonedaRecibido = transaccion.getIdMonedaRecibida();
			BigInteger idMonedaEntregado = transaccion.getIdMonedaEntregada();
			BigDecimal montoRecibido = transaccion.getMontoRecibido();
			BigDecimal montoEntregado = transaccion.getMontoEntregado();
			BigDecimal tasaCambio = transaccion.getTasaCambio();
			String referencia = transaccion.getReferencia();
			BigInteger idTransaccion = sessionServiceTS.crearTransaccionCompraVenta(tipoTransaccion, idMonedaRecibido, idMonedaEntregado, montoRecibido, montoEntregado, tasaCambio, referencia);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response crearTransaccionBancaria(TransaccionBancariaDTO transaccion) {
		Response response;
		try {
			String numeroCuenta = transaccion.getNumeroCuenta();
			BigDecimal monto = transaccion.getMonto();
			String referencia = transaccion.getReferencia();
			BigInteger idTransaccion = sessionServiceTS.crearTransaccionBancaria(numeroCuenta, monto, referencia);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response crearTransferencia(TransferenciaBancariaDTO transferencia) {
		Response response;
		try {
			String numeroCuentaOrigen = transferencia.getNumeroCuentaOrigen();
			String numeroCuentaDestino = transferencia.getNumeroCuentaDestino();
			BigDecimal monto = transferencia.getMonto();
			String referencia = transferencia.getReferencia();

			BigInteger idTransferencia = sessionServiceTS.crearTransferenciaBancaria(numeroCuentaOrigen, numeroCuentaDestino, monto, referencia);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransferencia)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response cancelarCuentaBancariaConRetiro(BigInteger id) {
		Response response;
		try {
			BigInteger idTransaccion = sessionServiceTS.cancelarCuentaBancariaConRetiro(id);
			response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idTransaccion)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		}
		return response;
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
