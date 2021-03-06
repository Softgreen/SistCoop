package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Accionista generated by hbm2java
 */
@Entity
@Table(name = "HISTORIAL_PAGO_SOBRE_GIRO", schema = "BDSISTEMAFINANCIERO")
@XmlRootElement(name = "sobreGiro")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({ @NamedQuery(name = HistorialPagoSobreGiro.findByIdSobreGiro, query = "SELECT h FROM HistorialPagoSobreGiro h WHERE h.sobreGiro.idSobreGiro = :idSobreGiro ORDER BY h.fecha") })
public class HistorialPagoSobreGiro implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    public final static String findByIdSobreGiro = "HistorialPagoSobreGiro.findByIdSobreGiro";

    private BigInteger idHistorialPagoSobreGiro;

    private SobreGiro sobreGiro;

    private BigDecimal monto;
    private Date fecha;

    public HistorialPagoSobreGiro() {

    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTORIALPAGOSOBREGIRO_SEQ")
    @SequenceGenerator(name = "HISTORIALPAGOSOBREGIRO_SEQ", initialValue = 1, allocationSize = 1, sequenceName = "HISTORIALPAGOSOBREGIRO_SEQ")
    @XmlElement(name = "id")
    @Id
    @Column(name = "ID_HISTORIAL_PAGO_SOBRE_GIRO", unique = true, nullable = false, precision = 22, scale = 0)
    public BigInteger getIdHistorialPagoSobreGiro() {
        return this.idHistorialPagoSobreGiro;
    }

    public void setIdHistorialPagoSobreGiro(BigInteger idHistorialPagoSobreGiro) {
        this.idHistorialPagoSobreGiro = idHistorialPagoSobreGiro;
    }

    @XmlElement(name = "sobreGiro")
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOBRE_GIRO", nullable = false)
    public SobreGiro getSobreGiro() {
        return sobreGiro;
    }

    public void setSobreGiro(SobreGiro sobreGiro) {
        this.sobreGiro = sobreGiro;
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA", nullable = false, length = 7)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((idHistorialPagoSobreGiro == null) ? 0 : idHistorialPagoSobreGiro.hashCode());
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
        HistorialPagoSobreGiro other = (HistorialPagoSobreGiro) obj;
        if (idHistorialPagoSobreGiro == null) {
            if (other.idHistorialPagoSobreGiro != null)
                return false;
        } else if (!idHistorialPagoSobreGiro.equals(other.idHistorialPagoSobreGiro))
            return false;
        return true;
    }

}
