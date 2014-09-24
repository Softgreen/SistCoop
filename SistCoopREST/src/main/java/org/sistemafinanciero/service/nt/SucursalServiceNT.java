package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Sucursal;

@Remote
public interface SucursalServiceNT extends AbstractServiceNT<Sucursal> {

	List<Agencia> getAgencias(BigInteger idSucursal);
}
