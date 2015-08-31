package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;

/**
 * TransaccionCompraVenta generated by hbm2java
 */
@Entity
@Table(name = "TRANSACCION_CHEQUE", schema = "C##BDSISTEMAFINANCIERO")
public class TransaccionCheque implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idTransaccionCheque;

	private Date fecha;
	private Date hora;
	private BigInteger numeroOperacion;
	private BigDecimal monto;

	private String observacion;
	private String tipoDocumento;
	private String numeroDocumento;
	private String persona;
	private int estado;

	private BigDecimal saldoDisponible;
	private String trabajador;

	private HistorialCaja historialCaja;
	private Cheque cheque;

	public TransaccionCheque() {
	}

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia_transaccion_cheque")
	@SequenceGenerator(name = "secuencia_transaccion_cheque", initialValue = 1, allocationSize = 1, sequenceName = "TRANSACCION_SEQUENCE")
	@Id
	@Column(name = "ID_TRANSACCION_CHEQUE", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdTransaccionCheque() {
		return this.idTransaccionCheque;
	}

	public void setIdTransaccionCheque(BigInteger idTransaccionCheque) {
		this.idTransaccionCheque = idTransaccionCheque;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA", nullable = false, length = 7)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "HORA", nullable = false)
	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	@Column(name = "NUMERO_OPERACION", nullable = false, precision = 22, scale = 0)
	public BigInteger getNumeroOperacion() {
		return this.numeroOperacion;
	}

	public void setNumeroOperacion(BigInteger numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	@Column(name = "MONTO", nullable = false, precision = 18)
	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@Column(name = "OBSERVACION", length = 100, columnDefinition = "nvarchar2")
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Column(name = "TRABAJADOR", length = 130, columnDefinition = "nvarchar2")
	public String getTrabajador() {
		return trabajador;
	}

	public void setTrabajador(String trabajador) {
		this.trabajador = trabajador;
	}

	@Column(name = "TIPO_DOCUMENTO", length = 100, columnDefinition = "nvarchar2")
	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Column(name = "NUMERO_DOCUMENTO", length = 100, columnDefinition = "nvarchar2")
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@Column(name = "PERSONA", length = 100, columnDefinition = "nvarchar2")
	public String getPersona() {
		return this.persona;
	}

	public void setPersona(String persona) {
		this.persona = persona;
	}

	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return (this.estado == 1 ? true : false);
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1 : 0);
	}

	@XmlElement
	@Column(name = "SALDO_DISPONIBLE", nullable = false, precision = 18)
	public BigDecimal getSaldoDisponible() {
		return this.saldoDisponible;
	}

	public void setSaldoDisponible(BigDecimal saldoDisponible) {
		this.saldoDisponible = saldoDisponible;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HISTORIAL_CAJA", nullable = false)
	public HistorialCaja getHistorialCaja() {
		return this.historialCaja;
	}

	public void setHistorialCaja(HistorialCaja historialCaja) {
		this.historialCaja = historialCaja;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CHEQUE", nullable = false)
	public Cheque getCheque() {
		return this.cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
	}

}
