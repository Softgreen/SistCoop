package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;

/**
 * CuentaBancariaTasa generated by hbm2java
 */
@Entity
@Table(name = "CUENTA_BANCARIA_TASA", schema = "BDSISTEMAFINANCIERO")
public class CuentaBancariaTasa implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal idCuentaBancariaTasa;
	private CuentaBancaria cuentaBancaria;
	private BigDecimal valor;

	public CuentaBancariaTasa() {
	}

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_CUENTA_BANCARIA_TASA", unique = true, nullable = false)
	public BigDecimal getIdCuentaBancariaTasa() {
		return this.idCuentaBancariaTasa;
	}

	public void setIdCuentaBancariaTasa(BigDecimal idCuentaBancariaTasa) {
		this.idCuentaBancariaTasa = idCuentaBancariaTasa;
	}

	@NotNull
	@NaturalId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_BANCARIA", foreignKey = @ForeignKey)
	public CuentaBancaria getCuentaBancaria() {
		return this.cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	// precision = 5, scale = 4
	@NotNull
	@DecimalMin(value = "0")
	@Digits(integer = 1, fraction = 3)
	@Column(name = "VALOR")
	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}