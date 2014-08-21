package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * Agencia generated by hbm2java
 */
@Entity
@Table(name = "AGENCIA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "agencia")
@XmlAccessorType(XmlAccessType.PROPERTY)
@NamedQueries({ @NamedQuery(name = Agencia.findByCodigo, query = "SELECT a FROM Agencia a WHERE a.codigo = :codigo") })
public class Agencia implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static String findByCodigo = "Agencia.findByCodigo";

	private Integer idAgencia;
	private String codigo;
	private String denominacion;
	private String abreviatura;
	private String ubigeo;
	private boolean estado;
	private Sucursal sucursal;

	private Set bovedas = new HashSet(0);
	private Set trabajadores = new HashSet(0);

	public Agencia() {
	}

	@XmlID
	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_AGENCIA", unique = true, nullable = false)
	public Integer getIdAgencia() {
		return this.idAgencia;
	}

	public void setIdAgencia(Integer idAgencia) {
		this.idAgencia = idAgencia;
	}

	@XmlElement(name = "codigo")
	@NotNull
	@Size(min = 1, max = 3)
	@NotEmpty
	@NotBlank
	@Column(name = "CODIGO", unique = true, nullable = false)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "denominacion")
	@NotNull
	@Size(min = 1, max = 100)
	@NotEmpty
	@NotBlank
	@Column(name = "DENOMINACION", unique = true, nullable = false)
	public String getDenominacion() {
		return this.denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	@XmlElement(name = "abreviatura")
	@NotNull
	@Size(min = 1, max = 10)
	@NotEmpty
	@NotBlank
	@Column(name = "ABREVIATURA", unique = true, nullable = false)
	public String getAbreviatura() {
		return this.abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@XmlElement(name = "ubigeo")
	@NotNull
	@Size(min = 1, max = 6)
	@NotEmpty
	@NotBlank
	@Column(name = "UBIGEO", nullable = false)
	public String getUbigeo() {
		return this.ubigeo;
	}

	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
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

	@XmlTransient
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SUCURSAL", nullable = false, foreignKey = @ForeignKey)
	public Sucursal getSucursal() {
		return this.sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "agencia")
	public Set<Boveda> getBovedas() {
		return this.bovedas;
	}

	public void setBovedas(Set bovedas) {
		this.bovedas = bovedas;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "agencia")
	public Set<Trabajador> getTrabajadores() {
		return this.trabajadores;
	}

	public void setTrabajadores(Set trabajadores) {
		this.trabajadores = trabajadores;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Agencia other = (Agencia) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
