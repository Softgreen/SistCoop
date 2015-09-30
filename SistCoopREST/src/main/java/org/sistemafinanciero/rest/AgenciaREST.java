package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Agencia;

@Path("/agencias")
public interface AgenciaREST {

    @GET
    @Produces({ "application/xml", "application/json" })
    public Response findAll(@QueryParam("estado") Boolean estado);

    @GET
    @Path("/{id}")
    @Produces({ "application/xml", "application/json" })
    public Response findById(@PathParam("id") BigInteger id);

    @POST
    @Produces({ "application/xml", "application/json" })
    public Response create(Agencia agencia);

    @PUT
    @Path("/{id}")
    @Produces({ "application/xml", "application/json" })
    public Response update(@PathParam("id") BigInteger id, Agencia agencia);

    @GET
    @Path("/{id}/bovedas")
    @Produces({ "application/json" })
    public Response getBovedasOfAgencia(@PathParam("id") BigInteger id);

    @GET
    @Path("/{id}/cajas")
    @Produces({ "application/json" })
    public Response getCajasOfAgencia(@PathParam("id") BigInteger id);

    @GET
    @Path("/{id}/giros/enviados")
    @Produces({ "application/json" })
    public Response getGirosEnviados(@PathParam("id") BigInteger id, @QueryParam("estado") String estadoGiro,
            @QueryParam("filterText") String filterText, @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit);

    @GET
    @Path("/{id}/giros/recibidos")
    @Produces({ "application/json" })
    public Response getGirosRecibidos(@PathParam("id") BigInteger id,
            @QueryParam("estado") String estadoGiro, @QueryParam("filterText") String filterText,
            @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

    @GET
    @Path("/{id}/giros/enviados/count")
    @Produces({ "application/xml", "application/json" })
    public Response countGirosEnviados(@PathParam("id") BigInteger id);

    @GET
    @Path("/{id}/giros/recibidos/count")
    @Produces({ "application/xml", "application/json" })
    public Response countGirosRecibidos(@PathParam("id") BigInteger id);

}
