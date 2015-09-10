package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.DebeHaber;
import org.sistemafinanciero.entity.type.TipoDebeHaber;
import org.sistemafinanciero.service.nt.ReportesServiceNT;
import org.sistemafinanciero.util.DateUtils;

@Named
@Stateless
@Remote(ReportesServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ReportesServiceBeanNT implements ReportesServiceNT {

    @Inject
    private DAO<Object, DebeHaber> debeHaberDAO;

    @Override
    public List<DebeHaber> getDebeHaber(Date fecha, BigInteger idMoneda, TipoDebeHaber tipoDebeHaber) {
        if (fecha == null) {
            return null;
        }
        QueryParameter queryParameter = QueryParameter.with("idMoneda", idMoneda).and("tipo", tipoDebeHaber)
                .and("desde", DateUtils.getDateIn00Time(fecha))
                .and("hasta", DateUtils.getDateIn00Time(DateUtils.sumarRestarDiasFecha(fecha, 1)));
        List<DebeHaber> list = debeHaberDAO.findByNamedQuery(DebeHaber.findDesdeHastaIdMonedaTipo,
                queryParameter.parameters());
        return list;
    }

}
