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

import org.sistemafinanciero.entity.SobreGiro;
import org.sistemafinanciero.entity.type.EstadoSobreGiro;

@Path("/sobreGiros")
public interface SobreGiroREST {

    @GET
    @Produces({ "application/xml", "application/json" })
    public Response findAll(@QueryParam("estado") EstadoSobreGiro[] estadosGiro,
            @QueryParam("filterText") String filterText, @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit);

    @GET
    @Path("/{id}")
    @Produces({ "application/xml", "application/json" })
    public Response findById(@PathParam("id") BigInteger id);

    @POST
    @Produces({ "application/xml", "application/json" })
    public Response create(SobreGiro sobreGiro);

    @PUT
    @Path("/{id}")
    @Produces({ "application/xml", "application/json" })
    public Response update(@PathParam("id") BigInteger id, SobreGiro sobreGiro);

    /**
     * Historial
     */

    @GET
    @Path("/{id}/historiales")
    @Produces({ "application/xml", "application/json" })
    public Response getHistorial(@PathParam("id") BigInteger id);
    
}
