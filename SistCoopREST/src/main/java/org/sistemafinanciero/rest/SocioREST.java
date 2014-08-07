package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.rest.dto.ApoderadoDTO;
import org.sistemafinanciero.rest.dto.SocioDTO;

@Path("/socios")
public interface SocioREST {

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response listAll(@QueryParam("filterText") String filterText, @QueryParam("cuentaAporte") Boolean estadoCuentaAporte, @QueryParam("estadoSocio") Boolean estadoSocio, @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

	@GET
	@Path("/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Response countAll();

	@GET
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response findById(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/cuentaAporte")
	@Produces({ "application/xml", "application/json" })
	public Response getCuentaAporte(@PathParam("id") BigInteger id);

	@POST
	@Path("/{id}/cuentaAporte/congelar")
	@Produces({ "application/xml", "application/json" })
	public Response congelarCuentaAporte(@PathParam("id") BigInteger id);

	@POST
	@Path("/{id}/cuentaAporte/descongelar")
	@Produces({ "application/xml", "application/json" })
	public Response descongelarCuentaAporte(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/apoderado")
	@Produces({ "application/xml", "application/json" })
	public Response getApoderado(@PathParam("id") BigInteger id);

	@POST
	@Path("/{id}/apoderado/cambiar")
	@Produces({ "application/xml", "application/json" })
	public Response cambiarApoderado(@PathParam("id") BigInteger idSocio, ApoderadoDTO apoderado);

	@POST
	@Path("/{id}/apoderado/eliminar")
	@Produces({ "application/xml", "application/json" })
	public Response eliminarApoderado(@PathParam("id") BigInteger idSocio);

	@GET
	@Path("/{id}/cuentasBancarias")
	@Produces({ "application/xml", "application/json" })
	public Response getCuentasBancarias(@PathParam("id") BigInteger id);

	@GET
	@Path("/{id}/historialAportes")
	@Produces({ "application/xml", "application/json" })
	public Response getAportesHistorial(@PathParam("id") BigInteger idSocio, @QueryParam("desde") Long desde, @QueryParam("hasta") Long hasta);

	@POST
	@Produces({ "application/xml", "application/json" })
	public Response createSocio(SocioDTO socio);

	@POST
	@Path("/{id}/desactivar")
	@Produces({ "application/xml", "application/json" })
	public Response desactivarSocio(@PathParam("id") BigInteger id);

}
