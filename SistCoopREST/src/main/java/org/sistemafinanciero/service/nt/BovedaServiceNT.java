package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.dto.GenericDetalle;

@Remote
public interface BovedaServiceNT extends AbstractServiceNT<Boveda> {

	public List<Boveda> findAll(BigInteger idAgencia);
	
	public Set<GenericDetalle> getDetalleBoveda(BigInteger idBoveda);

	public Set<GenericDetalle> getDetalleBoveda(BigInteger idBoveda, BigInteger idHistorialBoveda);
	
}
