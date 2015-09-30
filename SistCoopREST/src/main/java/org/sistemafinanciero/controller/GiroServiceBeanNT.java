package org.sistemafinanciero.controller;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.Hibernate;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Giro;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.type.EstadoGiro;
import org.sistemafinanciero.service.nt.GiroServiceNT;

@Named
@Stateless
@Remote(GiroServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GiroServiceBeanNT implements GiroServiceNT {

    @Inject
    private DAO<Object, Giro> giroDAO;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Giro findById(BigInteger id) {
        Giro giro = giroDAO.find(id);
        Agencia agenciaOrigen = giro.getAgenciaOrigen();
        Agencia agenciaDestino = giro.getAgenciaDestino();
        Moneda moneda = giro.getMoneda();

        Hibernate.initialize(agenciaOrigen);
        Hibernate.initialize(agenciaDestino);
        Hibernate.initialize(moneda);

        return giroDAO.find(id);
    }

    @Override
    public List<Giro> findAll() {
        return giroDAO.findAll();
    }

    @Override
    public int count() {
        return giroDAO.count();
    }

    @Override
    public List<Giro> getGirosEnviados(BigInteger idAgencia, EstadoGiro estado, String filterText,
            Integer offset, Integer limit) {

        if (filterText == null)
            filterText = "";
        if (offset == null) {
            offset = 0;
        }
        offset = Math.abs(offset);
        if (limit != null) {
            limit = Math.abs(limit);
        }

        List<Giro> list = null;
        if (estado != null) {
            QueryParameter queryParameter = QueryParameter.with("idAgencia", idAgencia).and("estado", estado)
                    .and("filterText", '%' + filterText + '%');
            list = giroDAO.findByNamedQuery(Giro.findEnviadosByIdAgenciaEstadoFilterText,
                    queryParameter.parameters(), offset, limit);
        } else {
            QueryParameter queryParameter = QueryParameter.with("idAgencia", idAgencia).and("filterText",
                    '%' + filterText + '%');
            list = giroDAO.findByNamedQuery(Giro.findEnviadosByIdAgenciaFilterText,
                    queryParameter.parameters(), offset, limit);
        }

        for (Giro giro : list) {
            Moneda moneda = giro.getMoneda();
            Agencia agenciaOrigen = giro.getAgenciaOrigen();
            Agencia agenciaDestino = giro.getAgenciaDestino();
            Hibernate.initialize(moneda);
            Hibernate.initialize(agenciaOrigen);
            Hibernate.initialize(agenciaDestino);
        }
        return list;
    }

    @Override
    public List<Giro> getGirosRecibidos(BigInteger idAgencia, EstadoGiro estado, String filterText,
            Integer offset, Integer limit) {

        if (filterText == null)
            filterText = "";
        if (offset == null) {
            offset = 0;
        }
        offset = Math.abs(offset);
        if (limit != null) {
            limit = Math.abs(limit);
        }

        List<Giro> list = null;
        if (estado != null) {
            QueryParameter queryParameter = QueryParameter.with("idAgencia", idAgencia).and("estado", estado)
                    .and("filterText", '%' + filterText + '%');
            list = giroDAO.findByNamedQuery(Giro.findRecibidosByIdAgenciaEstadoFilterText,
                    queryParameter.parameters(), offset, limit);
        } else {
            QueryParameter queryParameter = QueryParameter.with("idAgencia", idAgencia).and("filterText",
                    '%' + filterText + '%');
            list = giroDAO.findByNamedQuery(Giro.findRecibidosByIdAgenciaFilterText,
                    queryParameter.parameters(), offset, limit);
        }

        for (Giro giro : list) {
            Moneda moneda = giro.getMoneda();
            Agencia agenciaOrigen = giro.getAgenciaOrigen();
            Agencia agenciaDestino = giro.getAgenciaDestino();
            Hibernate.initialize(moneda);
            Hibernate.initialize(agenciaOrigen);
            Hibernate.initialize(agenciaDestino);
        }
        return list;
    }

    @Override
    public int countEnviados(BigInteger idAgencia) {
        Query q = em.createNamedQuery(Giro.countEnviadosByIdAgencia);
        q.setParameter("idAgencia", idAgencia);
        int count = ((Long) q.getSingleResult()).intValue();
        return count;
    }

    @Override
    public int countRecibidos(BigInteger idAgencia) {
        Query q = em.createNamedQuery(Giro.countRecibidosByIdAgencia);
        q.setParameter("idAgencia", idAgencia);
        int count = ((Long) q.getSingleResult()).intValue();
        return count;
    }

}
