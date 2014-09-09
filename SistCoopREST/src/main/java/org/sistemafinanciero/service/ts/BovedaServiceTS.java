package org.sistemafinanciero.service.ts;

import java.math.BigInteger;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface BovedaServiceTS extends AbstractServiceTS<Boveda> {

	public BigInteger abrir(BigInteger id) throws RollbackFailureException;

}
