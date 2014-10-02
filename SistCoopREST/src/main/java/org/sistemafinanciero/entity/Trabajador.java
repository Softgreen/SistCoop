package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.sun.istack.NotNull;

/**
 * Trabajador generated by hbm2java
 */
@Entity
@Table(name = "TRABAJADOR", schema = "BDSISTEMAFINANCIERO")
@NamedQueries({ @NamedQuery(name = Trabajador.findByUsername, query = "SELECT t FROM Trabajador t WHERE t.usuario = :username"), 
	@NamedQuery(name = Trabajador.findByFilterText, query = "SELECT t FROM Trabajador t INNER JOIN t.personaNatural p WHERE CONCAT(p.apellidoPaterno,' ', p.apellidoMaterno,' ',p.nombres) LIKE :filterText"), 
	@NamedQuery(name = Trabajador.findByFilterTextAndIdAgencia, query = "SELECT t FROM Trabajador t INNER JOIN t.agencia a INNER JOIN t.personaNatural p WHERE a.idAgencia = :idAgencia AND CONCAT(p.apellidoPaterno,' ', p.apellidoMaterno,' ',p.nombres) LIKE :filterText"),
	@NamedQuery(name = Trabajador.findByIdPersonaAndEstado, query = "SELECT t FROM Trabajador t INNER JOIN t.personaNatural p WHERE p.idPersonaNatural = :idPersonaNatural AND t.estado = :estado"),
	@NamedQuery(name = Trabajador.findByUsuarioAndEstado, query = "SELECT t FROM Trabajador t WHERE t.usuario = :usuario AND t.estado = :estado ")})
public class Trabajador implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String findByUsername = "Trabajador.findByUsername";
	public final static String findByFilterText = "Trabajador.findByFilterText";
	public final static String findByFilterTextAndIdAgencia = "Trabajador.findByFilterTextAndIdAgencia";
	public final static String findByIdPersonaAndEstado = "Trabajador.findByIdPersonaAndEstado";
	public final static String findByUsuarioAndEstado = "Trabajador.findByUsuarioAndEstado";

	private BigInteger idTrabajador;
	private PersonaNatural personaNatural;
	private Agencia agencia;
	private int estado;
	private String usuario;
	private Set trabajadorCajas = new HashSet(0);

	public Trabajador() {
	}

	public Trabajador(BigInteger idTrabajador, PersonaNatural personaNatural, Agencia agencia, int estado) {
		this.idTrabajador = idTrabajador;
		this.personaNatural = personaNatural;
		this.agencia = agencia;
		this.estado = estado;
	}

	public Trabajador(BigInteger idTrabajador, PersonaNatural personaNatural, Agencia agencia, int estado, Set trabajadorCajas) {
		this.idTrabajador = idTrabajador;
		this.personaNatural = personaNatural;
		this.agencia = agencia;
		this.estado = estado;
		this.trabajadorCajas = trabajadorCajas;
	}

	@XmlElement(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_TRABAJADOR", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdTrabajador() {
		return this.idTrabajador;
	}

	public void setIdTrabajador(BigInteger idTrabajador) {
		this.idTrabajador = idTrabajador;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PERSONA_NATURAL", nullable = false)
	public PersonaNatural getPersonaNatural() {
		return this.personaNatural;
	}

	public void setPersonaNatural(PersonaNatural personaNatural) {
		this.personaNatural = personaNatural;
	}

	@XmlTransient
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_AGENCIA", nullable = false)
	public Agencia getAgencia() {
		return this.agencia;
	}

	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}

	@NotNull
	@Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
	public boolean getEstado() {
		return this.estado == 1;
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1 : 0);
	}

	@Column(name = "USUARIO", columnDefinition = "nvarchar2")
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "trabajador")
	public Set<TrabajadorCaja> getTrabajadorCajas() {
		return this.trabajadorCajas;
	}

	public void setTrabajadorCajas(Set trabajadorCajas) {
		this.trabajadorCajas = trabajadorCajas;
	}

}
