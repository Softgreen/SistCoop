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

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.TransaccionBovedaBovedaView;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.TransaccionBovedaOtroView;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.VoucherCerrarBoveda;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBovedaBoveda;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBovedaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionEntidadBoveda;
import org.sistemafinanciero.entity.type.TransaccionEntidadBovedaOrigen;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.BovedaREST;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.service.nt.BovedaServiceNT;
import org.sistemafinanciero.service.nt.SessionServiceNT;
import org.sistemafinanciero.service.nt.TransaccionInternaServiceNT;
import org.sistemafinanciero.service.ts.BovedaServiceTS;

public class BovedaRESTService implements BovedaREST {

	@EJB
	private SessionServiceNT sessionServiceNT;

	@EJB
	private BovedaServiceNT bovedaServiceNT;

	@EJB
	private BovedaServiceTS bovedaServiceTS;

	@EJB
	private TransaccionInternaServiceNT transaccionInternaServiceNT;

	@Override
	public Response findById(BigInteger id) {
		Boveda boveda = bovedaServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(boveda).build();
		return response;
	}

	@Override
	public Response listAll(BigInteger idAgencia) {
		List<Boveda> list;
		if (idAgencia == null)
			list = bovedaServiceNT.findAll();
		else
			list = bovedaServiceNT.findAll(idAgencia);

		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response update(BigInteger id, String denominacion) {
		Response response;

		Boveda boveda = new Boveda();
		boveda.setDenominacion(denominacion);
		try {
			bovedaServiceTS.update(id, boveda);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response create(SecurityContext context, BigInteger idMoneda, String denominacion) {
		Response response;
		try {
			Agencia agencia = sessionServiceNT.getAgenciaOfSession();
			Moneda moneda = new Moneda();
			moneda.setIdMoneda(idMoneda);

			Boveda boveda = new Boveda();
			boveda.setIdBoveda(null);
			boveda.setDenominacion(denominacion);
			boveda.setMoneda(moneda);
			boveda.setAgencia(agencia);
			boveda.setAbierto(false);
			boveda.setEstadoMovimiento(false);
			boveda.setEstado(true);

			bovedaServiceTS.create(boveda);

			response = Response.status(Response.Status.CREATED).build();
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response delete(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getDetalleBoveda(BigInteger id, BigInteger idHistorial) {
		Set<GenericDetalle> detalle;
		if (idHistorial == null)
			detalle = bovedaServiceNT.getDetalle(id);
		else
			detalle = bovedaServiceNT.getDetalle(id, idHistorial);
		Response response = Response.status(Response.Status.OK).entity(detalle).build();
		return response;
	}

	@Override
	public Response getDetalleBovedaPenultimo(BigInteger id) {
		Set<GenericDetalle> detalle;
		detalle = bovedaServiceNT.getDetallePenultimo(id);
		Response response = Response.status(Response.Status.OK).entity(detalle).build();
		return response;
	}

	@Override
	public Response abrir(BigInteger id) {
		Response response;
		try {
			BigInteger idHistorial = bovedaServiceTS.abrir(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response congelar(BigInteger id) {
		Response response;
		try {
			bovedaServiceTS.congelar(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response descongelar(BigInteger id) {
		Response response;
		try {
			bovedaServiceTS.descongelar(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response cerrar(BigInteger id) {
		Response response;
		try {
			BigInteger idHistorial = bovedaServiceTS.cerrar(id);
			response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idHistorial)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getVoucherTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) {
		VoucherTransaccionBovedaCaja voucher = transaccionInternaServiceNT.getVoucherTransaccionBovedaCaja(idTransaccionBovedaCaja);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getVoucherTransaccionEntidadBoveda(BigInteger idTransaccionEntidadBoveda) {
		VoucherTransaccionEntidadBoveda voucher = transaccionInternaServiceNT.getVoucherTransaccionEntidadBoveda(idTransaccionEntidadBoveda);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getDetalleTransaccionEntidadBoveda(BigInteger idTransaccionEntidadBoveda) {
		TreeSet<GenericDetalle> detalleTransaccion = transaccionInternaServiceNT.getDetalleTransaccionEntidadBoveda(idTransaccionEntidadBoveda);
		return Response.status(Response.Status.OK).entity(detalleTransaccion).build();
	}

	@Override
	public Response getVoucherTransaccionBovedaBoveda(BigInteger idTransaccionBovedaBoveda) {
		VoucherTransaccionBovedaBoveda voucher = transaccionInternaServiceNT.getVoucherTransaccionBovedaBoveda(idTransaccionBovedaBoveda);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getDetalleTransaccionBovedaBoveda(BigInteger idTransaccionBovedaBoveda) {
		TreeSet<GenericDetalle> detalleTransaccion = transaccionInternaServiceNT.getDetalleTransaccionBovedaBoveda(idTransaccionBovedaBoveda);
		return Response.status(Response.Status.OK).entity(detalleTransaccion).build();
	}
	
	@Override
	public Response getVoucherCerrarBoveda(BigInteger idHistorial) {
		VoucherCerrarBoveda voucher = transaccionInternaServiceNT.getVoucherCerrarBoveda(idHistorial);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}
	
	@Override
	public Response getDetalleTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) {
		TreeSet<GenericDetalle> detalleTransaccion = transaccionInternaServiceNT.getDetalleTransaccionBovedaCaja(idTransaccionBovedaCaja);
		return Response.status(Response.Status.OK).entity(detalleTransaccion).build();
	}

	@Override
	public Response getTransaccionesBovedaCajaOfBovedaEnviados(BigInteger idAgencia) {
		List<TransaccionBovedaCajaView> list = null;
		if (idAgencia != null)
			list = bovedaServiceNT.getTransaccionesEnviadasBovedaCaja(idAgencia);

		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getTransaccionesBovedaCajaOfBovedaRecibidos(BigInteger idAgencia) {
		List<TransaccionBovedaCajaView> list = null;
		if (idAgencia != null)
			list = bovedaServiceNT.getTransaccionesRecibidasBovedaCaja(idAgencia);

		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response createTransaccionEntidadBoveda(TransaccionEntidadBovedaOrigen origen, Set<GenericDetalle> detalleTransaccion, BigInteger idEntidad, BigInteger idBoveda, String observacion) {
		Response response;
		try {
			BigInteger idTransaccion = bovedaServiceTS.crearTransaccionEntidadBoveda(origen, detalleTransaccion, idEntidad, idBoveda, observacion);
			response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idTransaccion)).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response createTransaccionBovedaBoveda(BigInteger idBovedaOrigen, BigInteger idBovedaDestino, Set<GenericDetalle> detalleTransaccion) {
		Response response;
		try {
			BigInteger idTransaccion = bovedaServiceTS.crearTransaccionBovedaBoveda(idBovedaOrigen, idBovedaDestino, detalleTransaccion);
			response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idTransaccion)).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getTransaccionesEntidadBoveda(BigInteger idAgencia, Integer offset, Integer limit) {
		List<TransaccionBovedaOtroView> list = transaccionInternaServiceNT.getTransaccionesEntidadBoveda(idAgencia, offset, limit);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getTransaccionesBovedaBovedaEnviados(BigInteger idAgencia, Integer offset, Integer limit) {	
		List<TransaccionBovedaBovedaView> list = transaccionInternaServiceNT.getTransaccionesBovedaBovedaEnviados(idAgencia, offset, limit);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getTransaccionesBovedaBovedaRecibidos(BigInteger idAgencia, Integer offset, Integer limit) {
		List<TransaccionBovedaBovedaView> list = transaccionInternaServiceNT.getTransaccionesBovedaBovedaRecibidos(idAgencia, offset, limit);
		return Response.status(Response.Status.OK).entity(list).build();
	}
	
	@Override
	public Response confirmarTransaccionBovedaBoveda(BigInteger id) {
		Response response;
		try {
			bovedaServiceTS.confirmarTransaccionBovedaBoveda(id);
			response = Response.status(Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response cancelarTransaccionBovedaBoveda(BigInteger id) {
		Response response;
		try {
			bovedaServiceTS.cancelarTransaccionBovedaBoveda(id);
			response = Response.status(Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}
}
