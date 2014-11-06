package org.sistemafinanciero.service.ts;

import java.math.BigInteger;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface TrabajadorServiceTS extends AbstractServiceTS<Trabajador> {

	void desactivar(BigInteger id) throws NonexistentEntityException, RollbackFailureException;

}
