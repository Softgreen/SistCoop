package org.sistemafinanciero.entity.dto;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Subselect;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Subselect(value = "SELECT c.ID_CAJA as idCaja, "
		+ "c.denominacion, "
		+ "c.abreviatura, "
		+ "c.estado, "
		+ "c.abierto, "
		+ "c.ESTADO_MOVIMIENTO as estadoMovimiento, "
		+ "listagg(b.denominacion || ',') WITHIN GROUP (ORDER BY c.ID_CAJA) AS bovedas "
		//+ "a.id_agencia as idAgencia"
		+ "FROM Caja c "
		+ "INNER JOIN BOVEDA_CAJA bc ON C.id_caja = bc.id_caja "
		+ "INNER JOIN BOVEDA b ON B.id_boveda = bc.id_boveda "
		//+ "INNER JOIN AGENCIA a ON a.id_agencia = b.id_agencia "
		+ "GROUP BY c.ID_CAJA, c.denominacion, c.abreviatura, c.estado, c.abierto, c.ESTADO_MOVIMIENTO")
@NamedQueries({ @NamedQuery(name = CajaView.findByIdAgencia, query = "SELECT c FROM CajaView c") })
public class CajaView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String findByIdAgencia = "CajaView.findByIdAgencia";

	private BigInteger idCaja;
	private String denominacion;
	private String abreviatura;
	private int estado;
	private int abierto;
	private int estadoMovimiento;
	
	private String bovedas;
	
	//private BigInteger idAgencia;

	// private Set bovedaCajas = new HashSet(0);

	public CajaView() {
		// TODO Auto-generated constructor stub
	}

	@XmlElement(name = "id")
	@Id
	public BigInteger getIdCaja() {
		return idCaja;
	}

	public void setIdCaja(BigInteger idCaja) {
		this.idCaja = idCaja;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public boolean getEstado() {
		return estado == 1;
	}

	public void setEstado(boolean estado) {
		this.estado = (estado ? 1: 0);
	}

	public boolean getAbierto() {
		return abierto == 1;
	}

	public void setAbierto(boolean abierto) {
		this.abierto = (abierto ? 1 : 0);
	}

	public boolean getEstadoMovimiento() {
		return estadoMovimiento == 1;
	}

	public void setEstadoMovimiento(boolean estadomovimiento) {
		this.estadoMovimiento = (estadomovimiento ? 1 : 0);
	}

	public String getBovedas() {
		return bovedas;
	}

	public void setBovedas(String bovedas) {
		this.bovedas = bovedas;
	}

	/*public BigInteger getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(BigInteger idAgencia) {
		this.idAgencia = idAgencia;
	}*/

}
