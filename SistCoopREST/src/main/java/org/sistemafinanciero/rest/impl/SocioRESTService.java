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

import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.SocioView;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.SocioREST;
import org.sistemafinanciero.service.nt.SocioServiceNT;
import org.sistemafinanciero.service.ts.SocioServiceTS;

public class SocioRESTService implements SocioREST {

	@EJB
	private SocioServiceNT socioServiceNT;

	@EJB
	private SocioServiceTS socioServiceTS;

	@Override
	public Response listAll(String filterText, Boolean estadoCuentaAporte, Boolean estadoSocio, Integer offset, Integer limit) {
		List<SocioView> list = socioServiceNT.findAllView(filterText, estadoCuentaAporte, estadoSocio, offset, limit);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response countAll() {
		int count = socioServiceNT.count();
		Response response = Response.status(Response.Status.OK).entity(count).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		SocioView socio = socioServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(socio).build();
		return response;
	}

	@Override
	public Response getCuentaAporte(BigInteger id) {
		CuentaAporte cuentaAporte = socioServiceNT.getCuentaAporte(id);
		Response response = Response.status(Response.Status.OK).entity(cuentaAporte).build();
		return response;
	}

	@Override
	public Response congelarCuentaAporte(BigInteger id) {
		Response response;
		try {
			socioServiceTS.congelarCuentaAporte(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response descongelarCuentaAporte(BigInteger id) {
		Response response;
		try {
			socioServiceTS.descongelarCuentaAporte(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getApoderado(BigInteger id) {		
		PersonaNatural apoderado = socioServiceNT.getApoderado(id);
		Response response = Response.status(Response.Status.OK).entity(apoderado).build();		
		return response;
	}

	@Override
	public Response cambiarApoderado(BigInteger idSocio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response eliminarApoderado(BigInteger idSocio) {
		Response response;
		try {
			socioServiceTS.deleteApoderado(idSocio);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getCuentasBancarias(BigInteger id) {
		List<CuentaBancariaView> list = socioServiceNT.getCuentasBancarias(id);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getAportesHistorial(BigInteger idSocio, Long desde, Long hasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createSocio() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response desactivarSocio(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

}
