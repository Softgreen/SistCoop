package org.sistemafinanciero.entity;

// Generated 02-may-2014 11:48:28 by Hibernate Tools 4.0.0

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.sistemafinanciero.entity.type.TipoPendienteCaja;

@Entity
@Table(name = "PENDIENTE_BOVEDA", schema = "C##BDSISTEMAFINANCIERO")
@XmlRootElement(name = "pendientecaja")
@XmlAccessorType(XmlAccessType.NONE)
// @NamedQueries({ @NamedQuery(name = PendienteCaja.findByIdCaja, query =
// "SELECT p FROM PendienteCaja p INNER JOIN p.historialCaja hc INNER JOIN hc.caja c WHERE c.idCaja = :idCaja ORDER BY p.fecha, p.hora")
// })
public class PendienteBoveda implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    // public final static String findByIdCaja = "PendienteCaja.findByIdCaja";

    private BigInteger idPendienteBoveda;
    private TipoPendienteCaja tipoPendiente;
    private Moneda moneda;
    private BigDecimal monto;
    private Date fecha;
    private Date hora;
    private String observacion;
    private String trabajadorCrea;

    public PendienteBoveda() {

    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PENDIENTEBOVEDA_SEQ")
    @SequenceGenerator(name = "PENDIENTEBOVEDA_SEQ", initialValue = 1, allocationSize = 1, sequenceName = "PENDIENTEBOVEDA_SEQ")
    @XmlElement(name = "id")
    @Id
    @Column(name = "ID_PENDIENTE_BOVEDA", unique = true, nullable = false, precision = 22, scale = 0)
    public BigInteger getIdPendienteBoveda() {
        return this.idPendienteBoveda;
    }

    public void setIdPendienteBoveda(BigInteger idPendienteBoveda) {
        this.idPendienteBoveda = idPendienteBoveda;
    }

    @XmlElement
    @Enumerated(value = EnumType.STRING)
    @Column(name = "TIPO_PENDIENTE", nullable = false, length = 40, columnDefinition = "nvarchar2")
    public TipoPendienteCaja getTipoPendiente() {
        return this.tipoPendiente;
    }

    public void setTipoPendiente(TipoPendienteCaja tipoPendiente) {
        this.tipoPendiente = tipoPendiente;
    }

    @XmlElement
    @Column(name = "MONTO", nullable = false, precision = 18)
    public BigDecimal getMonto() {
        return this.monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @XmlElement
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MONEDA", nullable = false)
    public Moneda getMoneda() {
        return this.moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    @XmlElement
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHA", nullable = false, length = 7)
    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @XmlElement
    @Column(name = "HORA", nullable = false)
    public Date getHora() {
        return this.hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    @XmlElement
    @Column(name = "TRABAJADOR_CREA", nullable = false, columnDefinition = "nvarchar2")
    public String getTrabajadorCrea() {
        return this.trabajadorCrea;
    }

    public void setTrabajadorCrea(String trabajadorCrea) {
        this.trabajadorCrea = trabajadorCrea;
    }

    @XmlElement
    @Column(name = "OBSERVACION", length = 40, columnDefinition = "nvarchar2")
    public String getObservacion() {
        return this.observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPendienteBoveda == null) ? 0 : idPendienteBoveda.hashCode());
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
        PendienteBoveda other = (PendienteBoveda) obj;
        if (idPendienteBoveda == null) {
            if (other.idPendienteBoveda != null)
                return false;
        } else if (!idPendienteBoveda.equals(other.idPendienteBoveda))
            return false;
        return true;
    }

}
