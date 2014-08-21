package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

/**
 * HistorialBoveda generated by hbm2java
 */
@Entity
@Table(name = "HISTORIAL_BOVEDA", schema = "BDSISTEMAFINANCIERO")
@NamedQueries({ @NamedQuery(name = HistorialBoveda.findByHistorialActivo, query = "SELECT h FROM HistorialBoveda h WHERE h.boveda.idBoveda = :idboveda AND h.estado = true") })
public class HistorialBoveda implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String findByHistorialActivo = "HistorialBoveda.findByHistorialActivo";

	private BigInteger idHistorialBoveda;
	private Boveda boveda;
	private Date fechaApertura;
	private Date fechaCierre;
	private Date horaApertura;
	private Date horaCierre;
	private boolean estado;
	private Set transaccionBovedaCajas = new HashSet(0);
	private Set detalleHistorialBovedas = new HashSet(0);
	private Set transaccionBovedaOtros = new HashSet(0);

	public HistorialBoveda() {
	}

	@Id
	@Column(name = "ID_HISTORIAL_BOVEDA", unique = true, nullable = false)
	public BigInteger getIdHistorialBoveda() {
		return this.idHistorialBoveda;
	}

	public void setIdHistorialBoveda(BigInteger idHistorialBoveda) {
		this.idHistorialBoveda = idHistorialBoveda;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_BOVEDA", nullable = false, foreignKey = @ForeignKey)
	public Boveda getBoveda() {
		return this.boveda;
	}

	public void setBoveda(Boveda boveda) {
		this.boveda = boveda;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_APERTURA", nullable = false)
	public Date getFechaApertura() {
		return this.fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CIERRE")
	public Date getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	@NotNull
	@Column(name = "HORA_APERTURA", nullable = false)
	public Date getHoraApertura() {
		return this.horaApertura;
	}

	public void setHoraApertura(Date horaApertura) {
		this.horaApertura = horaApertura;
	}

	@Column(name = "HORA_CIERRE")
	public Date getHoraCierre() {
		return this.horaCierre;
	}

	public void setHoraCierre(Date horaCierre) {
		this.horaCierre = horaCierre;
	}

	@NotNull
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Column(name = "ESTADO", nullable = false)
	public boolean isEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialBoveda")
	public Set<TransaccionBovedaCaja> getTransaccionBovedaCajas() {
		return this.transaccionBovedaCajas;
	}

	public void setTransaccionBovedaCajas(Set transaccionBovedaCajas) {
		this.transaccionBovedaCajas = transaccionBovedaCajas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialBoveda")
	public Set<DetalleHistorialBoveda> getDetalleHistorialBovedas() {
		return this.detalleHistorialBovedas;
	}

	public void setDetalleHistorialBovedas(Set detalleHistorialBovedas) {
		this.detalleHistorialBovedas = detalleHistorialBovedas;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "historialBoveda")
	public Set<TransaccionBovedaOtro> getTransaccionBovedaOtros() {
		return this.transaccionBovedaOtros;
	}

	public void setTransaccionBovedaOtros(Set transaccionBovedaOtros) {
		this.transaccionBovedaOtros = transaccionBovedaOtros;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idHistorialBoveda == null) ? 0 : idHistorialBoveda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HistorialBoveda))
			return false;
		HistorialBoveda other = (HistorialBoveda) obj;
		if (idHistorialBoveda == null) {
			if (other.idHistorialBoveda != null)
				return false;
		} else if (!idHistorialBoveda.equals(other.idHistorialBoveda))
			return false;
		return true;
	}

}
