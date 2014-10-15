package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Caja;

@Remote
public interface AgenciaServiceNT extends AbstractServiceNT<Agencia> {

	public Agencia findByCodigo(String codigo);

	public List<Agencia> findAll(Boolean estado);

	public Set<Caja> getCajasOfAgencia(BigInteger idAgencia);

}
