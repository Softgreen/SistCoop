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

import org.jboss.resteasy.annotations.cache.NoCache;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.type.TipoPendienteCaja;
import org.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.sistemafinanciero.rest.dto.CuentaBancariaDTO;
import org.sistemafinanciero.rest.dto.TransaccionBancariaDTO;
import org.sistemafinanciero.rest.dto.TransaccionChequeDTO;
import org.sistemafinanciero.rest.dto.TransaccionCompraVentaDTO;
import org.sistemafinanciero.rest.dto.TransaccionCuentaAporteDTO;
import org.sistemafinanciero.rest.dto.TransaccionGiroDTO;
import org.sistemafinanciero.rest.dto.TransaccionHistorialSobreGiroDTO;
import org.sistemafinanciero.rest.dto.TransaccionSobreGiroDTO;
import org.sistemafinanciero.rest.dto.TransferenciaBancariaDTO;

@Path("/session")
@NoCache
public interface SessionREST {

	@GET
	@Path("/caja")
	@Produces({ "application/xml", "application/json" })
	public Response getCajaOfSession();

	@POST
	@Path("/caja/abrir")
	@Produces({ "application/xml", "application/json" })
	public Response abrirCajaOfSession();

	@POST
	@Path("/caja/cerrar")
	@Produces({ "application/xml", "application/json" })
	public Response cerrarCajaOfSession(Set<GenericMonedaDetalle> detalle);

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
	@Path("/socio/{id}/desactivar")
	@Produces({ "application/xml", "application/json" })
	public Response desactivarSocio(@PathParam("id") BigInteger id);

	@POST
	@Path("/cuentasBancarias")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createCuentaPlazoFijo(CuentaBancariaDTO cuentaBancaria);

	@POST
	@Path("/transaccionPendiente")
	@Produces({ "application/xml", "application/json" })
	public Response crearPendienteCaja(@FormParam("tipo") TipoPendienteCaja tipoPendienteCaja, @FormParam("idboveda") BigInteger idboveda, @FormParam("monto") BigDecimal monto, @FormParam("observacion") String observacion, @FormParam("idPendienteRelacionado") BigInteger idPendienteRelacionado);

	@POST
	@Path("/transaccionCuentaAporte")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransaccionCuentaAporte(TransaccionCuentaAporteDTO transaccion);

	@POST
	@Path("/transaccionCompraVenta")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransaccionCompraVenta(TransaccionCompraVentaDTO tansaccion);

	@POST
	@Path("/transaccionBancaria")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransaccionBancaria(TransaccionBancariaDTO transaccion);

	@POST
	@Path("/transaccionCheque")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransaccionCheque(TransaccionChequeDTO transaccion);
	
	@POST
	@Path("/transaccionGiro")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransaccionGiro(TransaccionGiroDTO tansaccion);
	
	@POST
    @Path("/transaccionGiro/{idGiro}/extornar")
    @Consumes({ "application/xml", "application/json" })
    @Produces({ "application/xml", "application/json" })
    public Response extornarTransaccionGiro(@PathParam("idGiro") BigInteger idGiro);
	
	@POST
    @Path("/transaccionSobreGiro")
    @Consumes({ "application/xml", "application/json" })
    @Produces({ "application/xml", "application/json" })
    public Response crearTransaccionSobreGiro(TransaccionSobreGiroDTO transaccion);
	
	@POST
    @Path("/transaccionSobreGiro/pagar")
    @Consumes({ "application/xml", "application/json" })
    @Produces({ "application/xml", "application/json" })
    public Response crearTransaccionHistorialSobreGiro(TransaccionHistorialSobreGiroDTO historial);
	
	@POST
	@Path("/transaccion/{id}/extornar")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response extornar(@PathParam("id") BigInteger id);
	
	@POST
	@Path("/transferenciaBancaria")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response crearTransferencia(TransferenciaBancariaDTO transferencia);

	@POST
	@Path("/cuentasBancarias/{id}/cancelar")
	@Produces({ "application/xml", "application/json" })
	public Response cancelarCuentaBancariaConRetiro(@PathParam("id") BigInteger id);

	@POST
	@Path("/transaccionCajaCaja")
	@Produces({ "application/xml", "application/json" })
	public Response createTransaccionCajaCaja(@FormParam("idCaja") BigInteger idCaja, @FormParam("idMoneda") BigInteger idMoneda, @FormParam("monto") BigDecimal monto, @FormParam("observacion") String observacion);

	@POST
	@Path("/transaccionCajaCaja/{id}/confirmar")
	@Produces({ "application/xml", "application/json" })
	public Response confirmarTransaccionCajaCaja(@PathParam("id") BigInteger id);

	@POST
	@Path("/transaccionCajaCaja/{id}/cancelar")
	@Produces({ "application/xml", "application/json" })
	public Response cancelarTransaccionCajaCaja(@PathParam("id") BigInteger id);

	@POST
	@Path("/transaccionBovedaCaja/{origen}")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public Response createTransaccionBovedaCaja(@PathParam("origen") TransaccionBovedaCajaOrigen origen, Set<GenericDetalle> detalleTransaccion, @QueryParam("boveda") BigInteger idboveda, @QueryParam("caja") BigInteger idcaja);

	@POST
	@Path("/transaccionBovedaCaja/{id}/confirmar")
	@Produces({ "application/xml", "application/json" })
	public Response confirmarTransaccionBovedaCaja(@PathParam("id") BigInteger id);

	@POST
	@Path("/transaccionBovedaCaja/{id}/cancelar")
	@Produces({ "application/xml", "application/json" })
	public Response cancelarTransaccionBovedaCaja(@PathParam("id") BigInteger id);

}
