package org.sistemafinanciero.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sistemafinanciero.entity.type.EstadoCuentaAporte;
import org.sistemafinanciero.entity.type.TipoPersona;

/**
 * The persistent class for the SOCIO_VIEW database table.
 * 
 */
@Entity
@Table(name = "SOCIO_VIEW", schema = "C##BDSISTEMAFINANCIERO")
@XmlRootElement(name = "socioview")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({ @NamedQuery(name = SocioView.findAll, query = "SELECT sv FROM SocioView sv WHERE sv.estadoSocio IN :modeEstado ORDER BY sv.socio, sv.idsocio ASC"), @NamedQuery(name = SocioView.FindAllHaveCuentaAporte, query = "SELECT sv FROM SocioView sv WHERE sv.idCuentaAporte IS NOT NULL AND sv.estadoSocio IN :modeEstado ORDER BY sv.socio, sv.idsocio ASC"), @NamedQuery(name = SocioView.FindByFilterTextSocioView, query = "SELECT sv FROM SocioView sv WHERE sv.estadoSocio IN :modeEstado AND (sv.socio LIKE :filtertext or sv.numeroDocumento like :filtertext) ORDER BY sv.socio, sv.idsocio ASC"),
		@NamedQuery(name = SocioView.FindByFilterTextSocioViewAllHaveCuentaAporte, query = "SELECT sv FROM SocioView sv WHERE sv.idCuentaAporte IS NOT NULL AND sv.estadoSocio IN :modeEstado AND (sv.socio LIKE :filtertext or sv.numeroDocumento like :filtertext) ORDER BY sv.socio, sv.idsocio ASC"), @NamedQuery(name = SocioView.FindByTipoAndNumeroDocumento, query = "SELECT sv FROM SocioView sv WHERE sv.tipoPersona = :tipoPersona AND sv.idTipoDocumento = :idTipoDocumento AND sv.numeroDocumento = :numeroDocumento AND sv.estadoSocio = TRUE") })
public class SocioView implements Serializable {
	private static final long serialVersionUID = 1L;

	public final static String findAll = "SocioView.findAll";
	public final static String FindAllHaveCuentaAporte = "SocioView.FindAllHaveCuentaAporte";
	public final static String FindByFilterTextSocioView = "SocioView.FindByFilterTextSocioView";
	public final static String FindByFilterTextSocioViewAllHaveCuentaAporte = "SocioView.FindByFilterTextSocioViewAllHaveCuentaAporte";
	public final static String FindByTipoAndNumeroDocumento = "SocioView.FindByTipoAndNumeroDocumento";

	private BigInteger idsocio;
	private int estadoSocio;
	private Date fechaAsociado;
	private Date fechaFin;

	private BigInteger idCuentaAporte;
	private String numeroCuentaAporte;
	private EstadoCuentaAporte estadoCuentaAporte;

	private TipoPersona tipoPersona;
	private BigInteger idTipoDocumento;
	private String tipoDocumento;
	private String numeroDocumento;
	private String socio;
	private Date fechaNacimiento;

	private BigInteger idApoderado;
	private String codigoAgencia;

	public SocioView() {
	}

	@XmlElement(name = "id")
	@Id
	@Column(name = "ID_SOCIO", unique = true, nullable = false, precision = 22, scale = 0)
	public BigInteger getIdsocio() {
		return this.idsocio;
	}

	public void setIdsocio(BigInteger idsocio) {
		this.idsocio = idsocio;
	}

	@XmlElement(name = "estado")
	@Column(name = "ESTADO_SOCIO", nullable = false)
	public boolean getEstadoSocio() {
		return (this.estadoSocio == 1 ? true : false);
	}

	public void setEstadoSocio(boolean estadoSocio) {
		this.estadoSocio = (estadoSocio ? 1 : 0);
	}

	@XmlElement(name = "fechaAsociado")
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ASOCIADO")
	public Date getFechaAsociado() {
		return fechaAsociado;
	}

	public void setFechaAsociado(Date fechaAsociado) {
		this.fechaAsociado = fechaAsociado;
	}

	@XmlElement(name = "fechaFin")
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_FIN")
	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	@XmlElement(name = "idCuentaAporte")
	@Column(name = "ID_CUENTA_APORTE", precision = 22, scale = 0)
	public BigInteger getIdCuentaAporte() {
		return idCuentaAporte;
	}

	public void setIdCuentaAporte(BigInteger idCuentaAporte) {
		this.idCuentaAporte = idCuentaAporte;
	}

	@XmlElement(name = "numeroCuentaAporte")
	@Column(name = "NUMERO_CUENTA_APORTE", nullable = false, length = 14, columnDefinition = "nvarchar2")
	public String getNumeroCuentaAporte() {
		return numeroCuentaAporte;
	}

	public void setNumeroCuentaAporte(String numeroCuentaAporte) {
		this.numeroCuentaAporte = numeroCuentaAporte;
	}

	@XmlElement(name = "estadoCuentaAporte")
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_CUENTA_APORTE", nullable = false, length = 20, columnDefinition = "nvarchar2")
	public EstadoCuentaAporte getEstadoCuentaAporte() {
		return estadoCuentaAporte;
	}

	public void setEstadoCuentaAporte(EstadoCuentaAporte estadoCuentaAporte) {
		this.estadoCuentaAporte = estadoCuentaAporte;
	}

	@XmlElement(name = "tipoPersona")
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_PERSONA", nullable = false, length = 20, columnDefinition = "nvarchar2")
	public TipoPersona getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(TipoPersona tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	@XmlElement(name = "idTipoDocumento")
	@Column(name = "ID_TIPO_DOCUMENTO", precision = 22, scale = 0)
	public BigInteger getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(BigInteger idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	@XmlElement(name = "tipoDocumento")
	@Column(name = "TIPO_DOCUMENTO", columnDefinition = "nvarchar2")
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@XmlElement(name = "numeroDocumento")
	@Column(name = "NUMERO_DOCUMENTO", columnDefinition = "nvarchar2")
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	@XmlElement(name = "socio")
	@Column(name = "SOCIO", nullable = false, length = 192, columnDefinition = "nvarchar2")
	public String getSocio() {
		return this.socio;
	}

	public void setSocio(String socio) {
		this.socio = socio;
	}

	@XmlElement
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_NACIMIENTO")
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@XmlElement(name = "idApoderado")
	@Column(name = "ID_APODERADO", precision = 22, scale = 0)
	public BigInteger getIdApoderado() {
		return idApoderado;
	}

	public void setIdApoderado(BigInteger idApoderado) {
		this.idApoderado = idApoderado;
	}

	@XmlElement(name = "codigoAgencia")
	@Column(name = "CODIGO_AGENCIA", columnDefinition = "nvarchar2")
	public String getCodigoAgencia() {
		return codigoAgencia;
	}

	public void setCodigoAgencia(String codigoAgencia) {
		this.codigoAgencia = codigoAgencia;
	}

}