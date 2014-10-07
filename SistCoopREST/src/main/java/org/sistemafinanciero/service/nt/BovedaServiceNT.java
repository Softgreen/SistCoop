package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.dto.GenericDetalle;

@Remote
public interface BovedaServiceNT extends AbstractServiceNT<Boveda> {

	public List<Boveda> findAll(BigInteger idAgencia);

	public Set<GenericDetalle> getDetallePenultimo(BigInteger idBoveda);

	public Set<GenericDetalle> getDetalle(BigInteger idBoveda);

	public Set<GenericDetalle> getDetalle(BigInteger idBoveda, BigInteger idHistorialBoveda);

	public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja(BigInteger idAgencia);

	public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja(BigInteger idAgencia);
}
