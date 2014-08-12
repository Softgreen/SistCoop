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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Titular;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.CuentaBancariaREST;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.dto.CuentaBancariaDTO;
import org.sistemafinanciero.rest.dto.TitularDTO;
import org.sistemafinanciero.service.nt.CuentaBancariaServiceNT;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.nt.SessionServiceNT;
import org.sistemafinanciero.service.ts.CuentaBancariaServiceTS;

public class CuentaBancariaRESTService implements CuentaBancariaREST {

	private final static String baseUrl = "/cuentasBancarias";

	@EJB
	private CuentaBancariaServiceNT cuentaBancariaServiceNT;

	@EJB
	private CuentaBancariaServiceTS cuentaBancariaServiceTS;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	@EJB
	private SessionServiceNT sessionServiceNT;

	@Override
	public Response findAll(String filterText, TipoCuentaBancaria[] tipoCuenta, TipoPersona[] tipoPersona, EstadoCuentaBancaria[] tipoEstadoCuenta, Integer offset, Integer limit) {
		List<CuentaBancariaView> list = cuentaBancariaServiceNT.findAllView(filterText, tipoCuenta, tipoPersona, tipoEstadoCuenta, offset, limit);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response count() {
		int count = cuentaBancariaServiceNT.count();
		Response response = Response.status(Response.Status.OK).entity(count).build();
		return response;
	}

	@Override
	public Response buscarByNumeroCuenta(String numeroCuenta) {
		CuentaBancariaView cuentaBancariaView = cuentaBancariaServiceNT.findByNumeroCuenta(numeroCuenta);
		Response response = Response.status(Response.Status.OK).entity(cuentaBancariaView).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		CuentaBancariaView cuentaBancariaView = cuentaBancariaServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(cuentaBancariaView).build();
		return response;
	}

	@Override
	public Response getCertificado(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getEstadoCuenta(BigInteger id, Long desde, Long hasta) {
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		List<EstadocuentaBancariaView> list = cuentaBancariaServiceNT.getEstadoCuenta(id, dateDesde, dateHasta);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response congelar(BigInteger id) {
		Response response;
		try {
			cuentaBancariaServiceTS.congelarCuentaBancaria(id);
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
			cuentaBancariaServiceTS.descongelarCuentaBancaria(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response recalcular(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response renovar(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response cancelarCuentaBancaria(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response create(CuentaBancariaDTO cuentaBancaria) {
		Response response;

		TipoCuentaBancaria tipoCuentaBancaria = cuentaBancaria.getTipoCuenta();
		BigInteger idMoneda = cuentaBancaria.getIdMoneda();
		BigDecimal tasaInteres = cuentaBancaria.getTasaInteres();
		TipoPersona tipoPersona = cuentaBancaria.getTipoPersona();
		int periodo = cuentaBancaria.getPeriodo();
		int cantRetirantes = cuentaBancaria.getCantRetirantes();
		List<BigInteger> titulares = cuentaBancaria.getTitulares();
		List<Beneficiario> beneficiarios = cuentaBancaria.getBeneficiarios();

		Agencia agencia = sessionServiceNT.getAgenciaOfSession();
		PersonaNatural persona = personaNaturalServiceNT.find(cuentaBancaria.getIdTipoDocumento(), cuentaBancaria.getNumeroDocumento());
		try {
			BigInteger idCuenta = cuentaBancariaServiceTS.create(tipoCuentaBancaria, agencia.getCodigo(), idMoneda, tasaInteres, tipoPersona, persona.getIdPersonaNatural(), periodo, cantRetirantes, titulares, beneficiarios);
			response = Response.status(Response.Status.CREATED).build();
			URI resource = new URI(baseUrl + "/" + idCuenta.toString());
			response = Response.created(resource).entity(Jsend.getSuccessJSend(idCuenta)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		} catch (URISyntaxException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response update(CuentaBancaria caja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getTitulares(BigInteger id, Boolean estado) {
		if (estado == null)
			estado = false;
		Set<Titular> list = cuentaBancariaServiceNT.getTitulares(id, estado);
		Response response = Response.status(Response.Status.CREATED).entity(list).build();
		return response;
	}

	@Override
	public Response getTitular(BigInteger id, BigInteger idTitular) {
		return null;
	}

	@Override
	public Response createTitular(BigInteger id, TitularDTO titular) {
		Response response;

		BigInteger idTipoDocumento = titular.getIdTipoDocumento();
		String numeroDocumento = titular.getNumeroDocumento();
		PersonaNatural personaNatural = personaNaturalServiceNT.find(idTipoDocumento, numeroDocumento);
		if (personaNatural != null) {
			try {
				BigInteger idTitular = cuentaBancariaServiceTS.addTitular(id, personaNatural.getIdPersonaNatural());
				response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTitular)).build();
			} catch (RollbackFailureException e) {
				Jsend jsend = Jsend.getErrorJSend(e.getMessage());
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
			}
		} else {
			response = Response.status(Response.Status.BAD_REQUEST).entity(Jsend.getErrorJSend("Titular no encontrado")).build();
		}
		return response;
	}

	@Override
	public Response updateTitular(BigInteger id, BigInteger idTitular) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response deleteTitular(BigInteger id, BigInteger idTitular) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getBeneficiarios(BigInteger id) {
		Set<Beneficiario> list = cuentaBancariaServiceNT.getBeneficiarios(id);
		Response response = Response.status(Response.Status.CREATED).entity(list).build();
		return response;
	}

	@Override
	public Response getBeneficiario(BigInteger id, BigInteger idBeneficiario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createBeneficiario(BigInteger id, Beneficiario beneficiario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateBeneficiario(BigInteger id, BigInteger idBeneficiario, Beneficiario beneficiario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response deleteBeneficiario(BigInteger id, BigInteger idBeneficiario) {
		try {
			cuentaBancariaServiceTS.deleteBeneficiario(idBeneficiario);
			return Response.status(Status.OK).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
