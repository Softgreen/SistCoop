package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.hibernate.Hibernate;
import org.joda.time.LocalDate;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.BovedaCaja;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.Cheque;
import org.sistemafinanciero.entity.Chequera;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.CuentaBancariaInteresGenera;
import org.sistemafinanciero.entity.DetalleHistorialCaja;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.entity.PendienteCaja;
import org.sistemafinanciero.entity.PendienteCajaFaltanteView;
import org.sistemafinanciero.entity.PersonaJuridica;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Socio;
import org.sistemafinanciero.entity.TipoDocumento;
import org.sistemafinanciero.entity.Titular;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TrabajadorCaja;
import org.sistemafinanciero.entity.TransaccionBancaria;
import org.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.sistemafinanciero.entity.TransaccionBovedaCajaDetalle;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.TransaccionCajaCaja;
import org.sistemafinanciero.entity.TransaccionCheque;
import org.sistemafinanciero.entity.TransaccionCompraVenta;
import org.sistemafinanciero.entity.TransaccionCuentaAporte;
import org.sistemafinanciero.entity.TransferenciaBancaria;
import org.sistemafinanciero.entity.VariableSistema;
import org.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.sistemafinanciero.entity.dto.CajaView;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.dto.ResumenOperacionesCaja;
import org.sistemafinanciero.entity.dto.VoucherCompraVenta;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCheque;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCuentaAporte;
import org.sistemafinanciero.entity.dto.VoucherTransferenciaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPendienteCaja;
import org.sistemafinanciero.entity.type.Tipotransaccionbancaria;
import org.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.sistemafinanciero.entity.type.Variable;
import org.sistemafinanciero.service.nt.CajaServiceNT;
import org.sistemafinanciero.service.nt.MonedaServiceNT;
import org.sistemafinanciero.service.nt.VariableSistemaServiceNT;
import org.sistemafinanciero.util.DateUtils;
import org.sistemafinanciero.util.EntityManagerProducer;

@Named
@Stateless
@Remote(CajaServiceNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CajaServiceBeanNT implements CajaServiceNT {

    @Inject
    private DAO<Object, CajaView> cajaViewDAO;

    @Inject
    private DAO<Object, Caja> cajaDAO;

    @Inject
    private DAO<Object, HistorialCaja> historialCajaDAO;

    @Inject
    private DAO<Object, TransaccionBovedaCajaView> transaccionBovedaCajaViewDAO;

    @Inject
    private DAO<Object, TransaccionCuentaAporte> transaccionCuentaAporteDAO;

    @Inject
    private DAO<Object, TransaccionBancaria> transaccionBancariaDAO;

    @Inject
    private DAO<Object, TransaccionCheque> transaccionChequeDAO;

    @Inject
    private DAO<Object, TransferenciaBancaria> transferenciaBancariaDAO;

    @Inject
    private DAO<Object, TransaccionCompraVenta> transaccionCopraVentaDAO;

    @Inject
    private DAO<Object, HistorialTransaccionCaja> historialTransaccionCajaDAO;

    @Inject
    private DAO<Object, CuentaBancariaInteresGenera> cuentaBancariaInteresGeneraDAO;

    // @Inject
    // private DAO<Object, PendienteCaja> pendienteCajaDAO;

    @Inject
    private DAO<Object, PendienteCajaFaltanteView> pendienteCajaFaltanteViewDAO;

    @EJB
    private MonedaServiceNT monedaServiceNT;

    @EJB
    private VariableSistemaServiceNT variableSistemaServiceNT;

    @Inject
    private EntityManagerProducer em;

    private HistorialCaja getHistorialActivo(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja cajaHistorial = null;
        QueryParameter queryParameter = QueryParameter.with("idcaja", caja.getIdCaja());
        List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo,
                queryParameter.parameters());
        for (HistorialCaja c : list) {
            cajaHistorial = c;
            break;
        }
        return cajaHistorial;
    }

    @Override
    public Caja findById(BigInteger id) {
        return cajaDAO.find(id);
    }

    @Override
    public List<Caja> findAll() {
        return cajaDAO.findAll();
    }

    @Override
    public int count() {
        return cajaDAO.count();
    }

    @Override
    public Set<Boveda> getBovedas(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja != null) {
            Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
            Set<Boveda> bovedas = new HashSet<>();
            for (BovedaCaja bc : bovedasCajas) {
                Boveda boveda = bc.getBoveda();
                Moneda moneda = boveda.getMoneda();
                Hibernate.initialize(boveda);
                Hibernate.initialize(moneda);
                bovedas.add(boveda);
            }
            return bovedas;
        } else {
            return null;
        }
    }

    @Override
    public Set<Moneda> getMonedas(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja != null) {
            Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
            Set<Moneda> monedas = new HashSet<>();
            for (BovedaCaja bc : bovedasCajas) {
                Boveda boveda = bc.getBoveda();
                Moneda moneda = boveda.getMoneda();
                Hibernate.initialize(moneda);
                monedas.add(moneda);
            }
            return monedas;
        } else {
            return null;
        }
    }

    @Override
    public Set<HistorialCaja> getHistorialCaja(BigInteger idCaja, Date dateDesde, Date dateHasta) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja != null) {
            LocalDate localDesde = new LocalDate(dateDesde);
            LocalDate localHasta = new LocalDate(dateHasta);

            QueryParameter queryParameter = QueryParameter.with("idcaja", caja.getIdCaja())
                    .and("desde", localDesde.toDateTimeAtStartOfDay().toDate())
                    .and("hasta", localHasta.toDateTimeAtStartOfDay().toDate());
            List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(
                    HistorialCaja.findByHistorialDateRange, queryParameter.parameters());
            return new HashSet<HistorialCaja>(list);
        } else {
            return null;
        }
    }

    @Override
    public Set<GenericMonedaDetalle> getDetalleCaja(BigInteger idCaja) {
        Set<GenericMonedaDetalle> result = null;
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;

        // recuperando el historial activo
        HistorialCaja cajaHistorial = getHistorialActivo(idCaja);

        // recorrer por todas las bovedas
        Set<BovedaCaja> bovedas = caja.getBovedaCajas();
        result = new HashSet<GenericMonedaDetalle>();
        for (BovedaCaja bovedaCaja : bovedas) {
            Boveda boveda = bovedaCaja.getBoveda();
            Moneda moneda = boveda.getMoneda();
            Hibernate.initialize(moneda);
            GenericMonedaDetalle genericMonedaDetalle = new GenericMonedaDetalle(moneda);
            // recorrer todas las denominaciones existentes en la base de datos
            List<MonedaDenominacion> denominacionesExistentes = monedaServiceNT.getDenominaciones(moneda
                    .getIdMoneda());
            for (MonedaDenominacion m : denominacionesExistentes) {
                GenericDetalle detalle = new GenericDetalle(m.getValor(), BigInteger.ZERO);
                genericMonedaDetalle.addElementDetalleReplacingIfExist(detalle);
                genericMonedaDetalle.setMoneda(m.getMoneda());
            }
            // si tiene historiales activos reemplazar por cantidades
            if (cajaHistorial != null) {
                for (DetalleHistorialCaja d : cajaHistorial.getDetalleHistorialCajas()) {
                    Moneda monedaHistorial = d.getMonedaDenominacion().getMoneda();
                    if (monedaHistorial.equals(moneda)) {
                        GenericDetalle detalle = new GenericDetalle(d.getMonedaDenominacion().getValor(),
                                d.getCantidad());
                        genericMonedaDetalle.addElementDetalleReplacingIfExist(detalle);
                    }
                }
            }
            result.add(genericMonedaDetalle);
        }
        return result;
    }

    @Override
    public Set<GenericMonedaDetalle> getDetalleCaja(BigInteger idCaja, BigInteger idHistorialCaja) {
        Set<GenericMonedaDetalle> result = null;
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;

        // recuperando el historial activo
        HistorialCaja cajaHistorial = historialCajaDAO.find(idHistorialCaja);
        if (!cajaHistorial.getCaja().equals(caja))
            return null;

        // recorrer por todas las bovedas
        Set<BovedaCaja> bovedas = caja.getBovedaCajas();
        result = new HashSet<GenericMonedaDetalle>();
        for (BovedaCaja bovedaCaja : bovedas) {
            Boveda boveda = bovedaCaja.getBoveda();
            Moneda moneda = boveda.getMoneda();
            Hibernate.initialize(moneda);
            GenericMonedaDetalle genericMonedaDetalle = new GenericMonedaDetalle(moneda);
            // recorrer todas las denominaciones existentes en la base de datos
            List<MonedaDenominacion> denominacionesExistentes = monedaServiceNT.getDenominaciones(moneda
                    .getIdMoneda());
            for (MonedaDenominacion m : denominacionesExistentes) {
                GenericDetalle detalle = new GenericDetalle(m.getValor(), BigInteger.ZERO);
                genericMonedaDetalle.addElementDetalleReplacingIfExist(detalle);
                genericMonedaDetalle.setMoneda(m.getMoneda());
            }
            // si tiene historiales activos reemplazar por cantidades
            if (cajaHistorial != null) {
                for (DetalleHistorialCaja d : cajaHistorial.getDetalleHistorialCajas()) {
                    Moneda monedaHistorial = d.getMonedaDenominacion().getMoneda();
                    if (monedaHistorial.equals(moneda)) {
                        GenericDetalle detalle = new GenericDetalle(d.getMonedaDenominacion().getValor(),
                                d.getCantidad());
                        genericMonedaDetalle.addElementDetalleReplacingIfExist(detalle);
                    }
                }
            }
            result.add(genericMonedaDetalle);
        }
        return result;
    }

    @Override
    public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historial = getHistorialActivo(idCaja);
        QueryParameter queryParameter = QueryParameter
                .with("idHistorialCaja", historial.getIdHistorialCaja()).and("origen",
                        TransaccionBovedaCajaOrigen.CAJA);
        List<TransaccionBovedaCajaView> list = transaccionBovedaCajaViewDAO.findByNamedQuery(
                TransaccionBovedaCajaView.findByHistorialCajaEnviados, queryParameter.parameters());
        return list;
    }

    @Override
    public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historial = getHistorialActivo(idCaja);
        QueryParameter queryParameter = QueryParameter
                .with("idHistorialCaja", historial.getIdHistorialCaja()).and("origen",
                        TransaccionBovedaCajaOrigen.BOVEDA);
        List<TransaccionBovedaCajaView> list = transaccionBovedaCajaViewDAO.findByNamedQuery(
                TransaccionBovedaCajaView.findByHistorialCajaEnviados, queryParameter.parameters());
        return list;
    }

    @Override
    public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historial = getHistorialActivo(idCaja);
        Set<TransaccionCajaCaja> enviados = historial.getTransaccionCajaCajasForIdCajaHistorialOrigen();
        for (TransaccionCajaCaja ts : enviados) {
            Moneda moneda = ts.getMoneda();

            Hibernate.initialize(ts.getHistorialCajaDestino().getCaja());
            Hibernate.initialize(ts);
            Hibernate.initialize(moneda);
        }
        return enviados;
    }

    @Override
    public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historial = getHistorialActivo(idCaja);
        Set<TransaccionCajaCaja> recibidos = historial.getTransaccionCajaCajasForIdCajaHistorialDestino();
        for (TransaccionCajaCaja ts : recibidos) {
            Moneda moneda = ts.getMoneda();

            Hibernate.initialize(ts.getHistorialCajaOrigen().getCaja());
            Hibernate.initialize(ts);
            Hibernate.initialize(moneda);
        }
        return recibidos;
    }

    @Override
    public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja(BigInteger idCaja,
            BigInteger idHistorialCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historial = historialCajaDAO.find(idHistorialCaja);
        if (!historial.getCaja().equals(caja))
            return null;
        QueryParameter queryParameter = QueryParameter
                .with("idHistorialCaja", historial.getIdHistorialCaja()).and("origen",
                        TransaccionBovedaCajaOrigen.CAJA);
        List<TransaccionBovedaCajaView> list = transaccionBovedaCajaViewDAO.findByNamedQuery(
                TransaccionBovedaCajaView.findByHistorialCajaEnviados, queryParameter.parameters());
        return list;
    }

    @Override
    public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja(BigInteger idCaja,
            BigInteger idHistorialCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historial = historialCajaDAO.find(idHistorialCaja);
        if (!historial.getCaja().equals(caja))
            return null;
        QueryParameter queryParameter = QueryParameter
                .with("idHistorialCaja", historial.getIdHistorialCaja()).and("origen",
                        TransaccionBovedaCajaOrigen.BOVEDA);
        List<TransaccionBovedaCajaView> list = transaccionBovedaCajaViewDAO.findByNamedQuery(
                TransaccionBovedaCajaView.findByHistorialCajaEnviados, queryParameter.parameters());
        return list;
    }

    @Override
    public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja(BigInteger idCaja,
            BigInteger idHistorialCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historial = historialCajaDAO.find(idHistorialCaja);
        if (!historial.getCaja().equals(caja))
            return null;
        Set<TransaccionCajaCaja> enviados = historial.getTransaccionCajaCajasForIdCajaHistorialOrigen();
        Hibernate.initialize(enviados);
        return enviados;
    }

    @Override
    public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja(BigInteger idCaja,
            BigInteger idHistorialCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historial = historialCajaDAO.find(idHistorialCaja);
        if (!historial.getCaja().equals(caja))
            return null;
        Set<TransaccionCajaCaja> enviados = historial.getTransaccionCajaCajasForIdCajaHistorialDestino();
        Hibernate.initialize(enviados);
        return enviados;
    }

    @Override
    public Set<CajaCierreMoneda> getVoucherCierreCaja(BigInteger idHistorial) {
        Set<CajaCierreMoneda> result;

        Agencia agencia = null;
        HistorialCaja historialCaja = historialCajaDAO.find(idHistorial);
        Caja caja = historialCaja.getCaja();

        // recuperando agencia
        Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
        for (BovedaCaja bovedaCaja : bovedaCajas) {
            agencia = bovedaCaja.getBoveda().getAgencia();
            break;
        }
        if (agencia == null)
            return null;

        // recuperando el historial del dia anterior
        HistorialCaja historialAyer = null;
        TypedQuery<HistorialCaja> query = em.getEm().createNamedQuery(HistorialCaja.findHistorialAnterior,
                HistorialCaja.class);
        query.setMaxResults(2);
        query.setParameter("idcaja", caja.getIdCaja());
        query.setParameter("fecha", historialCaja.getFechaCierre(), TemporalType.DATE);
        query.setParameter("hora", historialCaja.getHoraCierre(), TemporalType.TIMESTAMP);
        List<HistorialCaja> list2 = query.getResultList();

        for (HistorialCaja hist : list2) {
            if (!historialCaja.equals(hist)) {
                historialAyer = hist;
                break;
            }
        }

        // recuperando las monedas de la trasaccion
        Set<DetalleHistorialCaja> detalleHistorial = historialCaja.getDetalleHistorialCajas();
        Set<Moneda> monedasTransaccion = new HashSet<Moneda>();
        for (DetalleHistorialCaja detHistcaja : detalleHistorial) {
            Moneda moneda = detHistcaja.getMonedaDenominacion().getMoneda();
            if (!monedasTransaccion.contains(moneda)) {
                monedasTransaccion.add(moneda);
            }
        }

        // poniendo los datos por moneda
        result = new HashSet<CajaCierreMoneda>();
        for (Moneda moneda : monedasTransaccion) {

            CajaCierreMoneda cajaCierreMoneda = new CajaCierreMoneda();
            cajaCierreMoneda.setAgencia(agencia.getDenominacion());
            cajaCierreMoneda.setCaja(caja.getDenominacion());
            cajaCierreMoneda.setFechaApertura(historialCaja.getFechaApertura());
            cajaCierreMoneda.setFechaCierre(historialCaja.getFechaCierre());
            cajaCierreMoneda.setHoraApertura(historialCaja.getHoraApertura());
            cajaCierreMoneda.setHoraCierre(historialCaja.getHoraCierre());
            cajaCierreMoneda.setMoneda(moneda);
            cajaCierreMoneda.setTrabajador(historialCaja.getTrabajador());

            BigDecimal saldoAyer = BigDecimal.ZERO;
            BigDecimal entradas = BigDecimal.ZERO;
            BigDecimal salidas = BigDecimal.ZERO;
            BigDecimal porDevolver = BigDecimal.ZERO;
            BigDecimal sobrante = BigDecimal.ZERO;
            BigDecimal faltante = BigDecimal.ZERO;

            /*********** Añadiendo el detalle de una moneda ***************/
            result.add(cajaCierreMoneda);

            // poniendo el detalle
            Set<GenericDetalle> detalle = new TreeSet<GenericDetalle>();
            cajaCierreMoneda.setDetalle(detalle);
            for (DetalleHistorialCaja detHistcaja : detalleHistorial) {
                MonedaDenominacion denominacion = detHistcaja.getMonedaDenominacion();
                Moneda moneda2 = denominacion.getMoneda();
                BigInteger cantidad = detHistcaja.getCantidad();
                if (moneda.equals(moneda2)) {
                    if (cantidad.compareTo(BigInteger.ZERO) > 0) {
                        detalle.add(new GenericDetalle(denominacion.getValor(), detHistcaja.getCantidad()));
                    }
                }
            }

            // recuperando saldo del dia anterior
            if (historialAyer == null) {
                saldoAyer = BigDecimal.ZERO;
            } else {
                for (DetalleHistorialCaja detHistCaja : historialAyer.getDetalleHistorialCajas()) {
                    MonedaDenominacion denominacion = detHistCaja.getMonedaDenominacion();
                    Moneda moneda2 = denominacion.getMoneda();
                    if (moneda.equals(moneda2)) {
                        BigDecimal subTotal = denominacion.getValor().multiply(
                                new BigDecimal(detHistCaja.getCantidad()));
                        saldoAyer = saldoAyer.add(subTotal);
                    }
                }
            }

            // recuperando las operaciones del dia
            Set<TransaccionBancaria> transBancarias = historialCaja.getTransaccionBancarias();
            Set<TransaccionCompraVenta> transComVent = historialCaja.getTransaccionCompraVentas();
            Set<TransaccionCuentaAporte> transCtaAport = historialCaja.getTransaccionCuentaAportes();
            Set<TransaccionBovedaCaja> transBovCaj = historialCaja.getTransaccionBovedaCajas();
            Set<TransaccionCajaCaja> transCajCajEnviados = historialCaja
                    .getTransaccionCajaCajasForIdCajaHistorialOrigen();
            Set<TransaccionCajaCaja> transCajCajRecibidos = historialCaja
                    .getTransaccionCajaCajasForIdCajaHistorialDestino();

            for (TransaccionBancaria transBanc : transBancarias) {
                if (transBanc.getEstado()) {
                    Moneda moneda2 = transBanc.getCuentaBancaria().getMoneda();
                    if (moneda.equals(moneda2)) {
                        if (transBanc.getMonto().compareTo(BigDecimal.ZERO) >= 0)
                            entradas = entradas.add(transBanc.getMonto());
                        else
                            salidas = salidas.add(transBanc.getMonto().abs().negate());
                    }
                }
            }
            for (TransaccionCompraVenta transCompVent : transComVent) {
                if (transCompVent.getEstado()) {
                    Moneda monedaRecibida = transCompVent.getMonedaRecibida();
                    Moneda monedaEntregada = transCompVent.getMonedaEntregada();
                    if (moneda.equals(monedaRecibida)) {
                        entradas = entradas.add(transCompVent.getMontoRecibido());
                    }
                    if (moneda.equals(monedaEntregada)) {
                        salidas = salidas.add(transCompVent.getMontoEntregado().abs().negate());
                    }
                }
            }
            for (TransaccionCuentaAporte transAport : transCtaAport) {
                if (transAport.getEstado()) {
                    Moneda moneda2 = transAport.getCuentaAporte().getMoneda();
                    if (moneda.equals(moneda2)) {
                        if (transAport.getMonto().compareTo(BigDecimal.ZERO) >= 0)
                            entradas = entradas.add(transAport.getMonto());
                        else
                            salidas = salidas.add(transAport.getMonto().abs().negate());
                    }
                }
            }

            // entradas y salidas de transacciones internas
            for (TransaccionCajaCaja trans : transCajCajEnviados) {
                if (moneda.equals(trans.getMoneda())) {
                    if (trans.getEstadoSolicitud() && trans.getEstadoConfirmacion()) {
                        salidas = salidas.add(trans.getMonto().abs().negate());
                    }
                }
            }
            for (TransaccionCajaCaja trans : transCajCajRecibidos) {
                if (moneda.equals(trans.getMoneda())) {
                    if (trans.getEstadoSolicitud() && trans.getEstadoConfirmacion()) {
                        entradas = entradas.add(trans.getMonto());
                    }
                }
            }
            for (TransaccionBovedaCaja trans : transBovCaj) {
                Moneda moneda2 = trans.getHistorialBoveda().getBoveda().getMoneda();
                if (moneda.equals(moneda2)) {
                    if (trans.getEstadoSolicitud() && trans.getEstadoConfirmacion()) {
                        Set<TransaccionBovedaCajaDetalle> det = trans.getTransaccionBovedaCajaDetalls();
                        BigDecimal total = BigDecimal.ZERO;
                        for (TransaccionBovedaCajaDetalle d : det) {
                            BigDecimal valor = d.getMonedaDenominacion().getValor();
                            BigDecimal subtotal = valor.multiply(new BigDecimal(d.getCantidad()));
                            total = total.add(subtotal);
                        }
                        switch (trans.getOrigen()) {
                        case BOVEDA:
                            entradas = entradas.add(total);
                            break;
                        case CAJA:
                            salidas = salidas.add(total.abs().negate());
                            break;
                        default:
                            break;
                        }
                    }
                }
            }

            // recuperando faltantes y sobrantes
            Set<PendienteCaja> listPendientes = historialCaja.getPendientes();
            for (PendienteCaja pendiente : listPendientes) {
                Moneda moneda2 = pendiente.getMoneda();
                if (moneda.equals(moneda2)) {
                    if (pendiente.getMonto().compareTo(BigDecimal.ZERO) >= 0)
                        sobrante = sobrante.add(pendiente.getMonto());
                    else
                        faltante = faltante.add(pendiente.getMonto());
                }
            }

            // poner saldo por devolver
            porDevolver = entradas.add(salidas).add(saldoAyer);

            cajaCierreMoneda.setSaldoAyer(saldoAyer);
            cajaCierreMoneda.setEntradas(entradas);
            cajaCierreMoneda.setSalidas(salidas);
            cajaCierreMoneda.setPorDevolver(porDevolver);
            cajaCierreMoneda.setSobrante(sobrante);
            cajaCierreMoneda.setFaltante(faltante);
        }
        return result;
    }

    @Override
    public ResumenOperacionesCaja getResumenOperacionesCaja(BigInteger idHistorial) {
        Agencia agencia = null;
        HistorialCaja historialCaja = historialCajaDAO.find(idHistorial);
        Caja caja = historialCaja.getCaja();
        // recuperando agencia
        Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
        for (BovedaCaja bovedaCaja : bovedaCajas) {
            agencia = bovedaCaja.getBoveda().getAgencia();
            break;
        }
        if (agencia == null)
            return null;

        ResumenOperacionesCaja result = new ResumenOperacionesCaja();

        int depositosLibre = 0;
        int retirosLibre = 0;
        int depositosRecaudadora = 0;
        int retirosRecaudadora = 0;
        int depositosPlazoFijo = 0;
        int retirosPlazoFijo = 0;
        int depositosAporte = 0;
        int retirosAporte = 0;

        int compra = 0;
        int venta = 0;

        int depositosMayorCuantia = 0;
        int retirosMayorCuantia = 0;
        int compraVentaMayorCuantia = 0;

        int transCajaCajaRecibido = 0;
        int transCajaCajaEnviado = 0;
        int transBovedaCajaRecibido = 0;
        int transBovedaCajaEnviado = 0;

        int pendienteFaltante = 0;
        int pendienteSobrante = 0;
        
        int transCheque = 0;
        
        int transExtornadoDepositoRetiro = 0;
        int transExtornadoCompraVenta = 0;
        int transExtornadoAporte = 0;
        int transExtornadoCheque = 0;

        // recuperando las operaciones del dia
        Set<TransaccionBancaria> transBancarias = historialCaja.getTransaccionBancarias();
        Set<TransaccionCompraVenta> transComVent = historialCaja.getTransaccionCompraVentas();
        Set<TransaccionCuentaAporte> transCtaAport = historialCaja.getTransaccionCuentaAportes();

        Set<TransaccionCajaCaja> transCajaCajaEnviados = historialCaja
                .getTransaccionCajaCajasForIdCajaHistorialOrigen();
        Set<TransaccionCajaCaja> transCajaCajaRecibidos = historialCaja
                .getTransaccionCajaCajasForIdCajaHistorialDestino();
        Set<TransaccionBovedaCaja> transBovedaCaja = historialCaja.getTransaccionBovedaCajas();

        Set<PendienteCaja> transPendiente = historialCaja.getPendientes();
        
        Set<TransaccionCheque> transaccionCheque = historialCaja.getTransaccionCheque();

        VariableSistema varSoles = variableSistemaServiceNT
                .getVariable(Variable.TRANSACCION_MAYOR_CUANTIA_NUEVOS_SOLES);
        VariableSistema varDolares = variableSistemaServiceNT
                .getVariable(Variable.TRANSACCION_MAYOR_CUANTIA_DOLARES);
        VariableSistema varEuros = variableSistemaServiceNT
                .getVariable(Variable.TRANSACCION_MAYOR_CUANTIA_EUROS);

        for (TransaccionBancaria transBanc : transBancarias) {
            if (transBanc.getEstado()) {
                TipoCuentaBancaria tipoCuenta = transBanc.getCuentaBancaria().getTipoCuentaBancaria();

                if (tipoCuenta.equals(TipoCuentaBancaria.LIBRE)) {
                    if (transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
                        depositosLibre++;
                    else
                        retirosLibre++;
                }
                if (tipoCuenta.equals(TipoCuentaBancaria.RECAUDADORA)) {
                    if (transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
                        depositosRecaudadora++;
                    else
                        retirosRecaudadora++;
                }
                if (tipoCuenta.equals(TipoCuentaBancaria.PLAZO_FIJO)) {
                    if (transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
                        depositosPlazoFijo++;
                    else
                        retirosPlazoFijo++;
                }

                // mayor cuantia
                if (transBanc.getCuentaBancaria().getMoneda().getIdMoneda().equals(new BigInteger("1"))) {
                    if (transBanc.getMonto().abs().compareTo(varSoles.getValor()) >= 0)
                        if (transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
                            depositosMayorCuantia++;
                        else
                            retirosMayorCuantia++;
                }
                if (transBanc.getCuentaBancaria().getMoneda().getIdMoneda().equals(new BigInteger("2"))) {
                    if (transBanc.getMonto().abs().compareTo(varDolares.getValor()) >= 0)
                        if (transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
                            depositosMayorCuantia++;
                        else
                            retirosMayorCuantia++;
                }
                if (transBanc.getCuentaBancaria().getMoneda().getIdMoneda().equals(new BigInteger("3"))) {
                    if (transBanc.getMonto().abs().compareTo(varEuros.getValor()) >= 0)
                        if (transBanc.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO))
                            depositosMayorCuantia++;
                        else
                            retirosMayorCuantia++;
                }
            }else {
            	transExtornadoDepositoRetiro++;
            }
        }
        for (TransaccionCompraVenta transCompraVenta : transComVent) {
            if (transCompraVenta.getEstado()) {
                if (transCompraVenta.getTipoTransaccion().equals(Tipotransaccioncompraventa.COMPRA))
                    compra++;
                else
                    venta++;
            } else {
            	transExtornadoCompraVenta++; 
            }
        }
        for (TransaccionCuentaAporte trans : transCtaAport) {
            if (trans.getEstado()) {
                if (trans.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO)) {
                    depositosAporte++;
                } else {
                    retirosAporte++;
                }
            } else {
            	transExtornadoAporte++;
            }
        }

        // transCajaCajaEnviado = transCajaCajaEnviados.size();
        // transCajaCajaRecibido = transCajaCajaRecibidos.size();

        for (TransaccionCajaCaja transCajaCaja : transCajaCajaEnviados) {
            if (transCajaCaja.getEstadoSolicitud() && transCajaCaja.getEstadoConfirmacion()) {
                transCajaCajaEnviado++;
            }
        }
        for (TransaccionCajaCaja transCajaCaja : transCajaCajaRecibidos) {
            if (transCajaCaja.getEstadoSolicitud() && transCajaCaja.getEstadoConfirmacion()) {
                transCajaCajaRecibido++;
            }
        }

        for (TransaccionBovedaCaja transBovCaj : transBovedaCaja) {
            if (transBovCaj.getEstadoSolicitud() && transBovCaj.getEstadoConfirmacion()) {
                if (transBovCaj.getOrigen().equals(TransaccionBovedaCajaOrigen.CAJA))
                    transBovedaCajaEnviado++;
                else
                    transBovedaCajaRecibido++;
            }
        }

        for (PendienteCaja pendiente : transPendiente) {
            if (pendiente.getTipoPendiente().equals(TipoPendienteCaja.FALTANTE))
                pendienteFaltante++;
            else
                pendienteSobrante++;
        }
        
        for (TransaccionCheque trans : transaccionCheque) {
            if (trans.getEstado())
            	transCheque++;
            else
                transExtornadoCheque++;
        }

        result.setAgencia(agencia.getDenominacion());
        result.setCaja(caja.getDenominacion());
        result.setFechaApertura(historialCaja.getFechaApertura());
        result.setHoraApertura(historialCaja.getHoraApertura());
        result.setFechaCierre(historialCaja.getFechaCierre());
        result.setHoraCierre(historialCaja.getHoraCierre());
        result.setTrabajador(historialCaja.getTrabajador());

        result.setDepositosLibre(depositosLibre);
        result.setRetirosLibre(retirosLibre);
        result.setDepositosRecaudadora(depositosRecaudadora);
        result.setRetirosRecaudadora(retirosRecaudadora);
        result.setDepositosPlazoFijo(depositosPlazoFijo);
        result.setRetirosPlazoFijo(retirosPlazoFijo);
        result.setDepositosAporte(depositosAporte);
        result.setRetirosAporte(retirosAporte);

        result.setCompra(compra);
        result.setVenta(venta);
        
        result.setTransExtornadoDepositoRetiro(transExtornadoDepositoRetiro);
        result.setTransExtornadoCompraVenta(transExtornadoCompraVenta);
        result.setTransExtornadoAporte(transExtornadoAporte);
        result.setTransExtornadoCheque(transExtornadoCheque);
        
        result.setDepositoMayorCuantia(depositosMayorCuantia);
        result.setRetiroMayorCuantia(retirosMayorCuantia);
        result.setCompraVentaMayorCuantia(compraVentaMayorCuantia);

        result.setEnviadoCajaCaja(transCajaCajaEnviado);
        result.setRecibidoCajaCaja(transCajaCajaRecibido);
        result.setEnviadoBovedaCaja(transBovedaCajaEnviado);
        result.setRecibidoBovedaCaja(transBovedaCajaRecibido);

        result.setPendienteFaltante(pendienteFaltante);
        result.setPendienteSobrante(pendienteSobrante);
        
        result.setTransaccionCheque(transCheque);

        return result;
    }

    @Override
    public VoucherTransaccionCuentaAporte getVoucherCuentaAporte(BigInteger idTransaccion) {
        VoucherTransaccionCuentaAporte voucherTransaccion = new VoucherTransaccionCuentaAporte();

        // recuperando transaccion
        TransaccionCuentaAporte transaccionCuentaAporte = transaccionCuentaAporteDAO.find(idTransaccion);
        CuentaAporte cuentaAporte = transaccionCuentaAporte.getCuentaAporte();
        Socio socio = new Socio();
        Set<Socio> socios = cuentaAporte.getSocios();
        if (socios.size() == 1) {
            for (Socio socioBuscado : socios) {
                socio = socioBuscado;
            }
        }
        Caja caja = transaccionCuentaAporte.getHistorialCaja().getCaja();
        Set<BovedaCaja> list = caja.getBovedaCajas();
        Agencia agencia = null;
        for (BovedaCaja bovedaCaja : list) {
            agencia = bovedaCaja.getBoveda().getAgencia();
            break;
        }

        // Poniendo datos de transaccion
        Moneda moneda = transaccionCuentaAporte.getCuentaAporte().getMoneda();
        Hibernate.initialize(moneda);
        voucherTransaccion.setMoneda(moneda);

        voucherTransaccion.setIdTransaccion(transaccionCuentaAporte.getIdTransaccionCuentaAporte());
        voucherTransaccion.setFecha(transaccionCuentaAporte.getFecha());
        voucherTransaccion.setHora(transaccionCuentaAporte.getHora());
        voucherTransaccion.setNumeroOperacion(transaccionCuentaAporte.getNumeroOperacion());
        voucherTransaccion.setMonto(transaccionCuentaAporte.getMonto());
        voucherTransaccion.setReferencia(transaccionCuentaAporte.getReferencia());
        voucherTransaccion.setTipoTransaccion(transaccionCuentaAporte.getTipoTransaccion());

        // Poniendo datos de cuenta bancaria
        voucherTransaccion.setNumeroCuenta(cuentaAporte.getNumeroCuenta());
        voucherTransaccion.setSaldoDisponible(cuentaAporte.getSaldo());
        voucherTransaccion.setObservacion(transaccionCuentaAporte.getObservacion());

        // Poniendo datos de agencia
        voucherTransaccion.setAgenciaDenominacion(agencia.getDenominacion());
        voucherTransaccion.setAgenciaAbreviatura(agencia.getAbreviatura());

        // Poniendo datos de caja
        voucherTransaccion.setCajaDenominacion(caja.getDenominacion());
        voucherTransaccion.setCajaAbreviatura(caja.getAbreviatura());

        // Poniendo datos del socio
        PersonaNatural personaNatural = socio.getPersonaNatural();
        PersonaJuridica personaJuridica = socio.getPersonaJuridica();
        if (personaJuridica == null) {
            voucherTransaccion.setIdSocio(socio.getIdSocio());
            voucherTransaccion.setTipoDocumento(socio.getPersonaNatural().getTipoDocumento()); //
            voucherTransaccion.setNumeroDocumento(socio.getPersonaNatural().getNumeroDocumento());
            voucherTransaccion.setSocio(personaNatural.getApellidoPaterno() + " "
                    + personaNatural.getApellidoMaterno() + ", " + personaNatural.getNombres());
        }
        if (personaNatural == null) {
            voucherTransaccion.setIdSocio(socio.getIdSocio());
            voucherTransaccion.setTipoDocumento(socio.getPersonaJuridica().getTipoDocumento()); //
            voucherTransaccion.setNumeroDocumento(socio.getPersonaJuridica().getNumeroDocumento());
            voucherTransaccion.setSocio(personaJuridica.getRazonSocial());
        }
        return voucherTransaccion;
    }

    @Override
    public VoucherTransaccionBancaria getVoucherTransaccionBancaria(BigInteger idTransaccionBancaria) {
        VoucherTransaccionBancaria voucherTransaccion = new VoucherTransaccionBancaria();

        // recuperando transaccion
        TransaccionBancaria transaccionBancaria = transaccionBancariaDAO.find(idTransaccionBancaria);
        CuentaBancaria cuentaBancaria = transaccionBancaria.getCuentaBancaria();
        Socio socio = cuentaBancaria.getSocio();
        Caja caja = transaccionBancaria.getHistorialCaja().getCaja();
        Set<BovedaCaja> list = caja.getBovedaCajas();
        Agencia agencia = null;
        for (BovedaCaja bovedaCaja : list) {
            agencia = bovedaCaja.getBoveda().getAgencia();
            break;
        }

        // añadir titulares para las firmas
        Set<Titular> titulares = cuentaBancaria.getTitulars();
        String titularesCadena = "";
        for (Titular titular : titulares) {
            PersonaNatural pn = titular.getPersonaNatural();
            titularesCadena = titularesCadena + pn.getNombres() + " " + pn.getApellidoPaterno() + " y ";
        }
        titularesCadena = titularesCadena.substring(0, titularesCadena.length() - 2);
        voucherTransaccion.setTitulares(titularesCadena);

        // Poniendo datos de transaccion
        voucherTransaccion.setIdTransaccionBancaria(transaccionBancaria.getIdTransaccionBancaria());
        Moneda moneda = transaccionBancaria.getMoneda();
        Hibernate.initialize(moneda);
        voucherTransaccion.setMoneda(moneda);

        voucherTransaccion.setFecha(transaccionBancaria.getFecha());
        voucherTransaccion.setHora(transaccionBancaria.getHora());
        voucherTransaccion.setNumeroOperacion(transaccionBancaria.getNumeroOperacion());
        voucherTransaccion.setMonto(transaccionBancaria.getMonto());
        voucherTransaccion.setReferencia(transaccionBancaria.getReferencia());
        voucherTransaccion.setTipoTransaccion(transaccionBancaria.getTipoTransaccion());
        voucherTransaccion.setObservacion(transaccionBancaria.getObservacion());

        // Poniendo datos de cuenta bancaria
        voucherTransaccion.setTipoCuentaBancaria(cuentaBancaria.getTipoCuentaBancaria());
        voucherTransaccion.setNumeroCuenta(cuentaBancaria.getNumeroCuenta());
        voucherTransaccion.setSaldoDisponible(cuentaBancaria.getSaldo());

        // Calculo de interes
        Date fechaActual = Calendar.getInstance().getTime();
        Date desde = DateUtils.sumarRestarDiasFecha(fechaActual, -1 * DateUtils.getDayOfMoth(fechaActual));
        Date hasta = Calendar.getInstance().getTime();
        hasta = DateUtils.sumarRestarDiasFecha(hasta, 1);
        QueryParameter queryParameter = QueryParameter
                .with("idCuentaBancaria", cuentaBancaria.getIdCuentaBancaria())
                .and("desde", DateUtils.getDateIn00Time(desde))
                .and("hasta", DateUtils.getDateIn00Time(hasta));
        List<CuentaBancariaInteresGenera> intereses = cuentaBancariaInteresGeneraDAO.findByNamedQuery(
                CuentaBancariaInteresGenera.findByIdAndDate, queryParameter.parameters());
        BigDecimal interesTotal = BigDecimal.ZERO;
        for (CuentaBancariaInteresGenera cuentaBancariaInteresGenera : intereses) {
            interesTotal = interesTotal.add(cuentaBancariaInteresGenera.getInteresGenerado());
        }
        interesTotal = interesTotal.setScale(2, RoundingMode.FLOOR);
        voucherTransaccion.setInteres(interesTotal);

        // Poniendo datos de agencia
        voucherTransaccion.setAgenciaDenominacion(agencia.getDenominacion());
        voucherTransaccion.setAgenciaAbreviatura(agencia.getAbreviatura());

        // Poniendo datos de caja
        voucherTransaccion.setCajaDenominacion(caja.getDenominacion());
        voucherTransaccion.setCajaAbreviatura(caja.getAbreviatura());

        // Poniendo datos del socio
        PersonaNatural personaNatural = socio.getPersonaNatural();
        PersonaJuridica personaJuridica = socio.getPersonaJuridica();
        if (personaJuridica == null) {
            voucherTransaccion.setIdSocio(socio.getIdSocio());
            voucherTransaccion.setTipoDocumento(socio.getPersonaNatural().getTipoDocumento()); //
            voucherTransaccion.setNumeroDocumento(socio.getPersonaNatural().getNumeroDocumento());
            voucherTransaccion.setSocio(personaNatural.getApellidoPaterno() + " "
                    + personaNatural.getApellidoMaterno() + ", " + personaNatural.getNombres());
        }
        if (personaNatural == null) {
            voucherTransaccion.setIdSocio(socio.getIdSocio());
            voucherTransaccion.setTipoDocumento(socio.getPersonaJuridica().getTipoDocumento()); //
            voucherTransaccion.setNumeroDocumento(socio.getPersonaJuridica().getNumeroDocumento());
            voucherTransaccion.setSocio(personaJuridica.getRazonSocial());
        }
        return voucherTransaccion;
    }

    @Override
    public VoucherTransferenciaBancaria getVoucherTransferenciaBancaria(BigInteger idTransferencia) {
        VoucherTransferenciaBancaria voucher = new VoucherTransferenciaBancaria();

        // recuperando transaccion
        TransferenciaBancaria transferenciaBancaria = transferenciaBancariaDAO.find(idTransferencia);
        CuentaBancaria cuentaOrigen = transferenciaBancaria.getCuentaBancariaOrigen();
        CuentaBancaria cuentaDestino = transferenciaBancaria.getCuentaBancariaDestino();
        Socio socioOrigen = cuentaOrigen.getSocio();
        // Socio socioDestino = cuentaDestino.getSocio();
        Caja caja = transferenciaBancaria.getHistorialCaja().getCaja();
        Set<BovedaCaja> list = caja.getBovedaCajas();
        Agencia agencia = null;
        for (BovedaCaja bovedaCaja : list) {
            agencia = bovedaCaja.getBoveda().getAgencia();
            break;
        }

        // Poniendo datos de transaccion
        voucher.setIdTransferenciaBancaria(transferenciaBancaria.getIdTransferenciaBancaria());
        Moneda moneda = cuentaOrigen.getMoneda();
        Hibernate.initialize(moneda);
        voucher.setMoneda(moneda);
        voucher.setFecha(transferenciaBancaria.getFecha());
        voucher.setHora(transferenciaBancaria.getHora());
        voucher.setNumeroOperacion(transferenciaBancaria.getNumeroOperacion());
        voucher.setMonto(transferenciaBancaria.getMonto());
        voucher.setReferencia(transferenciaBancaria.getReferencia());
        voucher.setTipoTransaccion("TRANSFERENCIA");
        voucher.setObservacion(transferenciaBancaria.getObservacion());

        // Poniendo datos de cuenta bancaria
        voucher.setNumeroCuentaOrigen(cuentaOrigen.getNumeroCuenta());
        voucher.setNumeroCuentaDestino(cuentaDestino.getNumeroCuenta());

        // Poniendo datos de agencia
        voucher.setAgenciaDenominacion(agencia.getDenominacion());
        voucher.setAgenciaAbreviatura(agencia.getAbreviatura());

        // Poniendo datos de caja
        voucher.setCajaDenominacion(caja.getDenominacion());
        voucher.setCajaAbreviatura(caja.getAbreviatura());

        // Poniendo datos del socio

        PersonaNatural personaNatural = socioOrigen.getPersonaNatural();
        PersonaJuridica personaJuridica = socioOrigen.getPersonaJuridica();
        if (personaJuridica == null) {
            voucher.setIdSocio(socioOrigen.getIdSocio());
            voucher.setTipoDocumento(socioOrigen.getPersonaNatural().getTipoDocumento()); //
            voucher.setNumeroDocumento(socioOrigen.getPersonaNatural().getNumeroDocumento());
            voucher.setSocio(personaNatural.getApellidoPaterno() + " " + personaNatural.getApellidoMaterno()
                    + ", " + personaNatural.getNombres());
        }
        if (personaNatural == null) {
            voucher.setIdSocio(socioOrigen.getIdSocio());
            voucher.setTipoDocumento(socioOrigen.getPersonaJuridica().getTipoDocumento()); //
            voucher.setNumeroDocumento(socioOrigen.getPersonaJuridica().getNumeroDocumento());
            voucher.setSocio(personaJuridica.getRazonSocial());
        }
        return voucher;
    }

    @Override
    public VoucherCompraVenta getVoucherCompraVenta(BigInteger idTransaccionCompraVenta) {
        VoucherCompraVenta voucherCompraVenta = new VoucherCompraVenta();

        // recuperando transaccion
        TransaccionCompraVenta compraVenta = transaccionCopraVentaDAO.find(idTransaccionCompraVenta);

        Caja caja = compraVenta.getHistorialCaja().getCaja();
        Set<BovedaCaja> list = caja.getBovedaCajas();
        Agencia agencia = null;
        for (BovedaCaja bovedaCaja : list) {
            agencia = bovedaCaja.getBoveda().getAgencia();
            break;
        }

        // Poniendo datos de transaccion
        Moneda monedaEntregada = compraVenta.getMonedaEntregada();
        Moneda monedaRecibida = compraVenta.getMonedaRecibida();
        Hibernate.initialize(monedaEntregada);
        Hibernate.initialize(monedaRecibida);

        voucherCompraVenta.setIdCompraVenta(compraVenta.getIdTransaccionCompraVenta());
        voucherCompraVenta.setFecha(compraVenta.getFecha());
        voucherCompraVenta.setHora(compraVenta.getHora());
        voucherCompraVenta.setMonedaEntregada(monedaEntregada);
        voucherCompraVenta.setMonedaRecibida(monedaRecibida);
        voucherCompraVenta.setMontoRecibido(compraVenta.getMontoRecibido());
        voucherCompraVenta.setMontoEntregado(compraVenta.getMontoEntregado());
        voucherCompraVenta.setEstado(compraVenta.getEstado());

        voucherCompraVenta.setNumeroOperacion(compraVenta.getNumeroOperacion());
        voucherCompraVenta.setObservacion(compraVenta.getObservacion());
        voucherCompraVenta.setReferencia(compraVenta.getCliente());
        voucherCompraVenta.setTipoCambio(compraVenta.getTipoCambio());
        voucherCompraVenta.setTipoTransaccion(compraVenta.getTipoTransaccion());

        // Poniendo datos de agencia
        voucherCompraVenta.setAgenciaDenominacion(agencia.getDenominacion());
        voucherCompraVenta.setAgenciaAbreviatura(agencia.getAbreviatura());

        // Poniendo datos de caja
        voucherCompraVenta.setCajaDenominacion(caja.getDenominacion());
        voucherCompraVenta.setCajaAbreviatura(caja.getAbreviatura());

        return voucherCompraVenta;
    }

    @Override
    public VoucherTransaccionCheque getVoucherTransaccionCheque(BigInteger idTransaccionCheque) {
        VoucherTransaccionCheque voucherTransaccion = new VoucherTransaccionCheque();

        // recuperando transaccion
        TransaccionCheque transaccionCheque = transaccionChequeDAO.find(idTransaccionCheque);
        Cheque cheque = transaccionCheque.getCheque();
        Chequera chequera = cheque.getChequera();
        CuentaBancaria cuentaBancaria = chequera.getCuentaBancaria();

        Socio socio = cuentaBancaria.getSocio();
        Caja caja = transaccionCheque.getHistorialCaja().getCaja();
        Set<BovedaCaja> list = caja.getBovedaCajas();
        Agencia agencia = null;
        for (BovedaCaja bovedaCaja : list) {
            agencia = bovedaCaja.getBoveda().getAgencia();
            break;
        }

        // añadir titulares para las firmas
        Set<Titular> titulares = cuentaBancaria.getTitulars();
        String titularesCadena = "";
        for (Titular titular : titulares) {
            PersonaNatural pn = titular.getPersonaNatural();
            titularesCadena = titularesCadena + pn.getNombres() + " " + pn.getApellidoPaterno() + " y ";
        }
        titularesCadena = titularesCadena.substring(0, titularesCadena.length() - 2);

        voucherTransaccion.setTitulares(titularesCadena);

        // Poniendo datos de transaccion
        voucherTransaccion.setIdTransaccionBancaria(transaccionCheque.getIdTransaccionCheque());

        Moneda moneda = cuentaBancaria.getMoneda();
        Hibernate.initialize(moneda);
        voucherTransaccion.setMoneda(moneda);

        voucherTransaccion.setNumeroCheque(cheque.getNumeroChequeUnico().toString());

        voucherTransaccion.setFecha(transaccionCheque.getFecha());
        voucherTransaccion.setHora(transaccionCheque.getHora());
        voucherTransaccion.setNumeroOperacion(transaccionCheque.getNumeroOperacion());
        voucherTransaccion.setMonto(transaccionCheque.getMonto());
        voucherTransaccion.setObservacion(transaccionCheque.getObservacion());

        // Poniendo datos de cuenta bancaria
        voucherTransaccion.setTipoCuentaBancaria(cuentaBancaria.getTipoCuentaBancaria());
        voucherTransaccion.setNumeroCuenta(cuentaBancaria.getNumeroCuenta());
        voucherTransaccion.setSaldoDisponible(cuentaBancaria.getSaldo());

        // Poniendo datos de agencia
        voucherTransaccion.setAgenciaDenominacion(agencia.getDenominacion());
        voucherTransaccion.setAgenciaAbreviatura(agencia.getAbreviatura());

        // Poniendo datos de caja
        voucherTransaccion.setCajaDenominacion(caja.getDenominacion());
        voucherTransaccion.setCajaAbreviatura(caja.getAbreviatura());

        // Poniendo datos del socio
        PersonaNatural personaNatural = socio.getPersonaNatural();
        PersonaJuridica personaJuridica = socio.getPersonaJuridica();
        if (personaJuridica == null) {
        	TipoDocumento t = socio.getPersonaNatural().getTipoDocumento();
        	Hibernate.initialize(t);
            voucherTransaccion.setIdSocio(socio.getIdSocio());
            voucherTransaccion.setTipoDocumento(t); //
            voucherTransaccion.setNumeroDocumento(socio.getPersonaNatural().getNumeroDocumento());
            voucherTransaccion.setSocio(personaNatural.getApellidoPaterno() + " "
                    + personaNatural.getApellidoMaterno() + ", " + personaNatural.getNombres());
        }
        if (personaNatural == null) {
        	TipoDocumento t = socio.getPersonaJuridica().getTipoDocumento();
        	Hibernate.initialize(t);
            voucherTransaccion.setIdSocio(socio.getIdSocio());
            voucherTransaccion.setTipoDocumento(t); //
            voucherTransaccion.setNumeroDocumento(socio.getPersonaJuridica().getNumeroDocumento());
            voucherTransaccion.setSocio(personaJuridica.getRazonSocial());
        }
        return voucherTransaccion;
    }

    @Override
    public Set<PendienteCaja> getPendientes(BigInteger idCaja, BigInteger idHistorialCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;
        HistorialCaja historialCaja = null;
        if (idHistorialCaja != null)
            historialCaja = historialCajaDAO.find(idHistorialCaja);
        if (historialCaja != null && !historialCaja.getCaja().equals(caja))
            return null;

        Set<PendienteCaja> result = null;
        if (historialCaja != null) {
            result = historialCaja.getPendientes();
        } else {
            // QueryParameter queryParameter = QueryParameter.with("idCaja",
            // idCaja);
            historialCaja = getHistorialActivo(caja.getIdCaja());
            result = historialCaja.getPendientes();
            // List<PendienteCaja> list =
            // pendienteCajaDAO.findByNamedQuery(PendienteCaja.findByIdCaja,
            // queryParameter.parameters());
            // result = new HashSet<>(list);
        }

        for (PendienteCaja pendienteCaja : result) {
            Moneda moneda = pendienteCaja.getMoneda();
            Hibernate.initialize(pendienteCaja);
            Hibernate.initialize(moneda);
        }
        return result;
    }

    @Override
    public List<PendienteCajaFaltanteView> getPendientesPorPagar(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            return null;

        QueryParameter queryParameter = QueryParameter.with("idCaja", idCaja);

        List<PendienteCajaFaltanteView> result = pendienteCajaFaltanteViewDAO.findByNamedQuery(
                PendienteCajaFaltanteView.findByIdCajaConMonto, queryParameter.parameters());

        for (PendienteCajaFaltanteView pendienteCaja : result) {
            Moneda moneda = pendienteCaja.getMoneda();
            Hibernate.initialize(pendienteCaja);
            Hibernate.initialize(moneda);
        }
        return result;
    }

    @Override
    public List<HistorialTransaccionCaja> getHistorialTransaccion(BigInteger idCaja, BigInteger idHistorial,
            String filterText) {
        Caja caja = cajaDAO.find(idCaja);
        HistorialCaja historial = null;
        if (caja == null) {
            return null;
        }
        if (idHistorial != null) {
            historial = historialCajaDAO.find(idHistorial);
        } else {
            historial = getHistorialActivo(idCaja);
        }
        if (historial == null || !historial.getCaja().equals(caja)) {
            return null;
        }

        if (filterText != null) {
            QueryParameter queryParameter = QueryParameter.with("filterText", "%" + filterText + "%").and(
                    "idHistorialCaja", historial.getIdHistorialCaja());
            List<HistorialTransaccionCaja> list = historialTransaccionCajaDAO.findByNamedQuery(
                    HistorialTransaccionCaja.findByTransaccion, queryParameter.parameters());
            return list;
        } else {
            QueryParameter queryParameter = QueryParameter.with("idHistorialCaja",
                    historial.getIdHistorialCaja());
            List<HistorialTransaccionCaja> list = historialTransaccionCajaDAO.findByNamedQuery(
                    HistorialTransaccionCaja.findByHistorialCaja, queryParameter.parameters());
            return list;
        }
    }

    @Override
    public List<CajaView> findAllView(BigInteger idAgencia) {
        List<CajaView> list = null;
        if (idAgencia == null) {
            list = cajaViewDAO.findAll();
        } else {
            QueryParameter queryParameter = QueryParameter.with("idAgencia", idAgencia);
            List<CajaView> a = cajaViewDAO.findByNamedQuery(CajaView.findByIdAgencia,
                    queryParameter.parameters());
            list = new ArrayList<CajaView>(a);
        }
        return list;
    }

    @Override
    public List<Trabajador> getTrabajadores(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
        if (caja != null) {
            Set<TrabajadorCaja> trabajadorCajas = caja.getTrabajadorCajas();
            List<Trabajador> trabajadores = new ArrayList<Trabajador>();
            for (TrabajadorCaja trabajadorCaja : trabajadorCajas) {
                Trabajador trabajador = trabajadorCaja.getTrabajador();
                PersonaNatural personaNatural = trabajador.getPersonaNatural();
                TipoDocumento tipoDocumento = personaNatural.getTipoDocumento();
                Hibernate.initialize(personaNatural);
                Hibernate.initialize(tipoDocumento);
                trabajadores.add(trabajador);
            }
            return trabajadores;
        } else {
            return null;
        }
    }

}
