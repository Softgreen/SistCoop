package org.sistemafinanciero.entity.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sistemafinanciero.entity.Moneda;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class VoucherTransaccionBovedaBoveda implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger id;
	private Boolean estadoConfirmacion;
	private Boolean estadoSolicitud;
	private Date fecha;
	private Date hora;
	private String observacion;

	private String bovedaOrigen;
	private String bovedaDestino;
	private String agenciaOrigen;
	private String agenciaDestino;

	private Moneda moneda;

	private BigDecimal monto;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Boolean getEstadoConfirmacion() {
		return estadoConfirmacion;
	}

	public void setEstadoConfirmacion(Boolean estadoConfirmacion) {
		this.estadoConfirmacion = estadoConfirmacion;
	}

	public Boolean getEstadoSolicitud() {
		return estadoSolicitud;
	}

	public void setEstadoSolicitud(Boolean estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getBovedaOrigen() {
		return bovedaOrigen;
	}

	public void setBovedaOrigen(String bovedaOrigen) {
		this.bovedaOrigen = bovedaOrigen;
	}

	public String getBovedaDestino() {
		return bovedaDestino;
	}

	public void setBovedaDestino(String bovedaDestino) {
		this.bovedaDestino = bovedaDestino;
	}

	public String getAgenciaOrigen() {
		return agenciaOrigen;
	}

	public void setAgenciaOrigen(String agenciaOrigen) {
		this.agenciaOrigen = agenciaOrigen;
	}

	public String getAgenciaDestino() {
		return agenciaDestino;
	}

	public void setAgenciaDestino(String agenciaDestino) {
		this.agenciaDestino = agenciaDestino;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

}
