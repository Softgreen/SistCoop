package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.Chequera;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.rest.dto.CuentaBancariaDTO;
import org.sistemafinanciero.rest.dto.TitularDTO;

@Path("/cuentasBancarias")
public interface CuentaBancariaREST {

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response findAll(@QueryParam("filterText") String filterText, @QueryParam("tipoCuenta") TipoCuentaBancaria[] tipoCuenta, @QueryParam("tipoPersona") TipoPersona[] tipoPersona, @QueryParam("tipoEstadoCuenta") EstadoCuentaBancaria[] tipoEstadoCuenta, @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

	@GET
	@Path("/count")
	@Produces({ "application/xml", "application/json" })
	public Response count();

	@GET
	@Path("/buscar")
	@Produces({ "application/xml", "application/json" })
	public Response buscarByNumeroCuenta(@QueryParam("numeroCuenta") String numeroCuenta);

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findById(@PathParam("id") BigInteger id);

	@GET
	@Path("{id}/certificado")
	public Response getCertificado(@PathParam("id") BigInteger id);

	@GET
	@Path("{id}/cartilla")
	public Response getCartillaInformacion(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/estadoCuenta")
	public Response getEstadoCuenta(@PathParam("id") BigInteger id, @QueryParam("desde") Long desde, @QueryParam("hasta") Long hasta, @QueryParam("estado") Boolean estado);

	@POST
	@Path("/{id}/congelar")
	@Produces({ "application/xml", "application/json" })
	public Response congelar(@PathParam("id") BigInteger id);

	@POST
	@Path("/{id}/descongelar")
	@Produces({ "application/xml", "application/json" })
	public Response descongelar(@PathParam("id") BigInteger id);

	@POST
	@Path("/{id}/recalcular")
	@Produces({ "application/xml", "application/json" })
	public Response recalcular(@PathParam("id") BigInteger id, CuentaBancariaDTO cuenta);

	@POST
	@Path("/{id}/renovar")
	@Produces({ "application/xml", "application/json" })
	public Response renovar(@PathParam("id") BigInteger id);

	@POST
	@Path("/{id}/cancelar")
	@Produces({ "application/xml", "application/json" })
	public Response cancelarCuentaBancaria(@PathParam("id") BigInteger id);

	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response create(CuentaBancariaDTO cuentaBancaria);

	@PUT
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response update(CuentaBancaria caja);

	@GET
	@Path("/{id}/titulares")
	@Produces({ "application/xml", "application/json" })
	public Response getTitulares(@PathParam("id") BigInteger id, @QueryParam("state") Boolean estado);

	@GET
	@Path("/{id}/titulares/{idTitular}")
	@Produces({ "application/xml", "application/json" })
	public Response getTitular(@PathParam("id") BigInteger id, @PathParam("idTitular") BigInteger idTitular);

	@POST
	@Path("/{id}/titulares")
	@Produces({ "application/xml", "application/json" })
	public Response createTitular(@PathParam("id") BigInteger id, TitularDTO titular);

	@PUT
	@Path("/{id}/titulares/{idTitular}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response updateTitular(@PathParam("id") BigInteger id, @PathParam("idTitular") BigInteger idTitular);

	@DELETE
	@Path("/{id}/titulares/{idTitular}")
	@Produces({ "application/xml", "application/json" })
	public Response deleteTitular(@PathParam("id") BigInteger id, @PathParam("idTitular") BigInteger idTitular);

	@GET
	@Path("/{id}/beneficiarios")
	@Produces({ "application/xml", "application/json" })
	public Response getBeneficiarios(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/beneficiarios/{idBeneficiario}")
	@Produces({ "application/xml", "application/json" })
	public Response getBeneficiario(@PathParam("id") BigInteger id, @PathParam("idBeneficiario") BigInteger idBeneficiario);

	@POST
	@Path("/{id}/beneficiarios")
	@Produces({ "application/xml", "application/json" })
	public Response createBeneficiario(@PathParam("id") BigInteger id, Beneficiario beneficiario);

	@PUT
	@Path("/{id}/beneficiarios/{idBeneficiario}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response updateBeneficiario(@PathParam("id") BigInteger id, @PathParam("idBeneficiario") BigInteger idBeneficiario, Beneficiario beneficiario);

	@DELETE
	@Path("/{id}/beneficiarios/{idBeneficiario}")
	@Produces({ "application/xml", "application/json" })
	public Response deleteBeneficiario(@PathParam("id") BigInteger id, @PathParam("idBeneficiario") BigInteger idBeneficiario);

	/**
	 * Chequera
	 */
	@GET
	@Path("/{id}/chequeras/{idChequera}")
	@Produces({ "application/xml", "application/json" })
	public Response getChequera(@PathParam("id") BigInteger idCuentaBancaria, @PathParam("idChequera") BigInteger idChequera);

	@GET
	@Path("/{id}/chequeras/ultimo")
	@Produces({ "application/xml", "application/json" })
	public Response getUltimaChequera(@PathParam("id") BigInteger idCuentaBancaria);

	@GET
	@Path("/{id}/chequeras")
	@Produces({ "application/xml", "application/json" })
	public Response getChequeras(@PathParam("id") BigInteger idCuentaBancaria);

	@POST
	@Path("/{id}/chequeras")
	@Produces({ "application/xml", "application/json" })
	public Response createChequera(@PathParam("id") BigInteger idCuentaBancaria, Chequera chequera);

	@POST
	@Path("/{id}/chequeras/{idChequera}/desactivar")
	@Produces({ "application/xml", "application/json" })
	public Response desactivarChequera(@PathParam("id") BigInteger idCuentaBancaria, @PathParam("idChequera") BigInteger idChequera);

	@POST
	@Path("/{id}/cheques/{numeroChequeUnico}/desactivar")
	@Produces({ "application/xml", "application/json" })
	public Response desactivarCheque(@PathParam("id") BigInteger idCuentaBancaria, @PathParam("numeroChequeUnico") BigInteger numeroChequeUnico);

	@POST
	@Path("/{id}/cheques/{numeroChequeUnico}/congelar")
	@Produces({ "application/xml", "application/json" })
	public Response congelarCheque(@PathParam("id") BigInteger idCuentaBancaria, @PathParam("numeroChequeUnico") BigInteger numeroChequeUnico);

	@POST
	@Path("/{id}/cheques/{numeroChequeUnico}/descongelar")
	@Produces({ "application/xml", "application/json" })
	public Response descongelarCheque(@PathParam("id") BigInteger idCuentaBancaria, @PathParam("numeroChequeUnico") BigInteger numeroChequeUnico);

	
	/**
	 * Cheque
	 */
	@GET
	@Path("/{id}/chequeras/{idChequera}/cheques")
	@Produces({ "application/xml", "application/json" })
	public Response getChequesOfChequera(@PathParam("id") BigInteger idCuentaBancaria, @PathParam("idChequera") BigInteger idChequera);

	@GET
	@Path("/{id}/chequeras/{idChequera}/cheques/{idCheque}")
	@Produces({ "application/xml", "application/json" })
	public Response getCheque(@PathParam("id") BigInteger idCuentaBancaria, @PathParam("idChequera") BigInteger idChequera, @PathParam("idCheque") BigInteger idCheque);

	@GET
	@Path("/cheques/{numeroChequeUnico}")
	@Produces({ "application/xml", "application/json" })
	public Response getChequeByNumeroUnico(@PathParam("numeroChequeUnico") BigInteger numeroChequeUnico);

	@GET
	@Path("/cheques/{numeroChequeUnico}/cuentaBancaria")
	@Produces({ "application/xml", "application/json" })
	public Response getChequeByNumeroUnicoCuentaBancaria(@PathParam("numeroChequeUnico") BigInteger numeroChequeUnico);
	
	
	
	@GET
	@Path("{id}/estadoCuenta/pdf")
	public Response getEstadoCuentaPdf(@PathParam("id") BigInteger id, @QueryParam("desde") Long desde, @QueryParam("hasta") Long hasta);

	
	/**MAIL*/
	
	@POST
	@Path("/{id}/enviarEstadoCuenta/pdf")
	@Produces({ "application/xml", "application/json" })
	public Response enviarEstadoCuentaPdf(@PathParam("id") BigInteger idCuentaBancaria, @QueryParam("desde") Long desde, @QueryParam("hasta") Long hasta);
	
	@POST
	@Path("/{id}/enviarEstadoCuenta/excel")
	@Produces({ "application/xml", "application/json" })
	public Response enviarEstadoCuentaExcel(@PathParam("id") BigInteger idCuentaBancaria, @QueryParam("desde") Long desde, @QueryParam("hasta") Long hasta);
	
}
