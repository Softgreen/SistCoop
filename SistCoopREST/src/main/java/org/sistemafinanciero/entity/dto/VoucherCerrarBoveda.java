package org.sistemafinanciero.entity.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sistemafinanciero.entity.Moneda;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class VoucherCerrarBoveda implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement
	private BigInteger idBoveda;

	@XmlElement
	private String boveda;

	@XmlElement
	private Date fechaApertura;

	@XmlElement
	private Date fechaCierre;

	@XmlElement
	private Date horaApertura;

	@XmlElement
	private Date horaCierre;

	@XmlElement
	private Moneda moneda;

	@XmlElement
	private String agenciaAbreviatura;

	@XmlElement
	private String agenciaDenominacion;

	@XmlElement
	private String trabajador;

	@XmlElement
	private BigDecimal TotalCierreBoveda;

	@XmlElement
	private TreeSet<GenericDetalle> detalle;

	public BigInteger getIdBoveda() {
		return idBoveda;
	}

	public void setIdBoveda(BigInteger idBoveda) {
		this.idBoveda = idBoveda;
	}

	public String getBoveda() {
		return boveda;
	}

	public void setBoveda(String boveda) {
		this.boveda = boveda;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Date getHoraApertura() {
		return horaApertura;
	}

	public void setHoraApertura(Date horaApertura) {
		this.horaApertura = horaApertura;
	}

	public Date getHoraCierre() {
		return horaCierre;
	}

	public void setHoraCierre(Date horaCierre) {
		this.horaCierre = horaCierre;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
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

	public String getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(String trabajador) {
		this.trabajador = trabajador;
	}

	public TreeSet<GenericDetalle> getDetalle() {
		return detalle;
	}

	public void setDetalle(TreeSet<GenericDetalle> detalle) {
		this.detalle = detalle;
	}

	public BigDecimal getTotalCierreBoveda() {
		return TotalCierreBoveda;
	}

	public void setTotalCierreBoveda(BigDecimal totalCierreBoveda) {
		TotalCierreBoveda = totalCierreBoveda;
	}
}
