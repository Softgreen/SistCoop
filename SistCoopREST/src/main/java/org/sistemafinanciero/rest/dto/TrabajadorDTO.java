package org.sistemafinanciero.rest.dto;

import java.io.Serializable;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TrabajadorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger id;

	private BigInteger idSucursal;
	private BigInteger idAgencia;
	private BigInteger idTipoDocumento;
	private String numeroDocumento;
	private String usuario;
	private Boolean estado;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(BigInteger idSucursal) {
		this.idSucursal = idSucursal;
	}

	public BigInteger getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(BigInteger idAgencia) {
		this.idAgencia = idAgencia;
	}

	public BigInteger getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(BigInteger idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

}
