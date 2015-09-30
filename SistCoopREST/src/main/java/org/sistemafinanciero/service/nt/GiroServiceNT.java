package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Giro;
import org.sistemafinanciero.entity.type.EstadoGiro;

@Remote
public interface GiroServiceNT extends AbstractServiceNT<Giro> {

    public List<Giro> getGirosEnviados(BigInteger idAgencia, EstadoGiro estado, String filterText,
            Integer offset, Integer limit);

    public List<Giro> getGirosRecibidos(BigInteger idAgencia, EstadoGiro estado, String filterText,
            Integer offset, Integer limit);

    public int countEnviados(BigInteger idAgencia);

    public int countRecibidos(BigInteger idAgencia);

}
