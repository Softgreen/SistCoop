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

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.rest.TasaInteresREST;
import org.sistemafinanciero.service.nt.TasaInteresServiceNT;
import org.sistemafinanciero.service.nt.VariableSistemaServiceNT;

public class TasaInteresRESTService implements TasaInteresREST {

	@EJB
	private TasaInteresServiceNT tasaInteresServiceNT;

	@EJB
	private VariableSistemaServiceNT variableSistemaServiceNT;

	@Override
	public Response findTasaAhorroByMoneda(BigInteger idMoneda) {
		BigDecimal result = tasaInteresServiceNT.getTasaInteresCuentaAhorro(idMoneda);
		JsonObject model = Json.createObjectBuilder().add("valor", result).build();
		return Response.status(Response.Status.OK).entity(model).build();
	}

	@Override
	public Response findTasaCorrienteByMoneda(BigInteger idMoneda) {
		BigDecimal result = tasaInteresServiceNT.getTasaInteresCuentaCorriente(idMoneda);
		JsonObject model = Json.createObjectBuilder().add("valor", result).build();
		return Response.status(Response.Status.OK).entity(model).build();
	}

	@Override
	public Response findTasaPlazoFijoByMoneda(BigInteger idMoneda) {
		BigDecimal result = tasaInteresServiceNT.getTasaInteresCuentaPlazoFijo(idMoneda, 0, BigDecimal.ZERO);
		JsonObject model = Json.createObjectBuilder().add("valor", result).build();
		return Response.status(Response.Status.OK).entity(model).build();
	}

	@Override
	public Response findTasaPlazoFijoByMonedaPeriodoMonto(BigInteger idMoneda, int periodo, BigDecimal monto) {
		BigDecimal result = tasaInteresServiceNT.getTasaInteresCuentaPlazoFijo(idMoneda, periodo, monto);
		JsonObject model = Json.createObjectBuilder().add("valor", result).build();
		return Response.status(Response.Status.OK).entity(model).build();
	}

	@Override
	public Response getTasaCambio(BigInteger idMonedaRecibida, BigInteger idMonedaEntregada) {
		BigDecimal result = variableSistemaServiceNT.getTasaCambio(idMonedaRecibida, idMonedaEntregada);
		JsonObject model = Json.createObjectBuilder().add("valor", result).build();
		return Response.status(Response.Status.OK).entity(model).build();
	}

}
