package org.sistemafinanciero.rest.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cajaDTO")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class CajaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger id;
	private String denominacion;
	private String abreviatura;
	private boolean estado;
	private boolean abierto;
	private boolean estadoMovimiento;
	private List<BigInteger> bovedas;

	public CajaDTO() {
		// TODO Auto-generated constructor stub
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public boolean isAbierto() {
		return abierto;
	}

	public void setAbierto(boolean abierto) {
		this.abierto = abierto;
	}

	public boolean isEstadoMovimiento() {
		return estadoMovimiento;
	}

	public void setEstadoMovimiento(boolean estadoMovimiento) {
		this.estadoMovimiento = estadoMovimiento;
	}

	public List<BigInteger> getBovedas() {
		return bovedas;
	}

	public void setBovedas(List<BigInteger> bovedas) {
		this.bovedas = bovedas;
	}

}
