package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Sucursal generated by hbm2java
 */
@Entity
@Table(name = "SUCURSAL", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Sucursal implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idSucursal;
	private String denominacion;
	private String abreviatura;
	private int estado;
	private Set agencias = new HashSet(0);

	public Sucursal() {
	}

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_SUCURSAL", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdSucursal() {
		return this.idSucursal;
	}

	public void setIdSucursal(BigInteger idSucursal) {
		this.idSucursal = idSucursal;
	}

	@Column(name = "DENOMINACION", nullable = false, length = 100, columnDefinition = "nvarchar2")
	public String getDenominacion() {
		return this.denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@Column(name = "ABREVIATURA", length = 20, columnDefinition = "nvarchar2")
	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return this.estado == 1;
	}

	public void setEstado(boolean estado) {
		this.estado = (estado == true ? 1 : 0);
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sucursal")
	public Set<Agencia> getAgencias() {
		return this.agencias;
	}

	public void setAgencias(Set agencias) {
		this.agencias = agencias;
	}

}
