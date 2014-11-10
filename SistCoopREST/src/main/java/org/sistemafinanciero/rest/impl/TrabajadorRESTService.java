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

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.TrabajadorREST;
import org.sistemafinanciero.rest.dto.TrabajadorDTO;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.nt.TrabajadorServiceNT;
import org.sistemafinanciero.service.ts.TrabajadorServiceTS;

public class TrabajadorRESTService implements TrabajadorREST {

	@EJB
	private TrabajadorServiceNT trabajadorServiceNT;

	@EJB
	private TrabajadorServiceTS trabajadorServiceTS;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	@Override
	public Response listAll(String filterText, BigInteger idAgencia) {
		List<Trabajador> list = trabajadorServiceNT.findAllByFilterTextAndAgencia(filterText, idAgencia);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		Trabajador trabajador = trabajadorServiceNT.findById(id);

		TrabajadorDTO result = new TrabajadorDTO();
		result.setId(trabajador.getIdTrabajador());
		result.setIdAgencia(trabajador.getAgencia().getIdAgencia());
		result.setIdSucursal(trabajador.getAgencia().getSucursal().getIdSucursal());
		result.setIdTipoDocumento(trabajador.getPersonaNatural().getTipoDocumento().getIdTipoDocumento());
		result.setNumeroDocumento(trabajador.getPersonaNatural().getNumeroDocumento());
		result.setUsuario(trabajador.getUsuario());
		result.setEstado(trabajador.getEstado());
		
		return Response.ok().entity(result).build();
	}

	@Override
	public Response create(TrabajadorDTO trabajadorDTO) {
		Response response;

		Agencia agencia = new Agencia();
		agencia.setIdAgencia(trabajadorDTO.getIdAgencia());

		PersonaNatural personaNatural = personaNaturalServiceNT.find(trabajadorDTO.getIdTipoDocumento(), trabajadorDTO.getNumeroDocumento());

		Trabajador trabajador = new Trabajador();
		trabajador.setIdTrabajador(null);
		trabajador.setAgencia(agencia);
		trabajador.setEstado(true);
		trabajador.setPersonaNatural(personaNatural);
		trabajador.setUsuario(trabajadorDTO.getUsuario());

		try {
			trabajadorServiceTS.create(trabajador);

			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (PreexistingEntityException e) {
			response = Response.status(Response.Status.CONFLICT).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
		return response;
	}

	@Override
	public Response update(BigInteger id, TrabajadorDTO trabajadorDTO) {
		Response response;

		Agencia agencia = new Agencia();
		agencia.setIdAgencia(trabajadorDTO.getIdAgencia());

		PersonaNatural personaNatural = personaNaturalServiceNT.find(trabajadorDTO.getIdTipoDocumento(), trabajadorDTO.getNumeroDocumento());

		Trabajador trabajador = new Trabajador();
		trabajador.setIdTrabajador(null);
		trabajador.setAgencia(agencia);
		trabajador.setEstado(true);
		trabajador.setPersonaNatural(personaNatural);
		trabajador.setUsuario(trabajadorDTO.getUsuario());

		try {
			trabajadorServiceTS.update(id, trabajador);
			response = Response.noContent().build();
		} catch (NonexistentEntityException e) {
			response = Response.status(Response.Status.NOT_FOUND).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (PreexistingEntityException e) {
			response = Response.status(Response.Status.CONFLICT).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
		return response;
	}

	@Override
	public Response desactivar(BigInteger id) {		
		Response response;		
		try {
			trabajadorServiceTS.desactivar(id);
			response = Response.noContent().build();
		} catch (NonexistentEntityException e) {
			response = Response.status(Response.Status.NOT_FOUND).entity(Jsend.getErrorJSend(e.getMessage())).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend(e.getMessage())).build();
		}
		return response;
	}

}
