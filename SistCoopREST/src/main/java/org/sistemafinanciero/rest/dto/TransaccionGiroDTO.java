package org.sistemafinanciero.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.sistemafinanciero.entity.type.EstadoGiro;
import org.sistemafinanciero.entity.type.LugarPagoComision;

@XmlRootElement(name = "transaccionGiroDTO")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TransaccionGiroDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idAgenciaOrigen;
	private BigInteger idAgenciaDestino;

	private String tipoDocumento;
	private String cliente;

	private BigInteger idMoneda;
	private BigDecimal monto;
	private BigDecimal comision;
	private LugarPagoComision lugarPagoComision;
	private boolean estadoPagoComision;

	private EstadoGiro estado;

	public BigInteger getIdAgenciaOrigen() {
		return idAgenciaOrigen;
	}

	public void setIdAgenciaOrigen(BigInteger idAgenciaOrigen) {
		this.idAgenciaOrigen = idAgenciaOrigen;
	}

	public BigInteger getIdAgenciaDestino() {
		return idAgenciaDestino;
	}

	public void setIdAgenciaDestino(BigInteger idAgenciaDestino) {
		this.idAgenciaDestino = idAgenciaDestino;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public BigInteger getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(BigInteger idMoneda) {
		this.idMoneda = idMoneda;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getComision() {
		return comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}

	public LugarPagoComision getLugarPagoComision() {
		return lugarPagoComision;
	}

	public void setLugarPagoComision(LugarPagoComision lugarPagoComision) {
		this.lugarPagoComision = lugarPagoComision;
	}

	public boolean isEstadoPagoComision() {
		return estadoPagoComision;
	}

	public void setEstadoPagoComision(boolean estadoPagoComision) {
		this.estadoPagoComision = estadoPagoComision;
	}

	public EstadoGiro getEstado() {
		return estado;
	}

	public void setEstado(EstadoGiro estado) {
		this.estado = estado;
	}

}
