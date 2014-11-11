package org.sistemafinanciero.service.ts;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.type.TransaccionEntidadBovedaOrigen;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;

@Remote
public interface BovedaServiceTS extends AbstractServiceTS<Boveda> {

	public BigInteger abrir(BigInteger id) throws RollbackFailureException;

	public BigInteger cerrar(BigInteger id) throws RollbackFailureException;

	public void congelar(BigInteger id) throws RollbackFailureException;

	public void descongelar(BigInteger id) throws RollbackFailureException;

	public BigInteger crearTransaccionEntidadBoveda(TransaccionEntidadBovedaOrigen origen, Set<GenericDetalle> detalleTransaccion, BigInteger idEntidad, BigInteger idBoveda, String observacion) throws NonexistentEntityException, RollbackFailureException;

	public BigInteger crearTransaccionBovedaBoveda(BigInteger idBovedaOrigen, BigInteger idBovedaDestino, Set<GenericDetalle> detalleTransaccion) throws NonexistentEntityException, RollbackFailureException;

	public void confirmarTransaccionBovedaBoveda(BigInteger id) throws RollbackFailureException;

	public void cancelarTransaccionBovedaBoveda(BigInteger id) throws RollbackFailureException;
}
