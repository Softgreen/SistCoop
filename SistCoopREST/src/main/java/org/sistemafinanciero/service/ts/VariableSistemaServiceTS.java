package org.sistemafinanciero.service.ts;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.VariableSistema;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface VariableSistemaServiceTS extends
		AbstractServiceTS<VariableSistema> {

	public void setTasaCambio(BigInteger idMonedaRecibida,
			BigInteger idMonedaEntregada, BigDecimal tasa)
			throws NonexistentEntityException, RollbackFailureException;

}
