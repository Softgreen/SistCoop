package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.model.config.ConfigurationFactory;
import org.model.type.EstadoCuentaAporte;

/**
 * CuentaAporte generated by hbm2java
 */
@Entity
@Table(name = "CUENTA_APORTE", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "cuentaaporte")
@XmlAccessorType(XmlAccessType.NONE)
public class CuentaAporte implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long idCuentaAporte;
	private String numeroCuenta;
	private BigDecimal saldo;
	private Moneda moneda;
	private EstadoCuentaAporte estadoCuenta;

	private Set socios = new HashSet(0);

	public CuentaAporte() {
	}

	@XmlID
	@XmlElement(name = "id")
	@GeneratedValue
	@Id
	@Column(name = "ID_CUENTA_APORTE", unique = true, nullable = false)
	public Long getIdCuentaAporte() {
		return this.idCuentaAporte;
	}

	public void setIdCuentaAporte(Long idCuentaaporte) {
		this.idCuentaAporte = idCuentaaporte;
	}

	@XmlElement
	@NotNull
	@NotEmpty
	@NotBlank
	@Size(min = 14, max = 14)
	@NaturalId
	@Column(name = "NUMERO_CUENTA")
	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	@XmlElement
	@NotNull
	@Min(value = 0)
	@DecimalMin(value = "0")
	@Digits(integer = 18, fraction = 2)
	@Column(name = "SALDO")
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	@XmlElement(name = "moneda")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MONEDA", nullable = false, foreignKey = @ForeignKey)
	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	@XmlElement
	@NotNull
	@Type(type = ConfigurationFactory.estadoCuentaAporteType)
	@Column(name = "ESTADO_CUENTA", length = 12)
	public EstadoCuentaAporte getEstadoCuenta() {
		return this.estadoCuenta;
	}

	public void setEstadoCuenta(EstadoCuentaAporte estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cuentaAporte")
	public Set<Socio> getSocios() {
		return this.socios;
	}

	public void setSocios(Set socios) {
		this.socios = socios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeroCuenta == null) ? 0 : numeroCuenta.hashCode());
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
		CuentaAporte other = (CuentaAporte) obj;
		if (numeroCuenta == null) {
			if (other.numeroCuenta != null)
				return false;
		} else if (!numeroCuenta.equals(other.numeroCuenta))
			return false;
		return true;
	}

}
