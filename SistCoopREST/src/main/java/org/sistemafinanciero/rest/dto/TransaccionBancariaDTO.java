package org.sistemafinanciero.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "transaccionBancariaDTO")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TransaccionBancariaDTO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String numeroCuenta;
    private BigDecimal monto;
    private String referencia;

    // Este atributo es aplicable cuando se hce un SOBRE GIRO BANCARIO
    private BigDecimal interes;

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

}
