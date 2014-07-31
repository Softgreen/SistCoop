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

import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.rest.TasaInteresREST;

public class TasaInteresRESTService implements TasaInteresREST {

	@Override
	public Response findCaja(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findTasaAhorroByMoneda(BigInteger idMoneda) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findTasaCorrienteByMoneda(BigInteger idMoneda) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findTasaPlazoFijoByMoneda(BigInteger idMoneda) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findTasaPlazoFijoByMonedaPeriodoMonto(BigInteger idMoneda,
			int periodo, BigDecimal monto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getTasaCambio(BigInteger idMonedaRecibida,
			BigInteger idMonedaEntregada) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createCaja() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateCaja(Caja caja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateCaja(int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
