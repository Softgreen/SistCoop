package org.sistemafinanciero.controller;

import java.math.BigInteger;

import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.entity.SobreGiro;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.SobreGiroServiceTS;

@Named
@Stateless
@Remote(SobreGiroServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SobreGiroServiceBeanTS implements SobreGiroServiceTS {

    @Inject
    private DAO<Object, SobreGiro> sobreGiroDAO;

    @Override
    public BigInteger create(SobreGiro t) throws PreexistingEntityException, RollbackFailureException {
        throw new EJBException();
    }

    @Override
    public void update(BigInteger id, SobreGiro t) throws NonexistentEntityException,
            PreexistingEntityException, RollbackFailureException {
        throw new EJBException();
    }

    @Override
    public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
        throw new EJBException();
    }

}
