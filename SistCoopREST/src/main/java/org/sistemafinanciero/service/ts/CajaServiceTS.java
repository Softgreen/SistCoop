package org.sistemafinanciero.service.ts;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface CajaServiceTS extends AbstractServiceTS<Caja> {

	public BigInteger create(Caja caja, List<BigInteger> idBovedas) throws RollbackFailureException;

	public void update(BigInteger id, Caja caja, List<BigInteger> idBovedas) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException;

	public void desactivar(BigInteger idCaja) throws RollbackFailureException;

	public BigInteger createTrabajadorCaja(BigInteger idCaja, BigInteger idTrabajador) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException;

	public void deleteTrabajadorCaja(BigInteger idCaja, BigInteger idTrabajador) throws NonexistentEntityException, RollbackFailureException;

	public BigInteger abrir(BigInteger idCaja) throws NonexistentEntityException, RollbackFailureException;

	public void congelar(BigInteger id) throws NonexistentEntityException, RollbackFailureException;;

	public void descongelar(BigInteger id) throws NonexistentEntityException, RollbackFailureException;

}
