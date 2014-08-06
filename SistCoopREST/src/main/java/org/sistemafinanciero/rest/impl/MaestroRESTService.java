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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Departamento;
import org.sistemafinanciero.entity.Distrito;
import org.sistemafinanciero.entity.Pais;
import org.sistemafinanciero.entity.Provincia;
import org.sistemafinanciero.entity.type.Estadocivil;
import org.sistemafinanciero.entity.type.Sexo;
import org.sistemafinanciero.entity.type.TipoEmpresa;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.rest.MaestroREST;
import org.sistemafinanciero.service.nt.MaestroServiceNT;

public class MaestroRESTService implements MaestroREST {

	@EJB
	private MaestroServiceNT maestroServiceNT;

	@Override
	public Response getTipoPersonas() {
		Response response;
		TipoPersona[] s = TipoPersona.values();
		List<TipoPersona> list = new ArrayList<TipoPersona>();
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getEstadosCiviles() {
		Response response;
		Estadocivil[] e = Estadocivil.values();
		List<Estadocivil> list = new ArrayList<Estadocivil>();
		for (int i = 0; i < e.length; i++) {
			list.add(e[i]);
		}
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getSexos() {
		Response response;
		Sexo[] s = Sexo.values();
		List<Sexo> list = new ArrayList<Sexo>();
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getTiposEmpresa() {
		Response response;
		TipoEmpresa[] s = TipoEmpresa.values();
		List<TipoEmpresa> list = new ArrayList<TipoEmpresa>();
		for (int i = 0; i < s.length; i++) {
			list.add(s[i]);
		}
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getPaises() {
		Response response;
		List<Pais> list = maestroServiceNT.getPaises();
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response findPaisByAbreviatura(String abreviatura) {
		Response response;
		Pais pais = maestroServiceNT.findPaisByAbreviatura(abreviatura);
		response = Response.status(Response.Status.OK).entity(pais).build();
		return response;
	}

	@Override
	public Response findPaisByCodigo(String codigo) {
		Response response;
		Pais pais = maestroServiceNT.findPaisByCodigo(codigo);
		response = Response.status(Response.Status.OK).entity(pais).build();
		return response;
	}

	@Override
	public Response getDepartamentos() {
		Response response;
		List<Departamento> list = maestroServiceNT.getDepartamentos();
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getProvinciasByCodigo(String codigoDepartamento) {
		Response response;
		List<Provincia> list = maestroServiceNT.getProvincias(codigoDepartamento);
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getDistritosByCodigo(String codigoDepartamento, String codigoProvincia) {
		Response response;
		List<Distrito> list = maestroServiceNT.getDistritos(codigoDepartamento, codigoProvincia);
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

}
