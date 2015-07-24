package org.sistemafinanciero.service.nt;

import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.SobreGiro;
import org.sistemafinanciero.entity.type.EstadoSobreGiro;

@Remote
public interface SobreGiroServiceNT extends AbstractServiceNT<SobreGiro> {

    List<SobreGiro> findAll(EstadoSobreGiro[] estadosGiro, String filterText, Integer offset, Integer limit);

}
