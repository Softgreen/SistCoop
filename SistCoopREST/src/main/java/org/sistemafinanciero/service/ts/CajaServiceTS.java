package org.sistemafinanciero.service.ts;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface CajaServiceTS extends AbstractServiceTS<Caja> {

	public BigInteger create(Caja caja, List<BigInteger> idBovedas) throws RollbackFailureException;

	public void desactivar(BigInteger idCaja) throws RollbackFailureException;

}
