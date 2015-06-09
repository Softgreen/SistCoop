package org.sistemafinanciero.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "tasaCambioDTO")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TasaCambioDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idMonedaRecibida;
	private BigInteger idMonedaEntregada;
	private BigDecimal tasa;

	public BigInteger getIdMonedaRecibida() {
		return idMonedaRecibida;
	}

	public void setIdMonedaRecibida(BigInteger idMonedaRecibida) {
		this.idMonedaRecibida = idMonedaRecibida;
	}

	public BigInteger getIdMonedaEntregada() {
		return idMonedaEntregada;
	}

	public void setIdMonedaEntregada(BigInteger idMonedaEntregada) {
		this.idMonedaEntregada = idMonedaEntregada;
	}

	public BigDecimal getTasa() {
		return tasa;
	}

	public void setTasa(BigDecimal tasa) {
		this.tasa = tasa;
	}

}
