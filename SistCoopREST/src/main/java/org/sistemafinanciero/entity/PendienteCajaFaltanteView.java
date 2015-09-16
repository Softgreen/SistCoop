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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "PENDIENTE_CAJA_FALTANTE_DEUDA", schema = "C##BDSISTEMAFINANCIERO")
@XmlRootElement(name = "pendientecaja")
@XmlAccessorType(XmlAccessType.NONE)
@NamedQueries({
        @NamedQuery(name = PendienteCajaFaltanteView.findByIdCajaConMonto, query = "SELECT p FROM PendienteCajaFaltanteView p INNER JOIN p.historialCajaCreacion hc INNER JOIN hc.caja c WHERE p.montoPorPagar < 0 AND c.idCaja = :idCaja ORDER BY p.fecha, p.hora"),
        @NamedQuery(name = PendienteCajaFaltanteView.findByIdPendienteCaja, query = "SELECT p FROM PendienteCajaFaltanteView p WHERE p.idPendienteCaja = :idPendienteCaja") })
public class PendienteCajaFaltanteView implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public final static String findByIdCajaConMonto = "PendienteCajaFaltanteView.findByIdCajaConMonto";
    public final static String findByIdPendienteCaja = "PendienteCajaFaltanteView.findByIdPendienteCaja";

    private BigInteger idPendienteCaja;
    private TipoPendienteCaja tipoPendiente;
    private Moneda moneda;
    private BigDecimal monto;
    private Date fecha;
    private Date hora;
    private HistorialCaja historialCajaCreacion;
    private String trabajadorCrea;
    private String observacion;
    private BigDecimal montoPorPagar;

    public PendienteCajaFaltanteView() {

    }

    @XmlElement(name = "id")
    @Id
    @Column(name = "ID_PENDIENTE_CAJA", unique = true, nullable = false, precision = 22, scale = 0)
    public BigInteger getIdPendienteCaja() {
        return this.idPendienteCaja;
    }

    public void setIdPendienteCaja(BigInteger idPendienteCaja) {
        this.idPendienteCaja = idPendienteCaja;
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

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_HISTORIAL_CAJA_CREACION", nullable = false)
    public HistorialCaja getHistorialCajaCreacion() {
        return this.historialCajaCreacion;
    }

    public void setHistorialCajaCreacion(HistorialCaja historialCajaCreacion) {
        this.historialCajaCreacion = historialCajaCreacion;
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

    @XmlElement
    @Column(name = "MONTO_POR_PAGAR", nullable = false, precision = 18)
    public BigDecimal getMontoPorPagar() {
        return this.montoPorPagar;
    }

    public void setMontoPorPagar(BigDecimal montoPorPagar) {
        this.montoPorPagar = montoPorPagar;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idPendienteCaja == null) ? 0 : idPendienteCaja.hashCode());
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
        PendienteCajaFaltanteView other = (PendienteCajaFaltanteView) obj;
        if (idPendienteCaja == null) {
            if (other.idPendienteCaja != null)
                return false;
        } else if (!idPendienteCaja.equals(other.idPendienteCaja))
            return false;
        return true;
    }

}
