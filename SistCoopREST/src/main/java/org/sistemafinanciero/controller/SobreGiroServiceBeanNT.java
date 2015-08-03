package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Hibernate;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.HistorialPagoSobreGiro;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PersonaJuridica;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.SobreGiro;
import org.sistemafinanciero.entity.Socio;
import org.sistemafinanciero.entity.TipoDocumento;
import org.sistemafinanciero.entity.type.EstadoSobreGiro;
import org.sistemafinanciero.service.nt.SobreGiroServiceNT;

@Named
@Stateless
@Remote(SobreGiroServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SobreGiroServiceBeanNT implements SobreGiroServiceNT {

    @Inject
    private DAO<Object, SobreGiro> sobreGiroDAO;

    @Inject
    private DAO<Object, HistorialPagoSobreGiro> historialPagoSobreGiroDAO;

    @Override
    public SobreGiro findById(BigInteger id) {
        SobreGiro sobreGiro = sobreGiroDAO.find(id);

        Socio socio = sobreGiro.getSocio();
        PersonaNatural pn = socio.getPersonaNatural();
        PersonaJuridica pj = socio.getPersonaJuridica();
        Moneda moneda = sobreGiro.getMoneda();

        TipoDocumento tipoDocumento = pn != null ? pn.getTipoDocumento() : pj.getTipoDocumento();

        Hibernate.initialize(socio);
        Hibernate.initialize(pn);
        Hibernate.initialize(pj);
        Hibernate.initialize(moneda);
        Hibernate.initialize(tipoDocumento);

        return sobreGiro;
    }

    @Override
    public List<SobreGiro> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int count() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<SobreGiro> findAll(EstadoSobreGiro[] estadosGiro, String filterText, Integer offset,
            Integer limit) {

        if (filterText == null)
            filterText = "";
        if (offset == null) {
            offset = 0;
        }
        offset = Math.abs(offset);
        if (limit != null) {
            limit = Math.abs(limit);
        }
        Integer offSetInteger = offset.intValue();
        Integer limitInteger = (limit != null ? limit.intValue() : null);

        List<EstadoSobreGiro> estados = Arrays.asList(estadosGiro);

        QueryParameter queryParameter = QueryParameter.with("filterText",
                '%' + filterText.toUpperCase() + '%').and("estados", EnumSet.copyOf(estados));

        List<SobreGiro> resultPN = sobreGiroDAO.findByNamedQuery(SobreGiro.FindByFilterTextPN,
                queryParameter.parameters(), offSetInteger, limitInteger);

        List<SobreGiro> resultPJ = sobreGiroDAO.findByNamedQuery(SobreGiro.FindByFilterTextPJ,
                queryParameter.parameters(), offSetInteger, limitInteger);

        resultPN.addAll(resultPJ);

        for (SobreGiro sobreGiro : resultPN) {
            Socio socio = sobreGiro.getSocio();
            PersonaNatural pn = socio.getPersonaNatural();
            PersonaJuridica pj = socio.getPersonaJuridica();
            Moneda moneda = sobreGiro.getMoneda();

            TipoDocumento tipoDocumento = pn != null ? pn.getTipoDocumento() : pj.getTipoDocumento();

            Hibernate.initialize(socio);
            Hibernate.initialize(pn);
            Hibernate.initialize(pj);
            Hibernate.initialize(moneda);
            Hibernate.initialize(tipoDocumento);
        }
        return resultPN;
    }

    @Override
    public List<HistorialPagoSobreGiro> findHistorialPagoById(BigInteger idSobreGiro) {
        QueryParameter parameters = QueryParameter.with("idSobreGiro", idSobreGiro);
        List<HistorialPagoSobreGiro> list = historialPagoSobreGiroDAO.findByNamedQuery(
                HistorialPagoSobreGiro.findByIdSobreGiro, parameters.parameters());

        for (HistorialPagoSobreGiro historialPagoSobreGiro : list) {
            SobreGiro sobreGiro = historialPagoSobreGiro.getSobreGiro();
            Socio socio = sobreGiro.getSocio();
            PersonaNatural pn = socio.getPersonaNatural();
            PersonaJuridica pj = socio.getPersonaJuridica();
            Moneda moneda = sobreGiro.getMoneda();

            TipoDocumento tipoDocumento = pn != null ? pn.getTipoDocumento() : pj.getTipoDocumento();

            Hibernate.initialize(socio);
            Hibernate.initialize(pn);
            Hibernate.initialize(pj);
            Hibernate.initialize(moneda);
            Hibernate.initialize(tipoDocumento);
        }

        return list;
    }
}
