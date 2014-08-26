package org.model;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.NaturalId;

/**
 * Accionista generated by hbm2java
 */
@Entity
@Table(name = "ACCIONISTA", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "accionista")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Accionista implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long idAccionista;
	private PersonaNatural personaNatural;
	private PersonaJuridica personaJuridica;
	private BigDecimal porcentajeParticipacion;

	public Accionista() {

	}

	@XmlID
	@XmlElement(name = "id")
	@Id
	@GeneratedValue
	@Column(name = "ID_ACCIONISTA", unique = true, nullable = false)
	public Long getIdAccionista() {
		return this.idAccionista;
	}

	public void setIdAccionista(Long idAccionista) {
		this.idAccionista = idAccionista;
	}

	@XmlElement(name = "personaNatural")
	@NotNull
	@NaturalId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA_NATURAL", nullable = false, foreignKey = @ForeignKey)
	public PersonaNatural getPersonaNatural() {
		return this.personaNatural;
	}

	public void setPersonaNatural(PersonaNatural personaNatural) {
		this.personaNatural = personaNatural;
	}

	@XmlTransient
	@NotNull
	@NaturalId
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA_JURIDICA", nullable = false, foreignKey = @ForeignKey)
	public PersonaJuridica getPersonaJuridica() {
		return this.personaJuridica;
	}

	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}

	@XmlElement(name = "porcentajeParticipacion")
	@NotNull
	@Min(value = 0)
	@Max(value = 100)	
	@DecimalMin(value = "0")
	@DecimalMax(value = "100")
	@Digits(integer = 3, fraction = 2)
	@Column(name = "PORCENTAJE_PARTICIPACION", nullable = false)
	public BigDecimal getPorcentajeParticipacion() {
		return this.porcentajeParticipacion;
	}

	public void setPorcentajeParticipacion(BigDecimal porcentajeParticipacion) {
		this.porcentajeParticipacion = porcentajeParticipacion;
	}

}