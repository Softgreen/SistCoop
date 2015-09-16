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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PendienteCaja;
import org.sistemafinanciero.entity.PendienteCajaFaltanteView;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.TransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.sistemafinanciero.entity.dto.CajaView;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.dto.ResumenOperacionesCaja;
import org.sistemafinanciero.entity.dto.VoucherCompraVenta;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBovedaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCheque;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCuentaAporte;
import org.sistemafinanciero.entity.dto.VoucherTransferenciaBancaria;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.CajaREST;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.dto.CajaDTO;
import org.sistemafinanciero.service.nt.CajaServiceNT;
import org.sistemafinanciero.service.nt.TransaccionInternaServiceNT;
import org.sistemafinanciero.service.ts.CajaServiceTS;
import org.sistemafinanciero.service.ts.SessionServiceTS;

public class CajaRESTService implements CajaREST {

	@EJB
	private CajaServiceNT cajaServiceNT;

	@EJB
	private CajaServiceTS cajaServiceTS;

	@EJB
	private SessionServiceTS cajaSessionServiceTS;

	@EJB
	private TransaccionInternaServiceNT transaccionInternaServiceNT;

	@Override
	public Response findAll(BigInteger idAgencia) {
		List<CajaView> list = cajaServiceNT.findAllView(idAgencia);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

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
	public Response getResumenCierreCaja(BigInteger idCaja, BigInteger idHistorial) {
		ResumenOperacionesCaja resumen = cajaServiceNT.getResumenOperacionesCaja(idHistorial);
		return Response.status(Response.Status.OK).entity(resumen).build();
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
			list = cajaServiceNT.getTransaccionesRecibidasCajaCaja(id);
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
	public Response getPendientesOfCaja(BigInteger id, BigInteger idHistorial) {
		Set<PendienteCaja> pendientes = cajaServiceNT.getPendientes(id, idHistorial);
		return Response.status(Response.Status.OK).entity(pendientes).build();
	}
	
	@Override
    public Response getPendientesPorPagarOfCaja(BigInteger id) {
        List<PendienteCajaFaltanteView> pendientes = cajaServiceNT.getPendientesPorPagar(id);
        return Response.status(Response.Status.OK).entity(pendientes).build();
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
	public Response create(CajaDTO cajaDTO) {
		try {
			Caja caja = new Caja();
			caja.setDenominacion(cajaDTO.getDenominacion());
			caja.setAbreviatura(cajaDTO.getAbreviatura());

			List<BigInteger> idBovedas = new ArrayList<BigInteger>();
			for (BigInteger id : cajaDTO.getBovedas()) {
				idBovedas.add(id);
			}
			BigInteger id = cajaServiceTS.create(caja, idBovedas);
			return Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(id)).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response update(BigInteger idCaja, CajaDTO cajaDTO) {
		try {
			Caja caja = new Caja();
			caja.setIdCaja(idCaja);
			caja.setDenominacion(cajaDTO.getDenominacion());
			caja.setAbreviatura(cajaDTO.getAbreviatura());

			cajaServiceTS.update(idCaja, caja, cajaDTO.getBovedas());
			return Response.status(Response.Status.OK).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (PreexistingEntityException e) {
			return Response.status(Response.Status.CONFLICT).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
	}

	@Override
	public Response getVoucherCuentaAporte(BigInteger idTransaccionCompraVenta) {
		VoucherTransaccionCuentaAporte voucher = cajaServiceNT.getVoucherCuentaAporte(idTransaccionCompraVenta);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getVoucherCompraVenta(BigInteger idTransaccionCompraVenta) {
		VoucherCompraVenta voucher = cajaServiceNT.getVoucherCompraVenta(idTransaccionCompraVenta);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getVoucherTransaccionBancaria(BigInteger idTransaccionTransaccionBancaria) {
		VoucherTransaccionBancaria voucher = cajaServiceNT.getVoucherTransaccionBancaria(idTransaccionTransaccionBancaria);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getVoucherTransferenciaBancaria(BigInteger idTransferenciaBancaria) {
		VoucherTransferenciaBancaria voucher = cajaServiceNT.getVoucherTransferenciaBancaria(idTransferenciaBancaria);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getVoucherCheque(BigInteger idTransaccionCheque) {
		VoucherTransaccionCheque voucher = cajaServiceNT.getVoucherTransaccionCheque(idTransaccionCheque);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}
	
	@Override
	public Response getVoucherTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) {
		VoucherTransaccionBovedaCaja voucher = transaccionInternaServiceNT.getVoucherTransaccionBovedaCaja(idTransaccionBovedaCaja);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getVoucherTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) {
		VoucherTransaccionCajaCaja voucher = transaccionInternaServiceNT.getVoucherTransaccionCajaCaja(idTransaccionCajaCaja);
		return Response.status(Response.Status.OK).entity(voucher).build();
	}

	@Override
	public Response getHistorialTransaccionCaja(BigInteger idCaja, BigInteger idHistorial, String filterText) {
		List<HistorialTransaccionCaja> list = cajaServiceNT.getHistorialTransaccion(idCaja, idHistorial, filterText);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response getTrabajadores(BigInteger id) {
		List<Trabajador> list = cajaServiceNT.getTrabajadores(id);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response createTrabajador(BigInteger idCaja, Trabajador trabajador) {
		Response response;

		BigInteger idTrabajador = trabajador.getIdTrabajador();
		try {
			BigInteger id = cajaServiceTS.createTrabajadorCaja(idCaja, idTrabajador);
			response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(id)).build();
		} catch (NonexistentEntityException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (PreexistingEntityException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
		return response;
	}

	@Override
	public Response deleteTrabajador(BigInteger idCaja, BigInteger idTrabajador) {
		Response response;
		try {
			cajaServiceTS.deleteTrabajadorCaja(idCaja, idTrabajador);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
		return response;
	}

	@Override
	public Response abrirCaja(BigInteger idCaja) {
		Response response;
		try {
			BigInteger idTransaccion = cajaServiceTS.abrir(idCaja);
			response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idTransaccion)).build();
		} catch (NonexistentEntityException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
		return response;
	}

	@Override
	public Response congelar(BigInteger id) {
		Response response;
		try {
			cajaServiceTS.congelar(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
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
	public Response descongelar(BigInteger id) {
		Response response;
		try {
			cajaServiceTS.descongelar(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	

}
