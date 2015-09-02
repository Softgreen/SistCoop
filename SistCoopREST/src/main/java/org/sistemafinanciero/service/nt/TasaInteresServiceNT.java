package org.sistemafinanciero.service.nt;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.ejb.Remote;

@Remote
public interface TasaInteresServiceNT extends AbstractServiceNT<TasaInteresServiceNT> {

	public BigDecimal getTasaInteresCuentaPlazoFijo(BigInteger idMoneda, int periodo, BigDecimal monto);

	public BigDecimal getTasaInteresCuentaLibre(BigInteger idMoneda);

	public BigDecimal getTasaInteresCuentaRecaudadora(BigInteger idMoneda);

	public BigDecimal getInteresGenerado(BigDecimal monto, int periodo, BigDecimal tasaInteres);

}
