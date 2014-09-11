package org.sistemafinanciero.entity.dto;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Subselect;

@Entity
@Subselect(value = "SELECT c.ID_CAJA as idCaja, c.denominacion, c.abreviatura, c.estado, c.abierto, c.ESTADO_MOVIMIENTO as estadoMovimiento FROM Caja c")
@NamedQueries({ 
	@NamedQuery(name = CajaView.findByIdAgencia, query = "SELECT c FROM CajaView c")	
})
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
	private int estadomovimiento;
	//private Set bovedaCajas = new HashSet(0);
	
	public CajaView() {
		// TODO Auto-generated constructor stub
	}

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

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getAbierto() {
		return abierto;
	}

	public void setAbierto(int abierto) {
		this.abierto = abierto;
	}

	public int getEstadomovimiento() {
		return estadomovimiento;
	}

	public void setEstadomovimiento(int estadomovimiento) {
		this.estadomovimiento = estadomovimiento;
	}

	/*@XmlTransient
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "caja")
	public Set<BovedaCaja> getBovedaCajas() {
		return this.bovedaCajas;
	}

	public void setBovedaCajas(Set bovedaCajas) {
		this.bovedaCajas = bovedaCajas;
	}*/

}
