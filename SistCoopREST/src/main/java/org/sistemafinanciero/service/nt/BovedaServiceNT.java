package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Boveda;

@Remote
public interface BovedaServiceNT extends AbstractServiceNT<Boveda> {

	public List<Boveda> findAll(BigInteger idAgencia);
}
