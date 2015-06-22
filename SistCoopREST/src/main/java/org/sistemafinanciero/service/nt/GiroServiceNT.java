package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Giro;

@Remote
public interface GiroServiceNT extends AbstractServiceNT<Giro> {

	public List<Giro> getGirosEnviados(BigInteger idAgencia, String filterText,
			Integer offset, Integer limit);

	public List<Giro> getGirosRecibidos(BigInteger idAgencia,
			String filterText, Integer offset, Integer limit);

}
