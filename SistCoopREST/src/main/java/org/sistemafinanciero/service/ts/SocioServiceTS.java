package org.sistemafinanciero.service.ts;

import java.math.BigInteger;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.SocioView;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface SocioServiceTS extends AbstractServiceTS<SocioView> {

	//public BigInteger create(BigInteger idAgencia, TipoPersona tipoPersona, BigInteger idDocSocio, String numDocSocio, BigInteger idDocApoderado, String numDocApoderado) throws RollbackFailureException;

	public void congelarCuentaAporte(BigInteger idSocio) throws RollbackFailureException;

	public void descongelarCuentaAporte(BigInteger idSocio) throws RollbackFailureException;

	public void inactivarSocio(BigInteger idSocio) throws RollbackFailureException;

	public void cambiarApoderado(BigInteger idSocio, BigInteger idPersonaNatural) throws RollbackFailureException;

	public void deleteApoderado(BigInteger idSocio) throws RollbackFailureException;

	public BigInteger addBeneficiario(BigInteger idSocio, Beneficiario beneficiario) throws RollbackFailureException;
	
	public void deleteBeneficiario(BigInteger idBeneficiario) throws NonexistentEntityException, RollbackFailureException;
	
	public void updateBeneficiario(BigInteger idBeneficiario, Beneficiario beneficiario) throws NonexistentEntityException, PreexistingEntityException, RollbackFailureException;
	
}
