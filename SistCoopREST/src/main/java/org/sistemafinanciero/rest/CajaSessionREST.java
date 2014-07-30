package org.sistemafinanciero.rest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.sistemafinanciero.entity.dto.GenericDetalle;

@Path("/session")
public interface CajaSessionREST {

	@GET
	@Path("/caja")
	@Produces({ "application/xml", "application/json" })
	public Response getCajaOfSession();

	@GET
	@Path("/agencia")
	@Produces({ "application/xml", "application/json" })
	public Response getAgenciaOfSession();

	@GET
	@Path("/usuario")
	public Response getUsuario(@Context SecurityContext context);

	@GET
	@Path("/persona")
	@Produces({ "application/xml", "application/json" })
	public Response getPersonaOfSession();

	@POST
	@Path("/transaccionPendiente")
	@Produces({ "application/xml", "application/json" })
	public Response crearPendiente(@FormParam("idboveda") BigInteger idboveda,
			@FormParam("monto") BigDecimal monto,
			@FormParam("observacion") String observacion);

	@POST
	@Path("/transaccionAporte")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransaccionCuentaAporte();

	@POST
	@Path("/transaccionCompraVenta")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransaccionCompraVenta();

	@POST
	@Path("/transaccionBancaria")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransaccionBancaria();

	@POST
	@Path("/transferenciaBancaria")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransferencia();

	@POST
	@Path("/cuentaBancaria/cancelar/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response cancelarCuentaBancariaConRetiro(
			@PathParam("id") BigInteger id);

	@POST
	@Path("/transaccionCajaCaja")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createTransaccionCajaCaja();

	@POST
	@Path("/transaccionCajaCaja/{id}/confirmar")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response confirmarTransaccionCajaCaja(@PathParam("id") BigInteger id);

	@POST
	@Path("/transaccionCajaCaja/{id}/cancelar")
	@Produces({ "application/xml", "application/json" })
	public Response cancelarTransaccionCajaCaja(@PathParam("id") BigInteger id);

	@POST
	@Path("/transaccionBovedaCaja")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createTransaccionBovedaCaja(
			Set<GenericDetalle> detalleTransaccion,
			@QueryParam("boveda") BigInteger idboveda);

	@POST
	@Path("/transaccionBovedaCaja/{id}/confirmar")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response confirmarTransaccionBovedaCaja(
			@PathParam("id") BigInteger id);

	@POST
	@Path("/transaccionBovedaCaja/{id}/cancelar")
	@Produces({ "application/xml", "application/json" })
	public Response cancelarTransaccionBovedaCaja(@PathParam("id") BigInteger id);

}
