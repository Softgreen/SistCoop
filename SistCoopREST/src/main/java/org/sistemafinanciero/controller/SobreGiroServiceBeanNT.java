package org.sistemafinanciero.controller;

import java.math.BigInteger;
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
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Giro;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.SobreGiro;
import org.sistemafinanciero.service.nt.GiroServiceNT;
import org.sistemafinanciero.service.nt.SobreGiroServiceNT;

@Named
@Stateless
@Remote(SobreGiroServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SobreGiroServiceBeanNT implements SobreGiroServiceNT {

    @Inject
    private DAO<Object, SobreGiro> sobreGiroDAO;

    @Override
    public SobreGiro findById(BigInteger id) {
        // TODO Auto-generated method stub
        return null;
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

}
