package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.sistemafinanciero.entity.type.Sexo;

/**
 * PersonaNatural generated by hbm2java
 */
@Entity
@Table(name = "PERSONA_NATURAL", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "personanatural")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({ @NamedQuery(name = PersonaNatural.FindAll, query = "SELECT p FROM PersonaNatural p ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombres, p.idPersonaNatural"), @NamedQuery(name = PersonaNatural.FindByTipoAndNumeroDocumento, query = "SELECT p FROM PersonaNatural p WHERE p.tipoDocumento.idTipoDocumento = :idTipoDocumento AND p.numeroDocumento = :numeroDocumento ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombres, p.idPersonaNatural"),
		@NamedQuery(name = PersonaNatural.FindByFilterText, query = "SELECT p FROM PersonaNatural p WHERE p.numeroDocumento LIKE :filterText OR UPPER(CONCAT(p.apellidoPaterno,' ', p.apellidoMaterno,' ',p.nombres)) LIKE :filterText ORDER BY p.apellidoPaterno, p.apellidoMaterno, p.nombres, p.idPersonaNatural") })
public class PersonaNatural implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String FindByTipoAndNumeroDocumento = "PersonaNatural.FindByTipoAndNumeroDocumento";
	public final static String FindByFilterText = "PersonaNatural.FindByFilterText";
	public final static String FindAll = "PersonaNatural.FindAll";

	private BigInteger idPersonaNatural;
	private TipoDocumento tipoDocumento;
	private String numeroDocumento;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nombres;
	private Date fechaNacimiento;
	private Sexo sexo;
	private String estadoCivil;
	private String ocupacion;
	private String direccion;
	private String referencia;
	private String telefono;
	private String celular;
	private String email;
	private String ubigeo;
	private String codigoPais;
	private String urlFoto;
	private String urlFirma;
	private Set titulars = new HashSet(0);
	private Set personaJuridicas = new HashSet(0);
	private Set accionistas = new HashSet(0);
	private Set socios = new HashSet(0);
	private Set trabajadors = new HashSet(0);

	public PersonaNatural() {
	}

	public PersonaNatural(BigInteger idPersonaNatural, TipoDocumento tipoDocumento, String numeroDocumento, String apellidoPaterno, String apellidoMaterno, String nombres, Date fechaNacimiento, Sexo sexo) {
		this.idPersonaNatural = idPersonaNatural;
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.nombres = nombres;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
	}

	public PersonaNatural(BigInteger idPersonaNatural, TipoDocumento tipoDocumento, String numeroDocumento, String apellidoPaterno, String apellidoMaterno, String nombres, Date fechaNacimiento, Sexo sexo, String estadoCivil, String ocupacion, String direccion, String referencia, String telefono, String celular, String email, String ubigeo, String codigoPais, Set titulars, Set personaJuridicas, Set accionistas, Set socios, Set trabajadors) {
		this.idPersonaNatural = idPersonaNatural;
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.nombres = nombres;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
		this.estadoCivil = estadoCivil;
		this.ocupacion = ocupacion;
		this.direccion = direccion;
		this.referencia = referencia;
		this.telefono = telefono;
		this.celular = celular;
		this.email = email;
		this.ubigeo = ubigeo;
		this.codigoPais = codigoPais;
		this.titulars = titulars;
		this.personaJuridicas = personaJuridicas;
		this.accionistas = accionistas;
		this.socios = socios;
		this.trabajadors = trabajadors;
	}

	@XmlID
	@XmlAttribute(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	@Column(name = "ID_PERSONA_NATURAL", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdPersonaNatural() {
		return this.idPersonaNatural;
	}

	public void setIdPersonaNatural(BigInteger idPersonaNatural) {
		this.idPersonaNatural = idPersonaNatural;
	}

	@XmlElement(name = "tipoDocumento")
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO_DOCUMENTO", nullable = false)
	public TipoDocumento getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@XmlAttribute(name = "numeroDocumento")
	@NotNull
	@NotBlank
	@Size(min = 1, max = 20)
	@Column(name = "NUMERO_DOCUMENTO", nullable = false, length = 40, columnDefinition = "nvarchar2")
	public String getNumeroDocumento() {
		return this.numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@XmlAttribute(name = "apellidoPaterno")
	@NotNull
	@NotBlank
	@Size(min = 1, max = 60)
	@Column(name = "APELLIDO_PATERNO", nullable = false, length = 120, columnDefinition = "nvarchar2")
	public String getApellidoPaterno() {
		return this.apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	@XmlAttribute(name = "apellidoMaterno")
	@NotNull
	@NotBlank
	@Size(min = 1, max = 60)
	@Column(name = "APELLIDO_MATERNO", nullable = false, length = 120, columnDefinition = "nvarchar2")
	public String getApellidoMaterno() {
		return this.apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	@XmlAttribute(name = "nombres")
	@NotNull
	@NotBlank
	@Size(min = 1, max = 70)
	@Column(name = "NOMBRES", nullable = false, length = 140, columnDefinition = "nvarchar2")
	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	@XmlAttribute(name = "fechaNacimiento")
	@NotNull
	@Past
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_NACIMIENTO", nullable = false, length = 7)
	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@XmlAttribute(name = "sexo")
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "SEXO", nullable = false, length = 20, columnDefinition = "nvarchar2")
	public Sexo getSexo() {
		return this.sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	@XmlAttribute(name = "estadoCivil")
	@Column(name = "ESTADO_CIVIL", length = 20, columnDefinition = "nvarchar2")
	public String getEstadoCivil() {
		return this.estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	@XmlAttribute(name = "ocupacion")
	@Size(max = 30)
	@Column(name = "OCUPACION", length = 60, columnDefinition = "nvarchar2")
	public String getOcupacion() {
		return this.ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	@XmlAttribute(name = "direccion")
	@Size(max = 70)
	@Column(name = "DIRECCION", length = 140, columnDefinition = "nvarchar2")
	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@XmlAttribute(name = "referencia")
	@Size(max = 50)
	@Column(name = "REFERENCIA", length = 100, columnDefinition = "nvarchar2")
	public String getReferencia() {
		return this.referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	@XmlAttribute(name = "telefono")
	@Size(max = 20)
	@Column(name = "TELEFONO", length = 40, columnDefinition = "nvarchar2")
	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	@XmlAttribute(name = "celular")
	@Size(max = 20)
	@Column(name = "CELULAR", length = 40, columnDefinition = "nvarchar2")
	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@XmlAttribute(name = "email")
	@Email
	@Size(max = 70)
	@Column(name = "EMAIL", length = 140, columnDefinition = "nvarchar2")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlAttribute(name = "ubigeo")
	@Size(max = 6)
	@Column(name = "UBIGEO", length = 12, columnDefinition = "nvarchar2")
	public String getUbigeo() {
		return this.ubigeo;
	}

	public void setUbigeo(String ubigeo) {
		this.ubigeo = ubigeo;
	}

	@XmlAttribute(name = "codigoPais")
	@Size(max = 3)
	@Column(name = "CODIGO_PAIS", length = 6, nullable = false, columnDefinition = "nvarchar2")
	public String getCodigoPais() {
		return this.codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	@XmlAttribute(name = "urlFoto")
	@Size(max = 100)
	@Column(name = "URL_FOTO", nullable = true, length = 200, columnDefinition = "nvarchar2")
	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

	@XmlAttribute(name = "urlFirma")
	@Size(max = 100)
	@Column(name = "URL_FIRMA", nullable = true, length = 200, columnDefinition = "nvarchar2")
	public String getUrlFirma() {
		return urlFirma;
	}

	public void setUrlFirma(String urlFirma) {
		this.urlFirma = urlFirma;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<Titular> getTitulars() {
		return this.titulars;
	}

	public void setTitulars(Set titulars) {
		this.titulars = titulars;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "representanteLegal")
	public Set<PersonaJuridica> getPersonaJuridicas() {
		return this.personaJuridicas;
	}

	public void setPersonaJuridicas(Set personaJuridicas) {
		this.personaJuridicas = personaJuridicas;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<Accionista> getAccionistas() {
		return this.accionistas;
	}

	public void setAccionistas(Set accionistas) {
		this.accionistas = accionistas;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<Socio> getSocios() {
		return this.socios;
	}

	public void setSocios(Set socios) {
		this.socios = socios;
	}

	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "personaNatural")
	public Set<Trabajador> getTrabajadors() {
		return this.trabajadors;
	}

	public void setTrabajadors(Set trabajadors) {
		this.trabajadors = trabajadors;
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof PersonaNatural)) {
			return false;
		}
		final PersonaNatural other = (PersonaNatural) obj;
		if (this.tipoDocumento == null || this.numeroDocumento == null) {
			return false;
		}
		if (other.getNumeroDocumento().equalsIgnoreCase(this.numeroDocumento)) {
			if (other.getTipoDocumento() != null) {
				if (other.getTipoDocumento().equals(this.tipoDocumento)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		if (this.tipoDocumento != null && this.numeroDocumento != null)
			return this.tipoDocumento.hashCode() * this.numeroDocumento.hashCode();
		return 0;
	}

}
