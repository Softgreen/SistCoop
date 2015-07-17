package org.sistemafinanciero.controller;

import java.math.BigInteger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Validator;

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

    @Inject
    private Validator validator;

    @Override
    public BigInteger create(SobreGiro t) throws PreexistingEntityException, RollbackFailureException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(BigInteger id, SobreGiro t) throws NonexistentEntityException,
            PreexistingEntityException, RollbackFailureException {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(BigInteger id) throws NonexistentEntityException, RollbackFailureException {
        // TODO Auto-generated method stub

    }

}
