package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.SobreGiro;
import org.sistemafinanciero.entity.type.EstadoSobreGiro;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response create(SobreGiro sobreGiro) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Response update(BigInteger id, SobreGiro sobreGiro) {
        // TODO Auto-generated method stub
        return null;
    }

}