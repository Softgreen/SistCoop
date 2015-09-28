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

import org.sistemafinanciero.entity.Giro;
import org.sistemafinanciero.entity.type.EstadoGiro;

@Path("/giros")
public interface GiroREST {

    @GET
    @Produces({ "application/xml", "application/json" })
    public Response findAll(@QueryParam("estado") EstadoGiro[] estadosGiro);

    @GET
    @Path("/{id}")
    @Produces({ "application/xml", "application/json" })
    public Response findById(@PathParam("id") BigInteger id);

    @POST
    @Produces({ "application/xml", "application/json" })
    public Response create(Giro giro);

    @PUT
    @Path("/{id}")
    @Produces({ "application/xml", "application/json" })
    public Response update(@PathParam("id") BigInteger id, Giro giro);

    @GET
    @Path("/enviados")
    @Produces({ "application/json" })
    public Response getGirosEnviados(@QueryParam("idAgencia") BigInteger idAgencia,
            @QueryParam("estado") String estadoGiro, @QueryParam("filterText") String filterText,
            @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

    @GET
    @Path("/recibidos")
    @Produces({ "application/json" })
    public Response getGirosRecibidos(@QueryParam("idAgencia") BigInteger idAgencia,
            @QueryParam("estado") String estadoGiro, @QueryParam("filterText") String filterText,
            @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

}
