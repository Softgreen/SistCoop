package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;

/**
 * TransaccionBovedaOtro generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_BOVEDA_BOVEDA_VIEW", schema = "BDSISTEMAFINANCIERO")
@NamedQueries({ @NamedQuery(name = TransaccionBovedaBovedaView.findByIdAgenciaOrigen, query = "SELECT t FROM TransaccionBovedaBovedaView t WHERE t.agenciaIdOrigen = :idAgencia "), @NamedQuery(name = TransaccionBovedaBovedaView.findByIdAgenciaDestino, query = "SELECT t FROM TransaccionBovedaBovedaView t WHERE t.agenciaIdDestino = :idAgencia ") })
public class TransaccionBovedaBovedaView implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String findByIdAgenciaOrigen = "TransaccionBovedaBovedaView.findByIdAgenciaOrigen";
	public final static String findByIdAgenciaDestino = "TransaccionBovedaBovedaView.findByIdAgenciaDestino";

	private BigInteger idTransaccion;
	private String agenciaOrigenDenominacion;
	private String agenciaDestinoDenominacion;
	private BigInteger agenciaIdOrigen;
	private BigInteger agenciaIdDestino;
	private String bovedaOrigen;
	private String bovedaDestino;
	private String monedaDenominacion;
	private String monedaSimbolo;

	private Date fecha;
	private Date hora;
	private int estadoSolicitud;
	private int estadoConfirmacion;
	private BigDecimal saldoDisponibleOrigen;
	private BigDecimal saldoDisponibleDestino;
	private String observacion;
	private BigDecimal monto;

	public TransaccionBovedaBovedaView() {
	}

	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_TRANSACCION")
	public BigInteger getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(BigInteger idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	@XmlElement
	@Column(name = "AGENCIA_DENOMINACION_ORIGEN", length = 140, columnDefinition = "nvarchar2")
	public String getAgenciaOrigenDenominacion() {
		return agenciaOrigenDenominacion;
	}

	public void setAgenciaOrigenDenominacion(String agenciaOrigenDenominacion) {
		this.agenciaOrigenDenominacion = agenciaOrigenDenominacion;
	}

	@XmlElement
	@Column(name = "AGENCIA_DENOMINACION_DESTINO", length = 140, columnDefinition = "nvarchar2")
	public String getAgenciaDestinoDenominacion() {
		return agenciaDestinoDenominacion;
	}

	public void setAgenciaDestinoDenominacion(String agenciaDestinoDenominacion) {
		this.agenciaDestinoDenominacion = agenciaDestinoDenominacion;
	}

	@Column(name = "AGENCIA_ID_ORIGEN")
	public BigInteger getAgenciaIdOrigen() {
		return agenciaIdOrigen;
	}

	public void setAgenciaIdOrigen(BigInteger agenciaIdOrigen) {
		this.agenciaIdOrigen = agenciaIdOrigen;
	}

	@Column(name = "AGENCIA_ID_DESTINO")
	public BigInteger getAgenciaIdDestino() {
		return agenciaIdDestino;
	}

	public void setAgenciaIdDestino(BigInteger agenciaIdDestino) {
		this.agenciaIdDestino = agenciaIdDestino;
	}

	@Column(name = "BOVEDA_ORIGEN", length = 140, columnDefinition = "nvarchar2")
	public String getBovedaOrigen() {
		return bovedaOrigen;
	}

	public void setBovedaOrigen(String bovedaOrigen) {
		this.bovedaOrigen = bovedaOrigen;
	}

	@Column(name = "BOVEDA_DESTINO", length = 140, columnDefinition = "nvarchar2")
	public String getBovedaDestino() {
		return bovedaDestino;
	}

	public void setBovedaDestino(String bovedaDestino) {
		this.bovedaDestino = bovedaDestino;
	}

	@Column(name = "MONEDA_DENOMINACION", length = 140, columnDefinition = "nvarchar2")
	public String getMonedaDenominacion() {
		return monedaDenominacion;
	}

	public void setMonedaDenominacion(String monedaDenominacion) {
		this.monedaDenominacion = monedaDenominacion;
	}

	@Column(name = "MONEDA_SIMBOLO", length = 140, columnDefinition = "nvarchar2")
	public String getMonedaSimbolo() {
		return monedaSimbolo;
	}

	public void setMonedaSimbolo(String monedaSimbolo) {
		this.monedaSimbolo = monedaSimbolo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA", nullable = false, length = 7)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HORA", nullable = false, length = 7)
	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	@Column(name = "ESTADO_SOLICITUD")
	public boolean getEstadoSolicitud() {
		return (this.estadoSolicitud == 1 ? true : false);
	}

	public void setEstadoSolicitud(boolean estadoSolicitud) {
		this.estadoSolicitud = (estadoSolicitud ? 1 : 0);
	}

	@Column(name = "ESTADO_CONFIRMACION")
	public boolean getEstadoConfirmacion() {
		return (this.estadoConfirmacion == 1 ? true : false);
	}

	public void setEstadoConfirmacion(boolean estadoConfirmacion) {
		this.estadoConfirmacion = (estadoConfirmacion ? 1 : 0);
	}

	@Column(name = "SALDO_DISPONIBLE_ORIGEN")
	public BigDecimal getSaldoDisponibleOrigen() {
		return saldoDisponibleOrigen;
	}

	public void setSaldoDisponibleOrigen(BigDecimal saldoDisponibleOrigen) {
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
	}

	@Column(name = "SALDO_DISPONIBLE_DESTINO")
	public BigDecimal getSaldoDisponibleDestino() {
		return saldoDisponibleDestino;
	}

	public void setSaldoDisponibleDestino(BigDecimal saldoDisponibleDestino) {
		this.saldoDisponibleDestino = saldoDisponibleDestino;
	}

	@Column(name = "OBSERVACION", columnDefinition = "nvarchar2")
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Column(name = "MONTO")
	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

}