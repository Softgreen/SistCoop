package org.sistemafinanciero.entity;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPersona;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * The persistent class for the CUENTA_BANCARIA_VIEW database table.
 * 
 */
@Entity
@Table(name = "CUENTA_BANCARIA_VIEW", schema = "C##BDSISTEMAFINANCIERO")
@XmlRootElement(name = "cuentabancariaview")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
		@NamedQuery(name = CuentaBancariaView.findByNumeroCuenta, query = "SELECT cbv FROM CuentaBancariaView cbv WHERE cbv.numeroCuenta = :numeroCuenta"),
		@NamedQuery(name = CuentaBancariaView.FindByFilterTextCuentaBancariaView, query = "SELECT cbv FROM CuentaBancariaView cbv WHERE cbv.tipoCuenta IN :tipoCuenta AND cbv.tipoPersona IN :tipoPersona AND cbv.estadoCuenta IN :tipoEstadoCuenta AND (cbv.numeroCuenta LIKE :filtertext OR cbv.numeroDocumento LIKE :filtertext OR UPPER(cbv.titulares) LIKE :filtertext)"),
		@NamedQuery(name = CuentaBancariaView.findByIdSocio, query = "SELECT cbv FROM CuentaBancariaView cbv WHERE cbv.idSocio = :idSocio")})
public class CuentaBancariaView implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String FindByFilterTextCuentaBancariaView = "CuentaBancariaView.FindByFilterTextCuentaBancariaView";
	public final static String findByNumeroCuenta = "CuentaBancariaView.findByNumeroCuenta";
	public final static String findByIdSocio = "CuentaBancariaView.findByIdSocio";

	private BigInteger idCuentaBancaria;
	private TipoCuentaBancaria tipoCuenta;
	private String numeroCuenta;	
	private EstadoCuentaBancaria estadoCuenta;
	private BigInteger idMoneda;
	private String moneda;
	private BigDecimal tasaInteres;
	private BigDecimal saldo;
	private int cantidadRetirantes;
	private Date fechaApertura;
	private Date fechaCierre;
	
	private BigInteger idSocio;
	private String socio;
	
	private TipoPersona tipoPersona;
	private BigInteger idTipoDocumento;
	private String tipoDocumento;
	private String numeroDocumento;
	
	private String titulares;
	
	public CuentaBancariaView() {
	}

	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_CUENTA_BANCARIA", unique = true, nullable = false)
	public BigInteger getIdCuentaBancaria() {
		return this.idCuentaBancaria;
	}

	public void setIdCuentaBancaria(BigInteger idCuentaBancaria) {
		this.idCuentaBancaria = idCuentaBancaria;
	}
	
	@XmlElement(name="tipoCuenta")	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_CUENTA", columnDefinition = "nvarchar2")
	public TipoCuentaBancaria getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(TipoCuentaBancaria tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	@XmlElement(name = "numeroCuenta")
	@Column(name = "NUMERO_CUENTA", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getNumeroCuenta() {
		return this.numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	@XmlElement(name = "estadoCuenta")
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_CUENTA", nullable = false, columnDefinition = "nvarchar2")
	public EstadoCuentaBancaria getEstadoCuenta() {
		return this.estadoCuenta;
	}

	public void setEstadoCuenta(EstadoCuentaBancaria estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}
	
	@XmlElement(name="idMoneda")
	@Column(name = "ID_MONEDA")
	public BigInteger getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(BigInteger idMoneda) {
		this.idMoneda = idMoneda;
	}
	
	@XmlElement(name = "moneda")
	@Column(name="MONEDA", columnDefinition="nvarchar2")
	public String getMoneda() {
		return this.moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	
	@XmlElement(name = "tasaInteres")
	@Column(name = "TASA_INTERES")
	public BigDecimal getTasaInteres() {
		return tasaInteres;
	}

	public void setTasaInteres(BigDecimal tasaInteres) {
		this.tasaInteres = tasaInteres;
	}
	
	@XmlElement(name = "saldo")
	@Column(name = "SALDO")
	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	@XmlElement(name="cantidadRetirantes")
	@Column(name = "CANTIDAD_RETIRANTES")
	public int getCantidadRetirantes() {
		return cantidadRetirantes;
	}

	public void setCantidadRetirantes(int cantidadRetirantes) {
		this.cantidadRetirantes = cantidadRetirantes;
	}
	
	@XmlElement(name = "fechaApertura")
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_APERTURA", nullable = false, length = 7)
	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	@XmlElement(name = "fechaCierre")
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CIERRE", nullable = false, length = 7)
	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}
	
	@XmlElement(name="idSocio")
	@Column(name = "ID_SOCIO")
	public BigInteger getIdSocio() {
		return idSocio;
	}

	public void setIdSocio(BigInteger idSocio) {
		this.idSocio = idSocio;
	}
	
	@XmlElement(name = "socio")
	@Column(name = "SOCIO", nullable = false, columnDefinition = "nvarchar2")
	public String getSocio() {
		return this.socio;
	}

	public void setSocio(String socio) {
		this.socio = socio;
	}

	@XmlElement(name = "tipoPersona")
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PERSONA", columnDefinition = "nvarchar2")
	public TipoPersona getTipoPersona() {
		return this.tipoPersona;
	}

	public void setTipoPersona(TipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	@XmlElement(name="idTipoDocumento")
	@Column(name = "ID_TIPO_DOCUMENTO")
	public BigInteger getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(BigInteger idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
	
	@XmlElement(name = "tipoDocumento")
	@Column(name = "TIPO_DOCUMENTO", nullable = false, length = 20, columnDefinition = "nvarchar2")
	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	
	@XmlElement(name = "numeroDocumento")
	@Column(name = "NUMERO_DOCUMENTO", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@XmlElement(name = "titulares")
	@Column(name = "TITULARES", nullable = false)
	public String getTitulares() {
		return titulares;
	}

	public void setTitulares(String titulares) {
		this.titulares = titulares;
	}

}