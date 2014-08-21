package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TrabajadorCajaId generated by hbm2java
 */
@Embeddable
public class TrabajadorCajaId implements java.io.Serializable {

	private BigDecimal idTrabajador;
	private BigDecimal idCaja;

	public TrabajadorCajaId() {
	}

	public TrabajadorCajaId(BigDecimal idTrabajador, BigDecimal idCaja) {
		this.idTrabajador = idTrabajador;
		this.idCaja = idCaja;
	}

	@Column(name = "ID_TRABAJADOR", nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdTrabajador() {
		return this.idTrabajador;
	}

	public void setIdTrabajador(BigDecimal idTrabajador) {
		this.idTrabajador = idTrabajador;
	}

	@Column(name = "ID_CAJA", nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdCaja() {
		return this.idCaja;
	}

	public void setIdCaja(BigDecimal idCaja) {
		this.idCaja = idCaja;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TrabajadorCajaId))
			return false;
		TrabajadorCajaId castOther = (TrabajadorCajaId) other;

		return ((this.getIdTrabajador() == castOther.getIdTrabajador()) || (this
				.getIdTrabajador() != null
				&& castOther.getIdTrabajador() != null && this
				.getIdTrabajador().equals(castOther.getIdTrabajador())))
				&& ((this.getIdCaja() == castOther.getIdCaja()) || (this
						.getIdCaja() != null && castOther.getIdCaja() != null && this
						.getIdCaja().equals(castOther.getIdCaja())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getIdTrabajador() == null ? 0 : this.getIdTrabajador()
						.hashCode());
		result = 37 * result
				+ (getIdCaja() == null ? 0 : this.getIdCaja().hashCode());
		return result;
	}

}
