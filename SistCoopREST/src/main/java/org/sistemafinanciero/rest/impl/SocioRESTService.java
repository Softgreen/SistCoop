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

import javax.ws.rs.core.Response;

import org.sistemafinanciero.rest.SocioREST;

public class SocioRESTService implements SocioREST {

	@Override
	public Response listAll(String filterText, Boolean estadoCuentaAporte,
			Boolean estadoSocio, Integer offset, Integer limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response countAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findById(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getCuentaAporte(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response congelarCuentaAporte(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response descongelarCuentaAporte(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getApoderado(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response cambiarApoderado(BigInteger idSocio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response eliminarApoderado(BigInteger idSocio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getCuentasBancarias(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getAportesHistorial(BigInteger idSocio, Long desde,
			Long hasta) {
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
