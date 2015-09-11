package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.DebeHaber;
import org.sistemafinanciero.entity.type.TipoDebeHaber;

@Remote
public interface ReportesServiceNT {

    public List<DebeHaber> getDebeHaber(Date fecha, BigInteger idMoneda, TipoDebeHaber tipoDebeHaber);

    public List<DebeHaber> getDebeHaber(Date fecha, TipoDebeHaber tipoDebeHaber);

}
