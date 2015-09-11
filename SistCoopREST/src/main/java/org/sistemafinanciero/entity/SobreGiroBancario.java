package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sistemafinanciero.entity.type.EstadoSobreGiroBancario;

/**
 * Accionista generated by hbm2java
 */
@Entity
@Table(name = "SOBRE_GIRO_BANCARIO", schema = "C##BDSISTEMAFINANCIERO")
@XmlRootElement(name = "sobreGiroBancario")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
        @NamedQuery(name = SobreGiroBancario.findByIdCuentaBancariaAndEstado, query = "SELECT s FROM SobreGiroBancario s WHERE s.cuentaBancaria.idCuentaBancaria = :idCuentaBancaria AND s.estado = :estado ORDER BY s.fechaCreacion ASC"),
        @NamedQuery(name = SobreGiroBancario.findByIdTransaccionBancaria, query = "SELECT s FROM SobreGiroBancario s WHERE s.idTransaccionBancaria = :idTransaccionBancaria") })
public class SobreGiroBancario implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public final static String findByIdCuentaBancariaAndEstado = "SobreGiroBancario.findByIdCuentaBancariaAndEstado";
    public final static String findByIdTransaccionBancaria = "SobreGiroBancario.findByIdTransaccionBancaria";

    private BigInteger idSobreGiro;

    private CuentaBancaria cuentaBancaria;
    private BigDecimal monto;
    private BigDecimal interes;
    private Date fechaCreacion;
    private EstadoSobreGiroBancario estado;
    private BigInteger idTransaccionBancaria;
    private Set historialPagos = new HashSet(0);

    public SobreGiroBancario() {

    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOBREGIROBANCARIO_SEQ")
    @SequenceGenerator(name = "SOBREGIROBANCARIO_SEQ", initialValue = 1, allocationSize = 1, sequenceName = "SOBREGIROBANCARIO_SEQ")
    @XmlElement(name = "id")
    @Id
    @Column(name = "ID_SOBRE_GIRO_BANCARIO", unique = true, nullable = false, precision = 22, scale = 0)
    public BigInteger getIdSobreGiro() {
        return this.idSobreGiro;
    }

    public void setIdSobreGiro(BigInteger idSobreGiro) {
        this.idSobreGiro = idSobreGiro;
    }

    @XmlElement(name = "cuentaBancaria")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CUENTA_BANCARIA", nullable = false)
    public CuentaBancaria getCuentaBancaria() {
        return cuentaBancaria;
    }

    public void setCuentaBancaria(CuentaBancaria cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }

    @XmlElement
    @Column(name = "MONTO", nullable = false, precision = 18)
    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @XmlElement
    @Column(name = "INTERES", nullable = false, precision = 18)
    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    @XmlElement
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION", nullable = false, length = 7)
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @XmlElement
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20, columnDefinition = "nvarchar2")
    public EstadoSobreGiroBancario getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoSobreGiroBancario estado) {
        this.estado = estado;
    }

    @Column(name = "ID_TRANSACCION_BANCARIA", unique = true, nullable = false, precision = 22, scale = 0)
    public BigInteger getIdTransaccionBancaria() {
        return idTransaccionBancaria;
    }

    public void setIdTransaccionBancaria(BigInteger idTransaccionBancaria) {
        this.idTransaccionBancaria = idTransaccionBancaria;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sobreGiroBancario")
    public Set<HistorialPagoSobreGiroBancario> getHistorialPagos() {
        return this.historialPagos;
    }

    public void setHistorialPagos(Set historialPagos) {
        this.historialPagos = historialPagos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idSobreGiro == null) ? 0 : idSobreGiro.hashCode());
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
        SobreGiroBancario other = (SobreGiroBancario) obj;
        if (idSobreGiro == null) {
            if (other.idSobreGiro != null)
                return false;
        } else if (!idSobreGiro.equals(other.idSobreGiro))
            return false;
        return true;
    }

}