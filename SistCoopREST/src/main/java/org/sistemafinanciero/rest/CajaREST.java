package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Caja;

@Path("/cajas")
public interface CajaREST {

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findById(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/bovedas")
	@Produces({ "application/xml", "application/json" })
	public Response getBovedas(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/monedas")
	@Produces({ "application/xml", "application/json" })
	public Response getMonedas(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/detalle")
	@Produces({ "application/xml", "application/json" })
	public Response getDetalleCaja(@PathParam("id") BigInteger id, @QueryParam("idHistorial") BigInteger idHistorial);

	@GET
	@Path("/{id}/historiales")
	@Produces({ "application/xml", "application/json" })
	public Response getHistorialCajaBetweenDates(@PathParam("id") BigInteger id, @QueryParam("desde") Long desde, @QueryParam("hasta") Long hasta);

	@GET
	@Path("/{id}/historiales/voucherCierreCaja")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response getVoucherCierreCaja(@PathParam("id") BigInteger id, @QueryParam("idHistorial") BigInteger idHistorial);

	@GET
	@Path("/{id}/historiales/resumenCierreCaja")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response getResumenCierreCaja(@PathParam("id") BigInteger idHistorialCaja, @QueryParam("idHistorial") BigInteger idHistorial);

	@GET
	@Path("{id}/transaccionCajaCaja/enviados")
	@Produces({ "application/xml", "application/json" })
	public Response getTransaccionesCajaCajaOfCajaEnviados(@PathParam("id") BigInteger id, @QueryParam("idHistorial") BigInteger idHistorial);

	@GET
	@Path("{id}/transaccionCajaCaja/recibidos")
	@Produces({ "application/xml", "application/json" })
	public Response getTransaccionesCajaCajaOfCajaRecibidos(@PathParam("id") BigInteger id, @QueryParam("idHistorial") BigInteger idHistorial);

	@GET
	@Path("/{id}/transaccionBovedaCaja/enviados")
	@Produces({ "application/xml", "application/json" })
	public Response getTransaccionesBovedaCajaEnviados(@PathParam("id") BigInteger id, @QueryParam("idHistorial") BigInteger idHistorial);

	@GET
	@Path("/{id}/transaccionBovedaCaja/recibidos")
	@Produces({ "application/xml", "application/json" })
	public Response getTransaccionesBovedaCajaOfCajaRecibidos(@PathParam("id") BigInteger id, @QueryParam("idHistorial") BigInteger idHistorial);

	@GET
	@Path("/{id}/pendientes")
	@Produces({ "application/xml", "application/json" })
	public Response getPendientesOfCaja(@PathParam("id") BigInteger id, @QueryParam("idHistorial") BigInteger idHistorial);

	@POST
	@Path("/{id}/desactivar")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response desactivarCaja(@PathParam("id") BigInteger id);

	@POST
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response create(Caja caja);

	@PUT
	@Path("/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response update(@PathParam("id") BigInteger id, Caja caja);

	@GET
	@Path("/voucherCuentaAporte/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response getVoucherCuentaAporte(@PathParam("id") BigInteger idTransaccionCuentaAporte);

	@GET
	@Path("/voucherCompraVenta/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response getVoucherCompraVenta(@PathParam("id") BigInteger idTransaccionCompraVenta);

	@GET
	@Path("/voucherTransaccionBancaria/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response getVoucherTransaccionBancaria(@PathParam("id") BigInteger idTransaccionTransaccionBancaria);

	@GET
	@Path("/voucherTransferenciaBancaria/{id}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response getVoucherTransferenciaBancaria(@PathParam("id") BigInteger idTransferenciaBancaria);

	@GET
	@Path("/{id}/historialTransaccion")
	@Produces({ "application/xml", "application/json" })
	public Response getHistorialTransaccionCaja(@PathParam("id") BigInteger idCaja, @QueryParam("idHistorial") BigInteger idHistorial, @QueryParam("filterText") String filterText);

}
