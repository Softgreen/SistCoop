package org.sistemafinanciero.entity.dto;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Subselect;

/*@Subselect(value = "SELECT c.ID_CAJA as idCaja, "
 + "c.denominacion, "
 + "c.abreviatura, "
 + "c.estado, "
 + "c.abierto, "
 + "c.ESTADO_MOVIMIENTO as estadoMovimiento, "
 + "listagg(b.denominacion || ',') WITHIN GROUP (ORDER BY c.ID_CAJA) AS bovedas, "
 + "a.id_agencia as idAgencia "
 + "FROM Caja c "
 + "INNER JOIN BOVEDA_CAJA bc ON C.id_caja = bc.id_caja "
 + "INNER JOIN BOVEDA b ON B.id_boveda = bc.id_boveda "
 + "INNER JOIN AGENCIA a ON a.id_agencia = b.id_agencia "
 + "GROUP BY c.ID_CAJA, c.denominacion, c.abreviatura, c.estado, c.abierto, c.ESTADO_MOVIMIENTO, a.id_agencia")*/
@NamedQueries({ @NamedQuery(name = CajaView.findByIdAgencia, query = "SELECT c FROM CajaView c WHERE c.idAgencia = :idAgencia") })
@Entity
@Table(name = "CAJA_VIEW", schema = "C##BDSISTEMAFINANCIERO")
@XmlRootElement(name = "cajaView")
@XmlAccessorType(XmlAccessType.NONE)
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
    private String saldos;

    private BigInteger idAgencia;

    public CajaView() {
        // TODO Auto-generated constructor stub
    }

    @XmlElement(name = "id")
    @Id
    @Column(name = "ID_CAJA", unique = true, nullable = false, precision = 22, scale = 0)
    public BigInteger getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(BigInteger idCaja) {
        this.idCaja = idCaja;
    }

    @XmlElement(name = "denominacion")
    @Column(name = "DENOMINACION", nullable = false, length = 100, columnDefinition = "nvarchar2")
    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    @XmlElement(name = "abreviatura")
    @Column(name = "ABREVIATURA", nullable = false, length = 100, columnDefinition = "nvarchar2")
    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    @XmlElement(name = "estado")
    @Column(name = "ESTADO", nullable = false, precision = 22, scale = 0)
    public boolean getEstado() {
        return estado == 1;
    }

    public void setEstado(boolean estado) {
        this.estado = (estado ? 1 : 0);
    }

    @XmlElement(name = "abierto")
    @Column(name = "ABIERTO", nullable = false, precision = 22, scale = 0)
    public boolean getAbierto() {
        return abierto == 1;
    }

    public void setAbierto(boolean abierto) {
        this.abierto = (abierto ? 1 : 0);
    }

    @XmlElement(name = "estadoMovimiento")
    @Column(name = "ESTADO_MOVIMIENTO", nullable = false, precision = 22, scale = 0)
    public boolean getEstadoMovimiento() {
        return estadoMovimiento == 1;
    }

    public void setEstadoMovimiento(boolean estadomovimiento) {
        this.estadoMovimiento = (estadomovimiento ? 1 : 0);
    }

    @XmlElement(name = "bovedas")
    @Column(name = "BOVEDAS", nullable = false, length = 100)
    public String getBovedas() {
        return bovedas;
    }

    public void setBovedas(String bovedas) {
        this.bovedas = bovedas;
    }

    @XmlElement(name = "idAgencia")
    @Column(name = "ID_AGENCIA", precision = 22, scale = 0)
    public BigInteger getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(BigInteger idAgencia) {
        this.idAgencia = idAgencia;
    }

    @XmlElement(name = "saldos")
    @Column(name = "SALDOS", nullable = false, length = 100)
    public String getSaldos() {
        return saldos;
    }

    public void setSaldos(String saldos) {
        this.saldos = saldos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idCaja == null) ? 0 : idCaja.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CajaView other = (CajaView) obj;
        if (idCaja == null) {
            if (other.idCaja != null)
                return false;
        } else if (!idCaja.equals(other.idCaja))
            return false;
        return true;
    }

}
