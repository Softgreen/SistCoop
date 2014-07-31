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
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.TransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.dto.VoucherCompraVenta;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCuentaAporte;
import org.sistemafinanciero.entity.dto.VoucherTransferenciaBancaria;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.CajaREST;
import org.sistemafinanciero.service.nt.CajaServiceNT;
import org.sistemafinanciero.service.ts.CajaServiceTS;
import org.sistemafinanciero.service.ts.SessionServiceTS;

public class CajaRESTService implements CajaREST {

	@EJB
	private CajaServiceNT cajaServiceNT;

	@EJB
	private CajaServiceTS cajaServiceTS;

	@EJB
	private SessionServiceTS cajaSessionServiceTS;

	@Override
	public Response findById(BigInteger id) {
		Caja caja = cajaServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(caja).build();
		return response;
	}

	@Override
	public Response getBovedas(BigInteger id) {
		Set<Boveda> bovedas = cajaServiceNT.getBovedas(id);
		Response response = Response.status(Response.Status.OK).entity(bovedas).build();
		return response;
	}

	@Override
	public Response getMonedas(BigInteger id) {
		Set<Moneda> bovedas = cajaServiceNT.getMonedas(id);
		Response response = Response.status(Response.Status.OK).entity(bovedas).build();
		return response;
	}

	@Override
	public Response getDetalleCaja(BigInteger id, BigInteger idHistorial) {
		Set<GenericMonedaDetalle> detalle;
		if (idHistorial == null)
			detalle = cajaServiceNT.getDetalleCaja(id);
		else
			detalle = cajaServiceNT.getDetalleCaja(id, idHistorial);
		Response response = Response.status(Response.Status.OK).entity(detalle).build();
		return response;
	}

	@Override
	public Response getHistorialCajaBetweenDates(BigInteger id, Long desde, Long hasta) {
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);

		Set<HistorialCaja> list = null;
		list = cajaServiceNT.getHistorialCaja(id, dateDesde, dateHasta);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getVoucherCierreCaja(BigInteger id, BigInteger idHistorial) {
		Set<CajaCierreMoneda> list = cajaServiceNT.getVoucherCierreCaja(idHistorial);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getResumenCierreCaja(BigInteger idHistorialCaja, BigInteger idHistorial) {
		Set<CajaCierreMoneda> list = cajaServiceNT.getVoucherCierreCaja(idHistorial);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getTransaccionesCajaCajaOfCajaEnviados(BigInteger id, BigInteger idHistorial) {
		Set<TransaccionCajaCaja> list;
		if (idHistorial != null)
			list = cajaServiceNT.getTransaccionesEnviadasCajaCaja(id, idHistorial);
		else
			list = cajaServiceNT.getTransaccionesEnviadasCajaCaja(id);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getTransaccionesCajaCajaOfCajaRecibidos(BigInteger id, BigInteger idHistorial) {
		Set<TransaccionCajaCaja> list;
		if (idHistorial != null)
			list = cajaServiceNT.getTransaccionesRecibidasCajaCaja(id, idHistorial);
		else
			list = cajaServiceNT.getTransaccionesEnviadasCajaCaja(id);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getTransaccionesBovedaCajaEnviados(BigInteger id, BigInteger idHistorial) {
		List<TransaccionBovedaCajaView> list;
		if (idHistorial != null)
			list = cajaServiceNT.getTransaccionesEnviadasBovedaCaja(id, idHistorial);
		else
			list = cajaServiceNT.getTransaccionesEnviadasBovedaCaja(id);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getTransaccionesBovedaCajaOfCajaRecibidos(BigInteger id, BigInteger idHistorial) {
		List<TransaccionBovedaCajaView> list;
		if (idHistorial != null)
			list = cajaServiceNT.getTransaccionesRecibidasBovedaCaja(id, idHistorial);
		else
			list = cajaServiceNT.getTransaccionesRecibidasBovedaCaja(id);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response abrir(BigInteger id) {
		try {
			cajaSessionServiceTS.abrirCaja();
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.NO_CONTENT).build();
		}
	}

	@Override
	public Response cerrar(BigInteger id) {
		try {
			cajaSessionServiceTS.cerrarCaja(null);
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response desactivarCaja(BigInteger id) {
		try {
			cajaServiceTS.desactivar(id);
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response create(Caja caja) {
		try {
			cajaServiceTS.create(caja);
			return Response.status(Response.Status.CREATED).build();
		} catch (PreexistingEntityException e) {
			return Response.status(Response.Status.CONFLICT).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response update(BigInteger idCaja, Caja caja) {
		try {
			cajaServiceTS.update(idCaja, caja);
			return Response.status(Response.Status.OK).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (PreexistingEntityException e) {
			return Response.status(Response.Status.CONFLICT).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response getVoucherCuentaAporte(BigInteger idTransaccionCompraVenta) {
		VoucherTransaccionCuentaAporte voucher = cajaServiceNT.getVoucherCuentaAporte(idTransaccionCompraVenta);
		return Response.status(Response.Status.NO_CONTENT).entity(voucher).build();
	}

	@Override
	public Response getVoucherCompraVenta(BigInteger idTransaccionCompraVenta) {
		VoucherCompraVenta voucher = cajaServiceNT.getVoucherCompraVenta(idTransaccionCompraVenta);
		return Response.status(Response.Status.NO_CONTENT).entity(voucher).build();
	}

	@Override
	public Response getVoucherTransaccionBancaria(BigInteger idTransaccionTransaccionBancaria) {
		VoucherTransaccionBancaria voucher = cajaServiceNT.getVoucherTransaccionBancaria(idTransaccionTransaccionBancaria);
		return Response.status(Response.Status.NO_CONTENT).entity(voucher).build();
	}

	@Override
	public Response getVoucherTransferenciaBancaria(BigInteger idTransferenciaBancaria) {
		VoucherTransferenciaBancaria voucher = cajaServiceNT.getVoucherTransferenciaBancaria(idTransferenciaBancaria);
		return Response.status(Response.Status.NO_CONTENT).entity(voucher).build();
	}

	@Override
	public Response getHistorialTransaccionCaja(String filterText, BigInteger idCaja, BigInteger idAgencia) {
		// TODO Auto-generated method stub
		return null;
	}

}