package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.DebeHaber;
import org.sistemafinanciero.entity.type.TipoDebeHaber;
import org.sistemafinanciero.rest.ReportesRest;
import org.sistemafinanciero.service.nt.ReportesServiceNT;

public class ReportesRestService implements ReportesRest {

    @EJB
    private ReportesServiceNT reportesServiceNT;

    @Override
    public Response reporteDebeHaber(Long fecha, TipoDebeHaber tipoDebeHaber, BigInteger idMoneda) {
        Date fechaReporte;
        if (fecha == null) {
            fechaReporte = Calendar.getInstance().getTime();
        } else {
            fechaReporte = new Date(fecha);
        }
        List<DebeHaber> list = reportesServiceNT.getDebeHaber(fechaReporte, idMoneda, tipoDebeHaber);
        Response response = Response.status(Response.Status.OK).entity(list).build();
        return response;
    }

}
