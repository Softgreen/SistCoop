package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * DetalleTransaccionCaja generated by hbm2java
 */
@Entity
@Table(name = "DETALLE_TRANSACCION_CAJA", schema = "BDSISTEMAFINANCIERO")
public class DetalleTransaccionCaja implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idDetalleTransaccionCaja;
	private int cantidad;
	private BigDecimal valorMoneda;
	private BigInteger idOrigen;
	private String origenDetalle;

	public DetalleTransaccionCaja() {
	}

	@Id
	@Column(name = "ID_DETALLE_TRANSACCION_CAJA", unique = true, nullable = false)
	public BigInteger getIdDetalleTransaccionCaja() {
		return this.idDetalleTransaccionCaja;
	}

	public void setIdDetalleTransaccionCaja(BigInteger idDetalleTransaccionCaja) {
		this.idDetalleTransaccionCaja = idDetalleTransaccionCaja;
	}

	@Column(name = "CANTIDAD")
	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@NotNull
	@DecimalMin(value = "0")
	@Digits(integer = 6, fraction = 2)
	@Column(name = "VALOR_MONEDA")
	public BigDecimal getValorMoneda() {
		return this.valorMoneda;
	}

	public void setValorMoneda(BigDecimal valorMoneda) {
		this.valorMoneda = valorMoneda;
	}

	@Column(name = "ORIGEN_DETALLE", length = 40)
	public String getOrigenDetalle() {
		return this.origenDetalle;
	}

	public void setOrigenDetalle(String origenDetalle) {
		this.origenDetalle = origenDetalle;
	}

	@Column(name = "ID_ORIGEN")
	public BigInteger getIdOrigen() {
		return this.idOrigen;
	}

	public void setIdOrigen(BigInteger idOrigen) {
		this.idOrigen = idOrigen;
	}

}
