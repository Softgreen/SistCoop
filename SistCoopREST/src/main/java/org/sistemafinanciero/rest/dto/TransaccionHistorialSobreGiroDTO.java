package org.sistemafinanciero.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transaccionHistorialSobreGiroDTO")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TransaccionHistorialSobreGiroDTO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private BigInteger idSobreGiro;
    private BigDecimal monto;

    public BigInteger getIdSobreGiro() {
        return idSobreGiro;
    }

    public void setIdSobreGiro(BigInteger idSobreGiro) {
        this.idSobreGiro = idSobreGiro;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

}
