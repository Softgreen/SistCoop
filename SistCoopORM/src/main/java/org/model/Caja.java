package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.model.config.ConfigurationFactory;

/**
 * Caja generated by hbm2java
 */
@Entity
@Table(name = "CAJA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "caja")
@XmlAccessorType(XmlAccessType.PROPERTY)
@NamedQueries({ @NamedQuery(name = Caja.findByUsername, query = "SELECT c FROM Caja c INNER JOIN c.trabajadorCajas tc INNER JOIN tc.trabajador t WHERE t.usuario = :username") })
public class Caja implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String findByUsername = "Caja.findByUsername";

	private Integer idCaja;
	private String denominacion;
	private String abreviatura;
	private boolean estado;
	private boolean abierto;
	private boolean estadoMovimiento;

	private Set trabajadorCajas = new HashSet(0);
	private Set bovedaCajas = new HashSet(0);
	private Set historialCajas = new HashSet(0);

	public Caja() {
	}

	@XmlID
	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_CAJA", unique = true, nullable = false)
	public Integer getIdCaja() {
		return this.idCaja;
	}

	public void setIdCaja(Integer idCaja) {
		this.idCaja = idCaja;
	}

	@XmlElement(name = "denominacion")
	@NotNull
	@Size(min = 0, max = 20)
	@NotBlank
	@NotEmpty
	@Column(name = "DENOMINACION", nullable = false)
	public String getDenominacion() {
		return this.denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@XmlElement(name = "abreviatura")
	@NotNull
	@Size(min = 0, max = 10)
	@NotBlank
	@NotEmpty
	@Column(name = "ABREVIATURA", nullable = false)
	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@XmlElement(name = "estado")
	@NotNull
	@Type(type = ConfigurationFactory.booleanType)
	@Column(name = "ESTADO", nullable = false)
	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	@XmlElement(name = "abierto")
	@NotNull
	@Type(type = ConfigurationFactory.booleanType)
	@Column(name = "ABIERTO", nullable = false)
	public boolean isAbierto() {
		return abierto;
	}

	public void setAbierto(boolean abierto) {
		this.abierto = abierto;
	}

	@XmlElement(name = "estadoMovimiento")
	@NotNull
	@Type(type = ConfigurationFactory.booleanType)
	@Column(name = "ESTADO_MOVIMIENTO", nullable = false)
	public boolean isEstadoMovimiento() {
		return estadoMovimiento;
	}

	public void setEstadoMovimiento(boolean estadoMovimiento) {
		this.estadoMovimiento = estadoMovimiento;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caja")
	public Set<TrabajadorCaja> getTrabajadorCajas() {
		return this.trabajadorCajas;
	}

	public void setTrabajadorCajas(Set trabajadorCajas) {
		this.trabajadorCajas = trabajadorCajas;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caja")
	public Set<BovedaCaja> getBovedaCajas() {
		return this.bovedaCajas;
	}

	public void setBovedaCajas(Set bovedaCajas) {
		this.bovedaCajas = bovedaCajas;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caja")
	public Set<HistorialCaja> getHistorialCajas() {
		return this.historialCajas;
	}

	public void setHistorialCajas(Set historialCajas) {
		this.historialCajas = historialCajas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCaja == null) ? 0 : idCaja.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Caja other = (Caja) obj;
		if (idCaja == null) {
			if (other.idCaja != null)
				return false;
		} else if (!idCaja.equals(other.idCaja))
			return false;
		return true;
	}

}
