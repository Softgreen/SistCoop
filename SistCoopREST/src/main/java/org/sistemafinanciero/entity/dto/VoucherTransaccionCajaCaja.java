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
public class VoucherTransaccionCajaCaja implements java.io.Serializable {

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
	private String cajaOrigenDenominacion;
	private String cajaOrigenAbreviatura;
	private String cajaDestinoDenominacion;
	private String cajaDestinoAbrevitura;
	private BigDecimal saldoDisponibleOrigen;
	private BigDecimal saldoDisponibleDestino;
	private Moneda moneda;
	private BigDecimal monto;
	private String trabajadorCajaOrigen;
	private String trabajadorCajaDestino;
	private String agenciaDenominacion;
	private String agenciaAbreviatura;
	
	
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

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public BigDecimal getSaldoDisponibleDestino() {
		return saldoDisponibleDestino;
	}

	public void setSaldoDisponibleDestino(BigDecimal saldoDisponibleDestino) {
		this.saldoDisponibleDestino = saldoDisponibleDestino;
	}

	public BigDecimal getSaldoDisponibleOrigen() {
		return saldoDisponibleOrigen;
	}

	public void setSaldoDisponibleOrigen(BigDecimal saldoDisponibleOrigen) {
		this.saldoDisponibleOrigen = saldoDisponibleOrigen;
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

	public String getAgenciaAbreviatura() {
		return agenciaAbreviatura;
	}

	public void setAgenciaAbreviatura(String agenciaAbreviatura) {
		this.agenciaAbreviatura = agenciaAbreviatura;
	}

	public String getAgenciaDenominacion() {
		return agenciaDenominacion;
	}

	public void setAgenciaDenominacion(String agenciaDenominacion) {
		this.agenciaDenominacion = agenciaDenominacion;
	}

	public String getCajaOrigenDenominacion() {
		return cajaOrigenDenominacion;
	}

	public void setCajaOrigenDenominacion(String cajaOrigenDenominacion) {
		this.cajaOrigenDenominacion = cajaOrigenDenominacion;
	}

	public String getCajaOrigenAbreviatura() {
		return cajaOrigenAbreviatura;
	}

	public void setCajaOrigenAbreviatura(String cajaOrigenAbreviatura) {
		this.cajaOrigenAbreviatura = cajaOrigenAbreviatura;
	}

	public String getCajaDestinoDenominacion() {
		return cajaDestinoDenominacion;
	}

	public void setCajaDestinoDenominacion(String cajaDestinoDenominacion) {
		this.cajaDestinoDenominacion = cajaDestinoDenominacion;
	}

	public String getCajaDestinoAbrevitura() {
		return cajaDestinoAbrevitura;
	}

	public void setCajaDestinoAbrevitura(String cajaDestinoAbrevitura) {
		this.cajaDestinoAbrevitura = cajaDestinoAbrevitura;
	}

	public String getTrabajadorCajaOrigen() {
		return trabajadorCajaOrigen;
	}

	public void setTrabajadorCajaOrigen(String trabajadorCajaOrigen) {
		this.trabajadorCajaOrigen = trabajadorCajaOrigen;
	}

	public String getTrabajadorCajaDestino() {
		return trabajadorCajaDestino;
	}

	public void setTrabajadorCajaDestino(String trabajadorCajaDestino) {
		this.trabajadorCajaDestino = trabajadorCajaDestino;
	}
	
}
