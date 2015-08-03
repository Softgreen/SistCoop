package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.HistorialPagoSobreGiro;
import org.sistemafinanciero.entity.SobreGiro;
import org.sistemafinanciero.entity.type.EstadoSobreGiro;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.SobreGiroREST;
import org.sistemafinanciero.service.nt.SobreGiroServiceNT;
import org.sistemafinanciero.service.ts.SobreGiroServiceTS;

public class SobreGiroRESTService implements SobreGiroREST {

    @EJB
    private SobreGiroServiceNT sobreGiroServiceNT;

    @EJB
    private SobreGiroServiceTS sobreGiroServiceTS;

    @Override
    public Response findAll(EstadoSobreGiro[] estadosGiro, String filterText, Integer offset, Integer limit) {
        if (estadosGiro == null) {
            estadosGiro = new EstadoSobreGiro[0];
            estadosGiro[0] = EstadoSobreGiro.ACTIVO;
        }
        List<SobreGiro> list = sobreGiroServiceNT.findAll(estadosGiro, filterText, offset, limit);
        Response response = Response.status(Response.Status.OK).entity(list).build();
        return response;
    }

    @Override
    public Response findById(BigInteger id) {
        SobreGiro sobreGiro = sobreGiroServiceNT.findById(id);
        return Response.status(Response.Status.OK).entity(sobreGiro).build();
    }

    @Override
    public Response create(SobreGiro sobreGiro) {
        throw new BadRequestException();
    }

    @Override
    public Response update(BigInteger id, SobreGiro sobreGiro) {
        throw new BadRequestException();
    }

    @Override
    public Response getHistorial(BigInteger id) {
        List<HistorialPagoSobreGiro> historiales = sobreGiroServiceNT.findHistorialPagoById(id);
        return Response.status(Response.Status.OK).entity(historiales).build();
    }   

}
