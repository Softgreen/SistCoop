package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.model.type.TipoPendiente;

@Entity
@Table(name = "PENDIENTE_CAJA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "pendientecaja")
@XmlAccessorType(XmlAccessType.NONE)
public class PendienteCaja implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger idPendienteCaja;
	private HistorialCaja historialCaja;
	private Moneda moneda;
	private BigDecimal monto;
	private String observacion;
	private Date fecha;
	private Date hora;
	private TipoPendiente tipoPendiente;
	private String trabajador;

	public PendienteCaja() {
	}

	@XmlID
	@XmlElement(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_PENDIENTE_CAJA", unique = true, nullable = false)
	public BigInteger getIdPendienteCaja() {
		return this.idPendienteCaja;
	}

	public void setIdPendienteCaja(BigInteger idPendienteCaja) {
		this.idPendienteCaja = idPendienteCaja;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_HISTORIAL_CAJA", nullable = false, foreignKey = @ForeignKey)
	public HistorialCaja getHistorialCaja() {
		return this.historialCaja;
	}

	public void setHistorialCaja(HistorialCaja historialCaja) {
		this.historialCaja = historialCaja;
	}

	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MONEDA", nullable = false, foreignKey = @ForeignKey)
	public Moneda getMoneda() {
		return this.moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	@XmlElement
	@NotNull
	@DecimalMin(value = "0")
	@Digits(integer = 18, fraction = 2)
	@Column(name = "MONTO", nullable = false, precision = 18)
	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	@XmlElement
	@Size(min = 0, max = 40)
	@Column(name = "OBSERVACION", length = 40)
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@XmlElement
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA", nullable = false)
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@XmlElement
	@NotNull
	@Column(name = "HORA", nullable = false)
	public Date getHora() {
		return this.hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	@XmlElement
	@Enumerated(value = EnumType.STRING)
	@Column(name = "TIPO_PENDIENTE", nullable = false, length = 40)
	public TipoPendiente getTipoPendiente() {
		return this.tipoPendiente;
	}

	public void setTipoPendiente(TipoPendiente tipoPendiente) {
		this.tipoPendiente = tipoPendiente;
	}

	@XmlElement
	@Column(name = "TRABAJADOR", nullable = false)
	public String getTrabajador() {
		return this.trabajador;
	}

	public void setTrabajador(String trabajador) {
		this.trabajador = trabajador;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPendienteCaja == null) ? 0 : idPendienteCaja.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PendienteCaja))
			return false;
		PendienteCaja other = (PendienteCaja) obj;
		if (idPendienteCaja == null) {
			if (other.idPendienteCaja != null)
				return false;
		} else if (!idPendienteCaja.equals(other.idPendienteCaja))
			return false;
		return true;
	}
}
