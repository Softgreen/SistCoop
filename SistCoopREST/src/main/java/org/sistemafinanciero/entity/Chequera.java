package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Accionista generated by hbm2java
 */
@Entity
@Table(name = "CHEQUERA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "chequera")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({ 
	@NamedQuery(name = Chequera.findChequeraByCuentaBancariaUltimo, query = "SELECT c FROM Chequera c WHERE c.cuentaBancaria.idCuentaBancaria = :idCuentaBancaria AND c.numeroFin = (SELECT MAX(cc.numeroFin) FROM Chequera cc WHERE cc.cuentaBancaria.idCuentaBancaria = :idCuentaBancaria)"),
	@NamedQuery(name = Chequera.findChequeraByEstado, query = "SELECT c FROM Chequera c WHERE c.cuentaBancaria.idCuentaBancaria = :idCuentaBancaria AND c.estado = :estado")})
public class Chequera implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static String findChequeraByCuentaBancariaUltimo = "findChequeraByCuentaBancariaUltimo";
	public final static String findChequeraByEstado = "findChequeraByEstado";

	private BigInteger idChequera;
	private Integer cantidad;
	private BigInteger numeroInicio;
	private BigInteger numeroFin;
	private Date fechaDisponible;
	private Date fechaExpiracion;

	private int estado;
	
	private CuentaBancaria cuentaBancaria;

	private Set cheques = new HashSet(0);
	
	public Chequera() {

	}

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="CHEQUERA_SEQ")
	@SequenceGenerator(name="CHEQUERA_SEQ", initialValue=1, allocationSize=1, sequenceName="CHEQUERA_SEQ")
	@XmlElement(name = "id")	
	@Id
	@Column(name = "ID_CHEQUERA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdChequera() {
		return idChequera;
	}

	public void setIdChequera(BigInteger idChequera) {
		this.idChequera = idChequera;
	}

	@XmlElement(name = "cantidad")
	@Column(name = "CANTIDAD", nullable = false)
	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	@XmlElement(name = "numeroInicio")
	@Column(name = "NUMERO_INICIO", nullable = false)
	public BigInteger getNumeroInicio() {
		return numeroInicio;
	}

	public void setNumeroInicio(BigInteger numeroInicio) {
		this.numeroInicio = numeroInicio;
	}

	@XmlElement(name = "numeroFin")
	@Column(name = "NUMERO_FIN", nullable = false)
	public BigInteger getNumeroFin() {
		return numeroFin;
	}

	public void setNumeroFin(BigInteger numeroFin) {
		this.numeroFin = numeroFin;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_DISPONIBLE", nullable = false)
	public Date getFechaDisponible() {
		return fechaDisponible;
	}

	public void setFechaDisponible(Date fechaDisponible) {
		this.fechaDisponible = fechaDisponible;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_EXPIRACION", nullable = false)
	public Date getFechaExpiracion() {
		return fechaExpiracion;
	}

	public void setFechaExpiracion(Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	@XmlElement
	@NotNull
	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return (this.estado == 1 ? true : false);
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1 : 0);
	}
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CUENTA_BANCARIA", nullable = false)
	public CuentaBancaria getCuentaBancaria() {
		return cuentaBancaria;
	}

	public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
		this.cuentaBancaria = cuentaBancaria;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chequera")
	public Set<Cheque> getCheques() {
		return cheques;
	}

	public void setCheques(Set cheques) {
		this.cheques = cheques;
	}

}
