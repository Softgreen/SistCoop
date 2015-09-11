package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.type.TipoDebeHaber;

@Path("/reportes")
public interface ReportesRest {

    @GET
    @Path("/debeHaber")
    @Produces({ "application/xml", "application/json" })
    public Response reporteDebeHaber(@QueryParam("fecha") Long fecha,
            @QueryParam("tipo") TipoDebeHaber tipoDebeHaber, @QueryParam("idMoneda") BigInteger idMoneda);

    @GET
    @Path("/debeHaber/pdf")
    @Produces({ "application/xml", "application/json" })
    public Response reporteDebeHaberPdf(@QueryParam("fecha") Long fecha);

}
