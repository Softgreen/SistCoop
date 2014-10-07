package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;

/**
 * TransaccionBovedaCaja generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_BOVEDA_CAJA_VIEW", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "transaccionbovedacaja")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
		@NamedQuery(name = TransaccionBovedaCajaView.findByHistorialCajaEnviados, query = "SELECT c FROM TransaccionBovedaCajaView c WHERE c.idHistorialCaja = :idHistorialCaja AND c.origen = :origen"),
		@NamedQuery(name = TransaccionBovedaCajaView.findByHistorialCajaRecibidos, query = "SELECT c FROM TransaccionBovedaCajaView c WHERE c.idHistorialCaja = :idHistorialCaja AND c.origen = :origen AND c.estadoConfirmacion = false AND c.estadoSolicitud = true"),
		@NamedQuery(name = TransaccionBovedaCajaView.findByAgenciaBovedaEnviados, query = "SELECT c FROM TransaccionBovedaCajaView c WHERE c.idAgencia = :idAgencia AND c.origen = :origen"),
		@NamedQuery(name = TransaccionBovedaCajaView.findByAgenciaBovedaRecibidos, query = "SELECT c FROM TransaccionBovedaCajaView c WHERE c.idAgencia = :idAgencia AND c.origen = :origen")})
public class TransaccionBovedaCajaView implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String findByHistorialCajaEnviados = "TransaccionBovedaCajaView.findByHistorialCajaEnviados";
	public final static String findByHistorialCajaRecibidos = "TransaccionBovedaCajaView.findByHistorialCajaRecibidos";
	public final static String findByAgenciaBovedaEnviados = "TransaccionBovedaCajaView.findByAgenciaBovedaEnviados";
	public final static String findByAgenciaBovedaRecibidos = "TransaccionBovedaCajaView.findByAgenciaBovedaRecibidos";

	private BigInteger idTransaccionBovedaCaja;
	private Date fecha;
	private Date hora;
	private BigDecimal monto;
	private int estadoSolicitud;
	private int estadoConfirmacion;
	private TransaccionBovedaCajaOrigen origen;
	private String observacion;
	private String boveda;
	private String caja;
	private String denominacionMoneda;
	private String simboloMoneda;

	private BigInteger idAgencia;
	private BigInteger idCaja;
	private BigInteger idBoveda;
	private BigInteger idHistorialCaja;
	private BigInteger idHistorialBoveda;

	public TransaccionBovedaCajaView() {
	}

	@XmlElement(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_TRANSACCION_BOVEDA_CAJA", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdTransaccionBovedaCaja() {
		return this.idTransaccionBovedaCaja;
	}

	public void setIdTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) {
		this.idTransaccionBovedaCaja = idTransaccionBovedaCaja;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA", nullable = false, length = 7)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@XmlElement
	@Column(name = "HORA", nullable = false)
	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	@XmlElement
	@Column(name = "OBSERVACION", length = 140, columnDefinition = "nvarchar2")
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@XmlElement
	@Column(name = "ESTADO_SOLICITUD", nullable = false, precision = 22, scale = 0)
	public boolean getEstadoSolicitud() {
		return (this.estadoSolicitud == 1 ? true : false);
	}

	public void setEstadoSolicitud(boolean estadoSolicitud) {
		this.estadoSolicitud = (estadoSolicitud ? 1 : 0);
	}

	@XmlElement
	@Column(name = "ESTADO_CONFIRMACION", nullable = false, precision = 22, scale = 0)
	public boolean getEstadoConfirmacion() {
		return (this.estadoConfirmacion == 1 ? true : false);
	}

	public void setEstadoConfirmacion(boolean estadoConfirmacion) {
		this.estadoConfirmacion = (estadoConfirmacion ? 1 : 0);
	}

	@XmlElement
	@Enumerated(value = EnumType.STRING)
	@Column(name = "ORIGEN", length = 12, columnDefinition = "nvarchar2")
	public TransaccionBovedaCajaOrigen getOrigen() {
		return this.origen;
	}

	public void setOrigen(TransaccionBovedaCajaOrigen origen) {
		this.origen = origen;
	}

	@XmlElement
	@Column(name = "MONTO")
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@XmlElement
	@Column(name = "BOVEDA", columnDefinition = "nvarchar2")
	public String getBoveda() {
		return boveda;
	}

	public void setBoveda(String boveda) {
		this.boveda = boveda;
	}

	@XmlElement
	@Column(name = "CAJA", columnDefinition = "nvarchar2")
	public String getCaja() {
		return caja;
	}

	public void setCaja(String caja) {
		this.caja = caja;
	}

	@XmlTransient
	@Column(name = "ID_CAJA")
	public BigInteger getIdCaja() {
		return idCaja;
	}

	public void setIdCaja(BigInteger idCaja) {
		this.idCaja = idCaja;
	}

	@XmlTransient
	@Column(name = "ID_BOVEDA")
	public BigInteger getIdBoveda() {
		return idBoveda;
	}

	public void setIdBoveda(BigInteger idBoveda) {
		this.idBoveda = idBoveda;
	}

	@XmlTransient
	@Column(name = "ID_HISTORIAL_CAJA")
	public BigInteger getIdHistorialCaja() {
		return idHistorialCaja;
	}

	public void setIdHistorialCaja(BigInteger idHistorialCaja) {
		this.idHistorialCaja = idHistorialCaja;
	}

	@XmlTransient
	@Column(name = "ID_HISTORIAL_BOVEDA")
	public BigInteger getIdHistorialBoveda() {
		return idHistorialBoveda;
	}

	public void setIdHistorialBoveda(BigInteger idHistorialBoveda) {
		this.idHistorialBoveda = idHistorialBoveda;
	}

	@XmlElement
	@Column(name = "ID_AGENCIA")
	public BigInteger getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(BigInteger idAgencia) {
		this.idAgencia = idAgencia;
	}

	@XmlElement
	@Column(name = "DENOMINACION_MONEDA", columnDefinition = "nvarchar2")
	public String getDenominacionMoneda() {
		return denominacionMoneda;
	}

	public void setDenominacionMoneda(String denominacionMoneda) {
		this.denominacionMoneda = denominacionMoneda;
	}

	@XmlElement
	@Column(name = "SIMBOLO_MONEDA", columnDefinition = "nvarchar2")
	public String getSimboloMoneda() {
		return simboloMoneda;
	}

	public void setSimboloMoneda(String simboloMoneda) {
		this.simboloMoneda = simboloMoneda;
	}

}
