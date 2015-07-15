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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.rest.dto.ApoderadoDTO;
import org.sistemafinanciero.rest.dto.SocioDTO;

@Path("/socios")
public interface SocioREST {

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response listAll(@QueryParam("filterText") String filterText, @QueryParam("cuentaAporte") Boolean estadoCuentaAporte, @QueryParam("estadoSocio") Boolean estadoSocio, @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

	@GET
	@Path("/findByTipoNumeroDocumento")
    @Produces({ "application/xml", "application/json" })
    public Response listAll(@QueryParam("tipoPersona") TipoPersona tipoPersona, @QueryParam("idTipoDocumento") BigInteger idTipoDocumento, @QueryParam("numeroDocumento") String numeroDocumento);
	
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

	@GET
	@Path("{id}/cartilla")
	public Response getCartillaInformacion(@PathParam("id") BigInteger id);

}
