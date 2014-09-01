package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Beneficiario generated by hbm2java
 */
@Entity
@Table(name = "BENEFICIARIO", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "beneficiario")
@XmlAccessorType(XmlAccessType.NONE)
public class Beneficiario implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private BigInteger idBeneficiario;
	private CuentaBancaria cuentaBancaria;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nombres;
	private String numeroDocumento;
	private int porcentajeBeneficio;
	
	private CuentaAporte cuentaAporte;

	public Beneficiario() {
	}

	public Beneficiario(BigInteger idBeneficiario, CuentaBancaria cuentaBancaria, int porcentajeBeneficio) {
		this.idBeneficiario = idBeneficiario;
		this.cuentaBancaria = cuentaBancaria;
		this.porcentajeBeneficio = porcentajeBeneficio;
	}

	public Beneficiario(BigInteger idBeneficiario, CuentaBancaria cuentaBancaria, String apellidoPaterno, String apellidoMaterno, String nombres, String numeroDocumento, int porcentajeBeneficio) {
		this.idBeneficiario = idBeneficiario;
		this.cuentaBancaria = cuentaBancaria;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.nombres = nombres;
		this.numeroDocumento = numeroDocumento;
		this.porcentajeBeneficio = porcentajeBeneficio;
	}

	@XmlElement(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_BENEFICIARIO", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdBeneficiario() {
		return this.idBeneficiario;
	}

	public void setIdBeneficiario(BigInteger idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_BANCARIA", nullable = false)
	public CuentaBancaria getCuentaBancaria() {
		return this.cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	@XmlElement(name = "apellidoPaterno")
	@NotNull
	@Size(min = 1, max = 70)
	@NotEmpty
	@NotBlank
	@Column(name = "APELLIDO_PATERNO", length = 140, columnDefinition = "nvarchar2")
	public String getApellidoPaterno() {
		return this.apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	@XmlElement(name = "apellidoMaterno")
	@NotNull
	@Size(min = 1, max = 70)
	@NotEmpty
	@NotBlank
	@Column(name = "APELLIDO_MATERNO", length = 140, columnDefinition = "nvarchar2")
	public String getApellidoMaterno() {
		return this.apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	@XmlElement(name = "nombres")
	@NotNull
	@Size(min = 1, max = 70)
	@NotEmpty
	@NotBlank
	@Column(name = "NOMBRES", length = 140, columnDefinition = "nvarchar2")
	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	@XmlElement
	@Size(min = 0, max = 70)
	@Column(name = "NUMERO_DOCUMENTO", length = 40, columnDefinition = "nvarchar2")
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@XmlElement
	@NotNull
	@Min(value = 0)
	@Max(value = 100)
	@Column(name = "PORCENTAJE_BENEFICIO", nullable = false, precision = 22, scale = 0)
	public int getPorcentajeBeneficio() {
		return this.porcentajeBeneficio;
	}

	public void setPorcentajeBeneficio(int porcentajeBeneficio) {
		this.porcentajeBeneficio = porcentajeBeneficio;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_APORTE")
	public CuentaAporte getCuentaAporte() {
		return this.cuentaAporte;
	}

	public void setCuentaAporte(CuentaAporte cuentaAporte) {
		this.cuentaAporte = cuentaAporte;
	}

}
