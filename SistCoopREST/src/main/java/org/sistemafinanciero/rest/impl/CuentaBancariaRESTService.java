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

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.CuentaBancariaREST;
import org.sistemafinanciero.service.nt.CuentaBancariaServiceNT;
import org.sistemafinanciero.service.ts.CuentaBancariaServiceTS;

public class CuentaBancariaRESTService implements CuentaBancariaREST {

	@EJB
	private CuentaBancariaServiceNT cuentaBancariaServiceNT;

	@EJB
	private CuentaBancariaServiceTS cuentaBancariaServiceTS;

	@Override
	public Response findAll(String filterText, TipoCuentaBancaria[] tipoCuenta,
			TipoPersona[] tipoPersona, EstadoCuentaBancaria[] tipoEstadoCuenta,
			BigInteger offset, BigInteger limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response count() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response buscarByNumeroCuenta(String numeroCuenta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response findById(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getSocio(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getCertificado(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getEstadoCuenta(BigInteger id, Long desde, Long hasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response congelar(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response descongelar(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
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
	public Response create() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response update(CuentaBancaria caja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getTitulares(BigInteger id, Boolean estado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getTitular(BigInteger id, BigInteger idTitular) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createTitular(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
	public Response updateBeneficiario(BigInteger id,
			BigInteger idBeneficiario, Beneficiario beneficiario) {
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
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

}
