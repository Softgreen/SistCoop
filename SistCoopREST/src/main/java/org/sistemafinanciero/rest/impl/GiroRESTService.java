package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Giro;
import org.sistemafinanciero.entity.type.EstadoGiro;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.GiroREST;
import org.sistemafinanciero.service.nt.GiroServiceNT;
import org.sistemafinanciero.service.ts.GiroServiceTS;

public class GiroRESTService implements GiroREST {

    @EJB
    private GiroServiceNT giroServiceNT;

    @EJB
    private GiroServiceTS giroServiceTS;

    @Override
    public Response findAll(EstadoGiro[] estadosGiro) {
        return null;
    }

    @Override
    public Response findById(BigInteger id) {
        Giro result = giroServiceNT.findById(id);
        Response response = Response.status(Response.Status.OK).entity(result).build();
        return response;
    }

    @Override
    public Response create(Giro giro) {
        return null;
    }

    @Override
    public Response update(BigInteger id, Giro giro) {
        try {
            giroServiceTS.update(id, giro);
            Response response = Response.status(Response.Status.OK).build();
            return response;
        } catch (NonexistentEntityException e) {
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        } catch (PreexistingEntityException e) {
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        } catch (RollbackFailureException e) {
            Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            return response;
        }
    }

    @Override
    public Response getGirosEnviados(BigInteger idAgencia, String estadoGiro, String filterText,
            Integer offset, Integer limit) {
        EstadoGiro estado = null;
        if (estadoGiro != null) {
            estado = EstadoGiro.valueOf(estadoGiro.toUpperCase());
        }

        List<Giro> list = giroServiceNT.getGirosEnviados(idAgencia, estado, filterText, offset, limit);
        Response response = Response.status(Response.Status.OK).entity(list).build();
        return response;
    }

    @Override
    public Response getGirosRecibidos(BigInteger idAgencia, String estadoGiro, String filterText,
            Integer offset, Integer limit) {
        EstadoGiro estado = null;
        if (estadoGiro != null) {
            estado = EstadoGiro.valueOf(estadoGiro.toUpperCase());
        }

        List<Giro> list = giroServiceNT.getGirosRecibidos(idAgencia, estado, filterText, offset, limit);
        Response response = Response.status(Response.Status.OK).entity(list).build();
        return response;
    }

}
