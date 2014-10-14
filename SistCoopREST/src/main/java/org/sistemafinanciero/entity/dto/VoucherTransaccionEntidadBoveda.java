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
public class VoucherTransaccionEntidadBoveda implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean estado;
	private Date fecha;
	private Date hora;
	private BigInteger id;
	private String observacion;
	private String tipoTransaccion;
	private Moneda moneda;
	private BigDecimal monto;
	private String trabajador;

	private String agenciaAbreviatura;
	private String agenciaDenominacion;
	private String bovedaDenominacion;
	private String entidad;
	
	private String origenTransaccion;
	private String destinoTransaccion;

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
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

	public String getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(String trabajador) {
		this.trabajador = trabajador;
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

	public String getBovedaDenominacion() {
		return bovedaDenominacion;
	}

	public void setBovedaDenominacion(String cajaDenominacion) {
		this.bovedaDenominacion = cajaDenominacion;
	}

	public String getOrigenTransaccion() {
		return origenTransaccion;
	}

	public void setOrigenTransaccion(String origenTransaccion) {
		this.origenTransaccion = origenTransaccion;
	}

	public String getDestinoTransaccion() {
		return destinoTransaccion;
	}

	public void setDestinoTransaccion(String destinoTransaccion) {
		this.destinoTransaccion = destinoTransaccion;
	}

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

}
