package org.sistemafinanciero.service.ts;

import java.math.BigInteger;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Sucursal;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface SucursalServiceTS extends AbstractServiceTS<Sucursal> {

	public BigInteger createAgencia(BigInteger idSucursal, Agencia agencia) throws NonexistentEntityException, RollbackFailureException;
	
	public void updateAgencia(BigInteger idSucursal, BigInteger idAgencia, Agencia agencia) throws NonexistentEntityException, RollbackFailureException;
}
