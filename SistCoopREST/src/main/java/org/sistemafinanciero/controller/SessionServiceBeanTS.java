package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.BovedaCaja;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.Cheque;
import org.sistemafinanciero.entity.Chequera;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.DetalleHistorialBoveda;
import org.sistemafinanciero.entity.DetalleHistorialCaja;
import org.sistemafinanciero.entity.Giro;
import org.sistemafinanciero.entity.HistorialBoveda;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.HistorialPagoSobreGiro;
import org.sistemafinanciero.entity.HistorialPagoSobreGiroBancario;
import org.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.entity.PendienteCaja;
import org.sistemafinanciero.entity.PendienteCajaFaltanteView;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.SobreGiro;
import org.sistemafinanciero.entity.SobreGiroBancario;
import org.sistemafinanciero.entity.Socio;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TransaccionBancaria;
import org.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.sistemafinanciero.entity.TransaccionBovedaCajaDetalle;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.TransaccionCajaCaja;
import org.sistemafinanciero.entity.TransaccionCheque;
import org.sistemafinanciero.entity.TransaccionCompraVenta;
import org.sistemafinanciero.entity.TransaccionCuentaAporte;
import org.sistemafinanciero.entity.TransferenciaBancaria;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.type.EstadoCheque;
import org.sistemafinanciero.entity.type.EstadoCuentaAporte;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.EstadoGiro;
import org.sistemafinanciero.entity.type.EstadoSobreGiro;
import org.sistemafinanciero.entity.type.EstadoSobreGiroBancario;
import org.sistemafinanciero.entity.type.LugarPagoComision;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPendienteCaja;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.entity.type.Tipotransaccionbancaria;
import org.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.sistemafinanciero.exception.IllegalResultException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.nt.MonedaServiceNT;
import org.sistemafinanciero.service.ts.CuentaBancariaServiceTS;
import org.sistemafinanciero.service.ts.SessionServiceTS;
import org.sistemafinanciero.service.ts.SocioServiceTS;
import org.sistemafinanciero.util.AllowedTo;
import org.sistemafinanciero.util.AllowedToEstadoMovimiento;
import org.sistemafinanciero.util.DateUtils;
import org.sistemafinanciero.util.EntityManagerProducer;
import org.sistemafinanciero.util.EstadoMovimiento;
import org.sistemafinanciero.util.Guard;
import org.sistemafinanciero.util.GuardEstadoMovimiento;
import org.sistemafinanciero.util.Permission;
import org.sistemafinanciero.util.UsuarioSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Named
@Interceptors(value = { Guard.class, GuardEstadoMovimiento.class })
@Remote(SessionServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SessionServiceBeanTS implements SessionServiceTS {

    @Inject
    private Validator validator;

    @Inject
    private DAO<Object, Socio> socioDAO;

    @Inject
    private DAO<Object, Boveda> bovedaDAO;

    @Inject
    private DAO<Object, HistorialBoveda> historialBovedaDAO;

    @Inject
    private DAO<Object, DetalleHistorialBoveda> detalleHistorialBovedaDAO;

    @Inject
    private DAO<Object, Caja> cajaDAO;

    @Inject
    private DAO<Object, HistorialCaja> historialCajaDAO;

    @Inject
    private DAO<Object, DetalleHistorialCaja> detalleHistorialCajaDAO;

    @Inject
    private DAO<Object, Trabajador> trabajadorDAO;

    @Inject
    private DAO<Object, CuentaAporte> cuentaAporteDAO;

    @Inject
    private DAO<Object, TransaccionCuentaAporte> transaccionCuentaAporteDAO;

    @Inject
    private DAO<Object, TransaccionBancaria> transaccionBancariaDAO;

    @Inject
    private DAO<Object, TransaccionCompraVenta> transaccionCompraVentaDAO;

    @Inject
    private DAO<Object, TransferenciaBancaria> transferenciaBancariaDAO;

    @Inject
    private DAO<Object, TransaccionBovedaCaja> transaccionBovedaCajaDAO;

    @Inject
    private DAO<Object, TransaccionCajaCaja> transaccionCajaCajaDAO;

    @Inject
    private DAO<Object, TransaccionBovedaCajaDetalle> detalleTransaccionBovedaCajaDAO;

    @Inject
    private DAO<Object, TransaccionBovedaCajaView> transaccionBovedaCajaViewDAO;

    @Inject
    private DAO<Object, TransaccionCheque> transaccionChequeDAO;

    @Inject
    private DAO<Object, Cheque> chequeDAO;

    @Inject
    private DAO<Object, HistorialTransaccionCaja> historialTransaccionCajaDAO;

    @Inject
    private DAO<Object, PendienteCaja> pendienteCajaDAO;

    @Inject
    private DAO<Object, PendienteCajaFaltanteView> pendienteCajaFaltanteViewDAO;

    @Inject
    private DAO<Object, Moneda> monedaDAO;

    @Inject
    private DAO<Object, BovedaCaja> bovedaCajaDAO;

    @Inject
    private DAO<Object, CuentaBancaria> cuentaBancariaDAO;

    @Inject
    private DAO<Object, Agencia> agenciaDAO;

    @Inject
    private DAO<Object, Giro> giroDAO;

    @Inject
    private DAO<Object, SobreGiro> sobreGiroDAO;

    @Inject
    private DAO<Object, SobreGiroBancario> sobreGiroBancarioDAO;

    @Inject
    private DAO<Object, HistorialPagoSobreGiro> historialPagoSobreGiroDAO;

    @Inject
    private DAO<Object, HistorialPagoSobreGiroBancario> historialPagoSobreGiroBancarioDAO;

    @Inject
    private EntityManagerProducer em;

    @Inject
    private UsuarioSession usuarioSession;

    @EJB
    private MonedaServiceNT monedaServiceNT;

    @EJB
    private CuentaBancariaServiceTS cuentaBancariaServiceTS;

    @EJB
    private SocioServiceTS socioServiceTS;

    private Logger LOGGER = LoggerFactory.getLogger(SessionServiceBeanTS.class);

    private Trabajador getTrabajador() {
        String username = usuarioSession.getUsername();

        QueryParameter queryParameter = QueryParameter.with("username", username);
        List<Trabajador> list = trabajadorDAO.findByNamedQuery(Trabajador.findByUsername,
                queryParameter.parameters());
        if (list.size() <= 1) {
            Trabajador trabajador = null;
            for (Trabajador t : list) {
                trabajador = t;
            }
            return trabajador;
        } else {
            System.out.println("Error: mas de un usuario registrado");
            return null;
        }
    }

    private Caja getCaja() {
        String username = usuarioSession.getUsername();
        QueryParameter queryParameter = QueryParameter.with("username", username);
        List<Caja> list = cajaDAO.findByNamedQuery(Caja.findByUsername, queryParameter.parameters());
        if (list.size() <= 1) {
            Caja caja = null;
            for (Caja c : list) {
                caja = c;
            }
            return caja;
        } else {
            System.out.println("Error: mas de un usuario registrado");
            return null;
        }
    }

    private HistorialCaja getHistorialActivo() {
        Caja caja = getCaja();
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

    private HistorialCaja getHistorialActivo(BigInteger idCaja) {
        Caja caja = cajaDAO.find(idCaja);
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

    private BigInteger getNumeroOperacion() {
        Agencia agencia = new Agencia();
        Caja caja = this.getCaja();
        Set<BovedaCaja> lisBC = caja.getBovedaCajas();
        for (BovedaCaja bovedaCaja : lisBC) {
            agencia = bovedaCaja.getBoveda().getAgencia();
        }

        Query query = em
                .getEm()
                .createNativeQuery(
                        "SELECT T.Numero_Operacion AS numero_operacion FROM Transaccion_Bancaria T Inner Join Historial_Caja HC on (HC.id_historial_caja = T.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and T.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) "
                                + "union "
                                + "select Tcv.Numero_Operacion AS numero_operacion From Transaccion_Compra_Venta TCV Inner Join Historial_Caja HC on (HC.id_historial_caja = Tcv.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and Tcv.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) "
                                + "union "
                                + "select Tb.Numero_Operacion AS numero_operacion From Transferencia_Bancaria TB Inner Join Historial_Caja HC on (HC.id_historial_caja = Tb.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and Tb.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) "
                                + "union "
                                + "select Ap.Numero_Operacion AS numero_operacion from Transaccion_Cuenta_Aporte AP Inner Join Historial_Caja HC on (HC.id_historial_caja = AP.id_historial_caja) Inner Join Caja C on (HC.Id_Caja = C.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.id_caja) Inner Join Boveda B on (B.id_boveda = BC.id_boveda) where B.Id_Agencia = :idagencia and ap.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) "
                                + "union "
                                + "select TCH.Numero_Operacion AS numero_operacion from Transaccion_Cheque TCH Inner Join Historial_Caja HC on (HC.id_historial_caja = TCH.id_historial_caja) Inner Join Caja C on (HC.Id_Caja = C.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.id_caja) Inner Join Boveda B on (B.id_boveda = BC.id_boveda) where B.Id_Agencia = :idagencia and TCH.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) ORDER BY numero_operacion DESC");
        query.setParameter("idagencia", agencia.getIdAgencia());

        List<BigDecimal> list = query.getResultList();
        if (list.size() == 0) {
            return BigInteger.ONE;
        } else {
            BigDecimal op = list.get(0);
            BigInteger numero_operacion = op.toBigInteger();
            return numero_operacion.add(BigInteger.ONE);
        }
    }

    private void actualizarSaldoCaja(BigDecimal monto, BigInteger idMoneda) throws RollbackFailureException {
        Moneda monedaTransaccion = monedaDAO.find(idMoneda);
        Caja caja = this.getCaja();
        Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();

        boolean commit = false;

        for (BovedaCaja bovedaCaja : bovedasCajas) {
            Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
            if (monedaTransaccion.equals(monedaBoveda)) {
                BigDecimal saldoActual = bovedaCaja.getSaldo();
                BigDecimal saldoFinal = saldoActual.add(monto);
                if (saldoFinal.compareTo(BigDecimal.ZERO) >= 0) {
                    bovedaCaja.setSaldo(saldoFinal);
                    bovedaCajaDAO.update(bovedaCaja);
                    commit = true;
                } else {
                    throw new RollbackFailureException(
                            "Saldo menor a cero, no se puede modificar saldo de caja");
                }
                break;
            }
        }

        if (!commit) {
            throw new RollbackFailureException(
                    "La caja no tiene la moneda indicada, no se puede realizar la transaccion");
        }
    }

    private void actualizarSaldoCaja(BigInteger idCaja, BigDecimal monto, BigInteger idMoneda)
            throws RollbackFailureException {
        Moneda monedaTransaccion = monedaDAO.find(idMoneda);
        Caja caja = cajaDAO.find(idCaja);
        if (caja == null)
            throw new RollbackFailureException("caja no encontrada para realizar la actualizacion de saldos");
        Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
        for (BovedaCaja bovedaCaja : bovedasCajas) {
            Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
            if (monedaTransaccion.equals(monedaBoveda)) {
                BigDecimal saldoActual = bovedaCaja.getSaldo();
                BigDecimal saldoFinal = saldoActual.add(monto);
                if (saldoFinal.compareTo(BigDecimal.ZERO) >= 0) {
                    bovedaCaja.setSaldo(saldoFinal);
                    bovedaCajaDAO.update(bovedaCaja);
                } else {
                    throw new RollbackFailureException(
                            "Saldo menor a cero, no se puede modificar saldo de caja");
                }
                break;
            }
        }
    }

    /* el factor indica si se va a sumar o restar al saldo de boveda */
    private void actualizarSaldoBoveda(BigInteger idBoveda,
            Set<TransaccionBovedaCajaDetalle> transaccionDetalle, int factor) throws RollbackFailureException {
        Boveda boveda = bovedaDAO.find(idBoveda);
        if (boveda == null)
            throw new RollbackFailureException("Boveda no encotrada");
        HistorialBoveda historialBoveda = getHistorialActivoBoveda(boveda.getIdBoveda());
        if (historialBoveda == null)
            throw new RollbackFailureException("Historial de Boveda no encontrada");

        BigDecimal saldoActual = BigDecimal.ZERO;
        BigDecimal montoTransaccion = BigDecimal.ZERO;

        Set<DetalleHistorialBoveda> detalleHistorialBoveda = historialBoveda.getDetalleHistorialBovedas();
        for (DetalleHistorialBoveda det : detalleHistorialBoveda) {
            BigInteger cantidad = det.getCantidad();
            BigDecimal valor = det.getMonedaDenominacion().getValor();
            saldoActual = saldoActual.add(valor.multiply(new BigDecimal(cantidad)));
        }

        for (TransaccionBovedaCajaDetalle det : transaccionDetalle) {
            BigInteger cantidad = det.getCantidad();
            BigDecimal valor = det.getMonedaDenominacion().getValor();
            montoTransaccion = montoTransaccion.add(valor.multiply(new BigDecimal(cantidad)));
        }

        // restando los valores
        for (DetalleHistorialBoveda detBoveda : detalleHistorialBoveda) {
            MonedaDenominacion monedaBoveda = detBoveda.getMonedaDenominacion();
            for (TransaccionBovedaCajaDetalle detTrans : transaccionDetalle) {
                MonedaDenominacion monedaTrans = detTrans.getMonedaDenominacion();
                if (monedaBoveda.equals(monedaTrans)) {
                    // restar los valores de cantidad
                    BigInteger cantidadActual = detBoveda.getCantidad();
                    BigInteger cantidadTrans = detTrans.getCantidad();
                    BigInteger cantidadFinal = null;
                    if (factor == 1)
                        cantidadFinal = cantidadActual.add(cantidadTrans);
                    else if (factor == -1)
                        cantidadFinal = cantidadActual.subtract(cantidadTrans);
                    else
                        throw new RollbackFailureException("Factor no Valido para Transaccion");

                    if (cantidadFinal.compareTo(BigInteger.ZERO) == -1)
                        throw new RollbackFailureException("Saldo Insuficiente en Boveda");
                    detBoveda.setCantidad(cantidadFinal);
                    break;
                }
            }
        }

        // verificando el que saldoActual - montoTransaccion == sumatoria final
        // de detalleHistorialBoveda
        BigDecimal saldoFinalConFactor = BigDecimal.ZERO;
        if (factor == 1)
            saldoFinalConFactor = saldoActual.add(montoTransaccion);
        else if (factor == -1)
            saldoFinalConFactor = saldoActual.subtract(montoTransaccion);
        else
            throw new RollbackFailureException("Factor no Valido para Transaccion");

        BigDecimal saldoFinalConHistorial = BigDecimal.ZERO;
        for (DetalleHistorialBoveda det : detalleHistorialBoveda) {
            BigInteger cantidad = det.getCantidad();
            BigDecimal valor = det.getMonedaDenominacion().getValor();
            saldoFinalConHistorial = saldoFinalConHistorial.add(valor.multiply(new BigDecimal(cantidad)));
        }

        if (saldoFinalConFactor.compareTo(saldoFinalConHistorial) != 0)
            throw new RollbackFailureException(
                    "No se pudo realizar la transaccion, el detalle de transaccion enviado no coincide con el historial de boveda, verifique que ninguna MONEDA DENOMINACION este inactiva");

        for (DetalleHistorialBoveda det : detalleHistorialBoveda) {
            detalleHistorialBovedaDAO.update(det);
        }
    }

    private HistorialBoveda getHistorialActivoBoveda(BigInteger idBoveda) {
        Boveda boveda = bovedaDAO.find(idBoveda);
        HistorialBoveda bovedaHistorial = null;
        QueryParameter queryParameter = QueryParameter.with("idboveda", boveda.getIdBoveda());
        List<HistorialBoveda> list = historialBovedaDAO.findByNamedQuery(
                HistorialBoveda.findByHistorialActivo, queryParameter.parameters());
        for (HistorialBoveda c : list) {
            bovedaHistorial = c;
            break;
        }
        return bovedaHistorial;
    }

    @AllowedTo(Permission.CERRADO)
    @Override
    public BigInteger abrirCaja() throws RollbackFailureException {
        Caja caja = getCaja();
        Trabajador trabajador = getTrabajador();
        if (trabajador == null)
            throw new RollbackFailureException("No se encontró un trabajador para la caja");

        Set<BovedaCaja> bovedaCajas = caja.getBovedaCajas();
        for (BovedaCaja bovedaCaja : bovedaCajas) {
            Boveda boveda = bovedaCaja.getBoveda();
            if (!boveda.getAbierto())
                throw new RollbackFailureException("Debe de abrir las bovedas asociadas a la caja("
                        + boveda.getDenominacion() + ")");
        }

        try {
            HistorialCaja historialCajaOld = this.getHistorialActivo();

            // abriendo caja
            caja.setAbierto(true);
            caja.setEstadoMovimiento(true);
            Set<ConstraintViolation<Caja>> violationsCaja = validator.validate(caja);
            if (!violationsCaja.isEmpty()) {
                throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsCaja));
            } else {
                cajaDAO.update(caja);
            }

            if (historialCajaOld != null) {
                historialCajaOld.setEstado(false);
                Set<ConstraintViolation<HistorialCaja>> violationsHistorialOld = validator
                        .validate(historialCajaOld);
                if (!violationsHistorialOld.isEmpty()) {
                    throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(
                            violationsHistorialOld));
                } else {
                    historialCajaDAO.update(historialCajaOld);
                }
            }

            Calendar calendar = Calendar.getInstance();
            HistorialCaja historialCajaNew = new HistorialCaja();
            historialCajaNew.setCaja(caja);
            historialCajaNew.setFechaApertura(calendar.getTime());
            historialCajaNew.setHoraApertura(calendar.getTime());
            historialCajaNew.setEstado(true);
            historialCajaNew.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno() + " "
                    + trabajador.getPersonaNatural().getApellidoMaterno() + ", "
                    + trabajador.getPersonaNatural().getNombres());
            Set<ConstraintViolation<HistorialCaja>> violationsHistorialNew = validator
                    .validate(historialCajaNew);
            if (!violationsHistorialNew.isEmpty()) {
                throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(
                        violationsHistorialNew));
            } else {
                historialCajaDAO.create(historialCajaNew);
            }

            if (historialCajaOld != null) {
                Set<DetalleHistorialCaja> detalleHistorialCajas = historialCajaOld.getDetalleHistorialCajas();
                for (DetalleHistorialCaja detalleHistorialCaja : detalleHistorialCajas) {
                    this.em.getEm().detach(detalleHistorialCaja);
                    detalleHistorialCaja.setIdDetalleHistorialCaja(null);
                    detalleHistorialCaja.setHistorialCaja(historialCajaNew);

                    Set<ConstraintViolation<DetalleHistorialCaja>> violationsHistorialDetalle = validator
                            .validate(detalleHistorialCaja);
                    if (!violationsHistorialDetalle.isEmpty()) {
                        throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(
                                violationsHistorialDetalle));
                    } else {
                        detalleHistorialCajaDAO.create(detalleHistorialCaja);
                    }
                }
            } else {
                for (BovedaCaja bovedaCaja : bovedaCajas) {
                    Moneda moneda = bovedaCaja.getBoveda().getMoneda();
                    List<MonedaDenominacion> denominaciones = monedaServiceNT.getDenominaciones(moneda
                            .getIdMoneda());
                    for (MonedaDenominacion monedaDenominacion : denominaciones) {
                        DetalleHistorialCaja detalleHistorialCaja = new DetalleHistorialCaja();
                        detalleHistorialCaja.setCantidad(BigInteger.ZERO);
                        detalleHistorialCaja.setHistorialCaja(historialCajaNew);
                        detalleHistorialCaja.setMonedaDenominacion(monedaDenominacion);

                        Set<ConstraintViolation<DetalleHistorialCaja>> violationsHistorialDetalle = validator
                                .validate(detalleHistorialCaja);
                        if (!violationsHistorialDetalle.isEmpty()) {
                            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(
                                    violationsHistorialDetalle));
                        } else {
                            detalleHistorialCajaDAO.create(detalleHistorialCaja);
                        }
                    }
                }
            }

            return historialCajaNew.getIdHistorialCaja();
        } catch (ConstraintViolationException e) {
            LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
            throw new EJBException(e);
        }
    }

    @AllowedTo(Permission.ABIERTO)
    @Override
    public BigInteger cerrarCaja(Set<GenericMonedaDetalle> detalleCaja) throws RollbackFailureException {
        Caja caja = getCaja();
        Set<BovedaCaja> bovedas = caja.getBovedaCajas();
        for (BovedaCaja bovedaCaja : bovedas) {
            Moneda moneda = bovedaCaja.getBoveda().getMoneda();
            for (GenericMonedaDetalle detalle : detalleCaja) {
                if (moneda.equals(detalle.getMoneda())) {
                    if (bovedaCaja.getSaldo().compareTo(detalle.getTotal()) != 0) {
                        throw new RollbackFailureException(
                                "El detalle enviado y el saldo en boveda no coinciden");
                    }
                    break;
                }
            }
        }
        try {
            Trabajador trabajador = this.getTrabajador();

            Calendar calendar = Calendar.getInstance();
            HistorialCaja historialCaja = this.getHistorialActivo();
            historialCaja.setEstado(true);
            historialCaja.setFechaCierre(calendar.getTime());
            historialCaja.setHoraCierre(calendar.getTime());
            historialCaja.setTrabajador(trabajador.getPersonaNatural().getApellidoPaterno() + " "
                    + trabajador.getPersonaNatural().getApellidoMaterno() + ","
                    + trabajador.getPersonaNatural().getNombres());

            Set<ConstraintViolation<HistorialCaja>> violationsHistorial = validator.validate(historialCaja);
            if (!violationsHistorial.isEmpty()) {
                throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(
                        violationsHistorial));
            } else {
                historialCajaDAO.update(historialCaja);
            }

            // cerrando caja
            caja.setAbierto(false);
            caja.setEstadoMovimiento(false);
            Set<ConstraintViolation<Caja>> violationsCaja = validator.validate(caja);
            if (!violationsHistorial.isEmpty()) {
                throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violationsCaja));
            } else {
                cajaDAO.update(caja);
            }

            // modificando el detalleCaja
            if (bovedas.size() == detalleCaja.size()) {
                for (BovedaCaja bovedaCaja : bovedas) {
                    Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
                    for (GenericMonedaDetalle detalle : detalleCaja) {
                        Moneda monedaUsuario = detalle.getMoneda();
                        if (monedaBoveda.equals(monedaUsuario)) {
                            Set<DetalleHistorialCaja> detHistCaja = historialCaja.getDetalleHistorialCajas();
                            Set<GenericDetalle> genDet = detalle.getDetalle();
                            for (DetalleHistorialCaja dhc : detHistCaja) {
                                MonedaDenominacion monedaDenom = dhc.getMonedaDenominacion();
                                BigDecimal valorMonedaDenom = monedaDenom.getValor();
                                for (GenericDetalle genericDetalle : genDet) {
                                    if (genericDetalle.getValor().compareTo(valorMonedaDenom) == 0
                                            && monedaDenom.getMoneda().equals(monedaUsuario)) {
                                        dhc.setCantidad(genericDetalle.getCantidad());

                                        Set<ConstraintViolation<DetalleHistorialCaja>> violationsDetalle = validator
                                                .validate(dhc);
                                        if (!violationsHistorial.isEmpty()) {
                                            throw new ConstraintViolationException(
                                                    new HashSet<ConstraintViolation<?>>(violationsDetalle));
                                        } else {
                                            detalleHistorialCajaDAO.update(dhc);
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            } else {
                throw new RollbackFailureException(
                        "El numero de bovedas enviadas no coincide con el numero en base de datos");
            }
            return historialCaja.getIdHistorialCaja();
        } catch (ConstraintViolationException e) {
            throw new EJBException(e);
        }
    }

    @Override
    public Map<Boveda, BigDecimal> getDiferenciaSaldoCaja(Set<GenericMonedaDetalle> detalle) {
        Map<Boveda, BigDecimal> result = new HashMap<Boveda, BigDecimal>();
        Caja caja = getCaja();
        Set<BovedaCaja> bovedas = caja.getBovedaCajas();
        for (BovedaCaja bovedaCaja : bovedas) {
            Moneda moneda = bovedaCaja.getBoveda().getMoneda();
            for (GenericMonedaDetalle det : detalle) {
                if (moneda.equals(det.getMoneda())) {
                    if (bovedaCaja.getSaldo().compareTo(det.getTotal()) != 0) {
                        Boveda boveda = bovedaCaja.getBoveda();
                        Hibernate.initialize(boveda);
                        BigDecimal diferencia = bovedaCaja.getSaldo().subtract(det.getTotal());
                        result.put(boveda, diferencia);
                    }
                    break;
                }
            }
        }
        return result;
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearAporte(BigInteger idSocio, BigDecimal monto, int mes, int anio, String referencia)
            throws RollbackFailureException {
        Socio socio = socioDAO.find(idSocio);
        if (socio == null)
            throw new RollbackFailureException("Socio no encontrado");
        CuentaAporte cuentaAporte = socio.getCuentaAporte();
        if (cuentaAporte == null)
            throw new RollbackFailureException("Socio no tiene cuenta de aportes");

        if (monto.compareTo(BigDecimal.ZERO) != 1) {
            throw new RollbackFailureException("Monto invalido para transaccion");
        }

        switch (cuentaAporte.getEstadoCuenta()) {
        case CONGELADO:
            throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");
        case INACTIVO:
            throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
        default:
            break;
        }

        // obteniendo datos de caja en session
        HistorialCaja historialCaja = this.getHistorialActivo();
        Trabajador trabajador = this.getTrabajador();
        PersonaNatural natural = trabajador.getPersonaNatural();

        // obteniendo saldo disponible de cuenta
        BigDecimal saldoDisponible = cuentaAporte.getSaldo().add(monto);
        cuentaAporte.setSaldo(saldoDisponible);
        cuentaAporteDAO.update(cuentaAporte);

        Calendar calendar = Calendar.getInstance();

        TransaccionCuentaAporte transaccionCuentaAporte = new TransaccionCuentaAporte();
        transaccionCuentaAporte.setAnioAfecta(anio);
        transaccionCuentaAporte.setMesAfecta(mes);
        transaccionCuentaAporte.setCuentaAporte(cuentaAporte);
        transaccionCuentaAporte.setEstado(true);
        transaccionCuentaAporte.setFecha(calendar.getTime());
        transaccionCuentaAporte.setHistorialCaja(historialCaja);
        transaccionCuentaAporte.setHora(calendar.getTime());
        transaccionCuentaAporte.setMonto(monto);
        transaccionCuentaAporte.setNumeroOperacion(this.getNumeroOperacion());
        transaccionCuentaAporte.setReferencia(referencia);
        transaccionCuentaAporte.setObservacion("Doc:" + natural.getTipoDocumento().getAbreviatura() + "/"
                + natural.getNumeroDocumento() + "Trabajador:" + natural.getApellidoPaterno() + " "
                + natural.getApellidoMaterno() + "," + natural.getNombres());
        transaccionCuentaAporte.setSaldoDisponible(saldoDisponible);
        transaccionCuentaAporte.setTipoTransaccion(Tipotransaccionbancaria.DEPOSITO);

        transaccionCuentaAporteDAO.create(transaccionCuentaAporte);
        // actualizar saldo caja
        this.actualizarSaldoCaja(monto, cuentaAporte.getMoneda().getIdMoneda());
        return transaccionCuentaAporte.getIdTransaccionCuentaAporte();
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger retiroCuentaAporte(BigInteger idSocio) throws RollbackFailureException {
        Socio socio = socioDAO.find(idSocio);
        if (socio == null)
            throw new RollbackFailureException("Socio no encontrado");
        CuentaAporte cuentaAporte = socio.getCuentaAporte();
        if (cuentaAporte == null)
            throw new RollbackFailureException("Socio no tiene cuenta de aportes");

        switch (cuentaAporte.getEstadoCuenta()) {
        case CONGELADO:
            throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");
        case INACTIVO:
            throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
        default:
            break;
        }

        // obteniendo datos de caja en session
        HistorialCaja historialCaja = this.getHistorialActivo();
        Trabajador trabajador = this.getTrabajador();
        PersonaNatural natural = trabajador.getPersonaNatural();

        // obteniendo saldo disponible de cuenta
        BigDecimal montoTransaccion = cuentaAporte.getSaldo().negate();
        BigDecimal saldoDisponible = cuentaAporte.getSaldo().add(montoTransaccion);
        cuentaAporte.setSaldo(saldoDisponible);
        cuentaAporteDAO.update(cuentaAporte);

        Calendar calendar = Calendar.getInstance();

        TransaccionCuentaAporte transaccionCuentaAporte = new TransaccionCuentaAporte();
        transaccionCuentaAporte.setCuentaAporte(cuentaAporte);
        transaccionCuentaAporte.setEstado(true);
        transaccionCuentaAporte.setFecha(calendar.getTime());
        transaccionCuentaAporte.setHistorialCaja(historialCaja);
        transaccionCuentaAporte.setHora(calendar.getTime());
        transaccionCuentaAporte.setMonto(montoTransaccion);
        transaccionCuentaAporte.setNumeroOperacion(this.getNumeroOperacion());
        transaccionCuentaAporte.setReferencia("Cancelacion de cuenta");
        transaccionCuentaAporte.setObservacion("Doc:" + natural.getTipoDocumento().getAbreviatura() + "/"
                + natural.getNumeroDocumento() + "Trabajador:" + natural.getApellidoPaterno() + " "
                + natural.getApellidoMaterno() + "," + natural.getNombres());
        transaccionCuentaAporte.setSaldoDisponible(saldoDisponible);
        transaccionCuentaAporte.setTipoTransaccion(Tipotransaccionbancaria.RETIRO);

        transaccionCuentaAporteDAO.create(transaccionCuentaAporte);

        // actualizar saldo caja
        this.actualizarSaldoCaja(montoTransaccion, cuentaAporte.getMoneda().getIdMoneda());

        return transaccionCuentaAporte.getIdTransaccionCuentaAporte();
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransaccionBancaria(String numeroCuenta, BigDecimal monto, String referencia,
            BigDecimal interes) throws RollbackFailureException {
        CuentaBancaria cuentaBancaria = null;
        try {
            QueryParameter queryParameter = QueryParameter.with("numerocuenta", numeroCuenta);
            List<CuentaBancaria> list = cuentaBancariaDAO.findByNamedQuery(CuentaBancaria.findByNumeroCuenta,
                    queryParameter.parameters());
            if (list.size() == 1)
                cuentaBancaria = list.get(0);
            else
                throw new IllegalResultException("Existen mas de una cuenta con el numero de cuenta indicado");
        } catch (IllegalResultException e) {
            LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
            throw new EJBAccessException("Error de inconsistencia de datos");
        }

        switch (cuentaBancaria.getEstado()) {
        case ACTIVO:
            if (cuentaBancaria.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO)) {
                if (monto.compareTo(BigDecimal.ZERO) >= 0) {
                    cuentaBancaria.setEstado(EstadoCuentaBancaria.CONGELADO);
                }
            }
            break;
        case CONGELADO:
            throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");
        case INACTIVO:
            throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
        default:
            break;
        }

        // obteniendo datos de caja en session
        HistorialCaja historialCaja = this.getHistorialActivo();
        Trabajador trabajador = this.getTrabajador();
        PersonaNatural natural = trabajador.getPersonaNatural();

        // Creando transaccion
        Calendar calendar = Calendar.getInstance();

        TransaccionBancaria transaccionBancaria = new TransaccionBancaria();
        transaccionBancaria.setCuentaBancaria(cuentaBancaria);
        transaccionBancaria.setEstado(true);
        transaccionBancaria.setFecha(calendar.getTime());
        transaccionBancaria.setHora(calendar.getTime());
        transaccionBancaria.setHistorialCaja(historialCaja);
        transaccionBancaria.setMonto(monto);
        transaccionBancaria.setNumeroOperacion(this.getNumeroOperacion());
        transaccionBancaria.setObservacion("Doc:" + natural.getTipoDocumento().getAbreviatura() + "/"
                + natural.getNumeroDocumento() + "Trabajador:" + natural.getApellidoPaterno() + " "
                + natural.getApellidoMaterno() + "," + natural.getNombres());
        transaccionBancaria.setReferencia(referencia);
        transaccionBancaria.setSaldoDisponible(cuentaBancaria.getSaldo().add(monto));
        transaccionBancaria
                .setTipoTransaccion(monto.compareTo(BigDecimal.ZERO) >= 0 ? Tipotransaccionbancaria.DEPOSITO
                        : Tipotransaccionbancaria.RETIRO);
        transaccionBancaria.setMoneda(cuentaBancaria.getMoneda());
        transaccionBancariaDAO.create(transaccionBancaria);

        // actualizar saldo caja
        this.actualizarSaldoCaja(monto, cuentaBancaria.getMoneda().getIdMoneda());

        // Verificar sobreGiro
        if (monto.compareTo(BigDecimal.ZERO) >= 0) {
            // deposito
            BigDecimal saldoActual = cuentaBancaria.getSaldo();
            if (saldoActual.compareTo(BigDecimal.ZERO) == -1) {
                // tiene sobreGiros sin pagar
                QueryParameter queryParameter = QueryParameter.with("idCuentaBancaria",
                        cuentaBancaria.getIdCuentaBancaria()).and("estado", EstadoSobreGiroBancario.ACTIVO);
                List<SobreGiroBancario> list = sobreGiroBancarioDAO.findByNamedQuery(
                        SobreGiroBancario.findByIdCuentaBancariaAndEstado, queryParameter.parameters());

                BigDecimal montoBandera = monto.abs();
                for (SobreGiroBancario sobreGiroBancario : list) {
                    if (montoBandera.compareTo(BigDecimal.ZERO) == 1) {
                        BigDecimal montoTotalSobreGiro = sobreGiroBancario.getMonto().abs();
                        BigDecimal montoTotalSobreGiroPagado = BigDecimal.ZERO;
                        Set<HistorialPagoSobreGiroBancario> historialPagos = sobreGiroBancario
                                .getHistorialPagos();
                        for (HistorialPagoSobreGiroBancario historialPagoSobreGiroBancario : historialPagos) {
                            BigDecimal montoTrans = historialPagoSobreGiroBancario.getMonto().abs();
                            montoTotalSobreGiroPagado = montoTotalSobreGiroPagado.add(montoTrans);
                        }
                        BigDecimal montoSobreGiroQueFaltaPagar = montoTotalSobreGiro
                                .subtract(montoTotalSobreGiroPagado);

                        // Crear historial de pago
                        HistorialPagoSobreGiroBancario nuevoPago = new HistorialPagoSobreGiroBancario();
                        nuevoPago.setFecha(Calendar.getInstance().getTime());
                        nuevoPago.setSobreGiroBancario(sobreGiroBancario);
                        nuevoPago.setIdTransaccionBancaria(transaccionBancaria.getIdTransaccionBancaria());
                        if (montoBandera.subtract(montoSobreGiroQueFaltaPagar).compareTo(BigDecimal.ZERO) >= 0) {
                            // paga giro y lo inactiva
                            nuevoPago.setMonto(montoSobreGiroQueFaltaPagar);
                            historialPagoSobreGiroBancarioDAO.create(nuevoPago);

                            sobreGiroBancario.setEstado(EstadoSobreGiroBancario.PAGADO);
                            sobreGiroBancarioDAO.update(sobreGiroBancario);
                        } else {
                            // paga giro pero sigue activo
                            nuevoPago.setMonto(montoBandera.abs());
                            historialPagoSobreGiroBancarioDAO.create(nuevoPago);
                        }
                        montoBandera = montoBandera.subtract(montoSobreGiroQueFaltaPagar.abs());
                    } else {
                        break;
                    }
                }
            }
        } else {
            // retiro
            BigDecimal saldoDisponible = cuentaBancaria.getSaldo().add(monto);
            if (saldoDisponible.compareTo(BigDecimal.ZERO) == -1) {
                // se debe de hacer un sobregiro
                SobreGiroBancario sobreGiroBancario = new SobreGiroBancario();
                sobreGiroBancario.setCuentaBancaria(cuentaBancaria);
                sobreGiroBancario.setEstado(EstadoSobreGiroBancario.ACTIVO);
                sobreGiroBancario.setFechaCreacion(Calendar.getInstance().getTime());
                sobreGiroBancario.setInteres(interes);
                sobreGiroBancario.setMonto(saldoDisponible.abs());
                sobreGiroBancario.setIdTransaccionBancaria(transaccionBancaria.getIdTransaccionBancaria());
                sobreGiroBancarioDAO.create(sobreGiroBancario);
            }
        }
        // actualizar saldo cuenta bancaria
        BigDecimal saldoDisponible = cuentaBancaria.getSaldo().add(monto);
        cuentaBancaria.setSaldo(saldoDisponible);
        cuentaBancariaDAO.update(cuentaBancaria);

        return transaccionBancaria.getIdTransaccionBancaria();
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransaccionCompraVenta(Tipotransaccioncompraventa tipoTransaccion,
            BigInteger idMonedaRecibido, BigInteger idMonedaEntregado, BigDecimal montoRecibido,
            BigDecimal montoEntregado, BigDecimal tasaCambio, String referencia)
            throws RollbackFailureException {
        Moneda monedaRecibida = monedaDAO.find(idMonedaRecibido);
        Moneda monedaEntregada = monedaDAO.find(idMonedaEntregado);
        if (monedaRecibida == null || monedaEntregada == null)
            throw new RollbackFailureException("Monedas no encontradas");

        Calendar calendar = Calendar.getInstance();

        HistorialCaja historialCaja = this.getHistorialActivo();
        Trabajador trabajador = this.getTrabajador();
        PersonaNatural natural = trabajador.getPersonaNatural();

        TransaccionCompraVenta transaccionCompraVenta = new TransaccionCompraVenta();
        transaccionCompraVenta.setIdTransaccionCompraVenta(null);
        transaccionCompraVenta.setEstado(true);
        transaccionCompraVenta.setFecha(calendar.getTime());
        transaccionCompraVenta.setHora(calendar.getTime());
        transaccionCompraVenta.setHistorialCaja(historialCaja);
        transaccionCompraVenta.setMonedaEntregada(monedaEntregada);
        transaccionCompraVenta.setMonedaRecibida(monedaRecibida);
        transaccionCompraVenta.setMontoEntregado(montoEntregado);
        transaccionCompraVenta.setMontoRecibido(montoRecibido);
        transaccionCompraVenta.setNumeroOperacion(this.getNumeroOperacion());
        transaccionCompraVenta.setObservacion("Doc:" + natural.getTipoDocumento().getAbreviatura() + "/"
                + natural.getNumeroDocumento() + "Trabajador:" + natural.getApellidoPaterno() + " "
                + natural.getApellidoMaterno() + "," + natural.getNombres());
        transaccionCompraVenta.setCliente(referencia);
        transaccionCompraVenta.setTipoCambio(tasaCambio);
        transaccionCompraVenta.setTipoTransaccion(tipoTransaccion);

        actualizarSaldoCaja(montoEntregado.abs().negate(), monedaEntregada.getIdMoneda());
        actualizarSaldoCaja(montoRecibido.abs(), monedaRecibida.getIdMoneda());

        transaccionCompraVentaDAO.create(transaccionCompraVenta);

        return transaccionCompraVenta.getIdTransaccionCompraVenta();
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransferenciaBancaria(String numeroCuentaOrigen, String numeroCuentaDestino,
            BigDecimal monto, String referencia) throws RollbackFailureException {
        CuentaBancaria cuentaBancariaOrigen = null;
        CuentaBancaria cuentaBancariaDestino = null;

        if (monto.compareTo(BigDecimal.ZERO) != 1) {
            throw new RollbackFailureException("Monto invalido para transaccion");
        }
        try {
            QueryParameter queryParameter1 = QueryParameter.with("numerocuenta", numeroCuentaOrigen);
            List<CuentaBancaria> list1 = cuentaBancariaDAO.findByNamedQuery(
                    CuentaBancaria.findByNumeroCuenta, queryParameter1.parameters());
            QueryParameter queryParameter2 = QueryParameter.with("numerocuenta", numeroCuentaDestino);
            List<CuentaBancaria> list2 = cuentaBancariaDAO.findByNamedQuery(
                    CuentaBancaria.findByNumeroCuenta, queryParameter2.parameters());

            if (list1.size() == 1)
                cuentaBancariaOrigen = list1.get(0);
            else
                throw new IllegalResultException("Existen mas de una cuenta con el numero de cuenta indicado");
            if (list2.size() == 1)
                cuentaBancariaDestino = list2.get(0);
            else
                throw new IllegalResultException("Existen mas de una cuenta con el numero de cuenta indicado");
        } catch (IllegalResultException e) {
            LOGGER.error(e.getMessage(), e.getCause(), e.getLocalizedMessage());
            throw new EJBAccessException("Error de inconsistencia de datos");
        }

        switch (cuentaBancariaOrigen.getEstado()) {
        case CONGELADO:
            throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");
        case INACTIVO:
            throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
        default:
            break;
        }
        switch (cuentaBancariaDestino.getEstado()) {
        case CONGELADO:
            throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");
        case INACTIVO:
            throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
        default:
            break;
        }

        // obteniendo datos de caja en session
        HistorialCaja historialCaja = this.getHistorialActivo();
        Trabajador trabajador = this.getTrabajador();
        PersonaNatural natural = trabajador.getPersonaNatural();

        // obteniendo saldo disponible de cuenta
        BigDecimal saldoDisponibleOrigen = cuentaBancariaOrigen.getSaldo().subtract(monto);
        BigDecimal saldoDisponibleDestino = cuentaBancariaDestino.getSaldo().add(monto);
        if (saldoDisponibleOrigen.compareTo(BigDecimal.ZERO) == -1)
            throw new RollbackFailureException("Saldo insuficiente para transferencia");

        cuentaBancariaOrigen.setSaldo(saldoDisponibleOrigen);
        cuentaBancariaDestino.setSaldo(saldoDisponibleDestino);
        if (cuentaBancariaDestino.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO)) {
            cuentaBancariaDestino.setEstado(EstadoCuentaBancaria.CONGELADO);
        }
        cuentaBancariaDAO.update(cuentaBancariaOrigen);
        cuentaBancariaDAO.update(cuentaBancariaDestino);

        Calendar calendar = Calendar.getInstance();

        TransferenciaBancaria transferenciaBancaria = new TransferenciaBancaria();
        transferenciaBancaria.setIdTransferenciaBancaria(null);
        transferenciaBancaria.setCuentaBancariaOrigen(cuentaBancariaOrigen);
        transferenciaBancaria.setCuentaBancariaDestino(cuentaBancariaDestino);
        transferenciaBancaria.setEstado(true);
        transferenciaBancaria.setFecha(calendar.getTime());
        transferenciaBancaria.setHora(calendar.getTime());
        transferenciaBancaria.setHistorialCaja(historialCaja);
        transferenciaBancaria.setMonto(monto);
        transferenciaBancaria.setNumeroOperacion(this.getNumeroOperacion());
        transferenciaBancaria.setObservacion("Doc:" + natural.getTipoDocumento().getAbreviatura() + "/"
                + natural.getNumeroDocumento() + "Trabajador:" + natural.getApellidoPaterno() + " "
                + natural.getApellidoMaterno() + "," + natural.getNombres());
        transferenciaBancaria.setReferencia(referencia);
        transferenciaBancaria.setSaldoDisponibleOrigen(saldoDisponibleOrigen);
        transferenciaBancaria.setSaldoDisponibleDestino(saldoDisponibleDestino);

        transferenciaBancariaDAO.create(transferenciaBancaria);
        return transferenciaBancaria.getIdTransferenciaBancaria();
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransaccionCheque(String numeroChequeUnico, BigDecimal monto,
            String tipoDocumento, String numeroDocumento, String persona, String observacion)
            throws RollbackFailureException {
        QueryParameter queryParameter = QueryParameter.with("numeroChequeUnico", new BigInteger(
                numeroChequeUnico.toString()));
        List<Cheque> list = chequeDAO.findByNamedQuery(Cheque.findChequeByNumeroChequeUnico,
                queryParameter.parameters());
        Cheque cheque = null;
        for (Cheque item : list) {
            cheque = item;
            break;
        }

        if (cheque == null)
            throw new RollbackFailureException("Cheque no encontrado");
        if (!cheque.getEstado().equals(EstadoCheque.POR_COBRAR)) {
            throw new RollbackFailureException("El Cheque se encuentra en estado " + cheque.getEstado().toString()
                    + " no es posible realizar la operacion");
        }

        Chequera chequera = cheque.getChequera();
        CuentaBancaria cuentaBancaria = chequera.getCuentaBancaria();

        if (!chequera.getEstado()) {
            throw new RollbackFailureException("Chequera inactiva, no se puede realizar la transaccion");
        }

        switch (cuentaBancaria.getEstado()) {
        case ACTIVO:
            if (!cuentaBancaria.getTipoCuentaBancaria().equals(TipoCuentaBancaria.RECAUDADORA)) {
                throw new RollbackFailureException("Solo las cuentas recaudadoras pueden girar cheques");
            }
            break;
        case CONGELADO:
            throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");
        case INACTIVO:
            throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
        default:
            break;
        }

        // obteniendo datos de caja en session
        HistorialCaja historialCaja = this.getHistorialActivo();
        Trabajador trabajador = this.getTrabajador();
        PersonaNatural natural = trabajador.getPersonaNatural();

        // obteniendo saldo disponible de cuenta
        BigDecimal saldoDisponible = cuentaBancaria.getSaldo().subtract(monto.abs());
        if (saldoDisponible.compareTo(BigDecimal.ZERO) == -1) {
            throw new RollbackFailureException("Saldo insuficiente para transaccion");
        } else {
            cuentaBancaria.setSaldo(saldoDisponible);
            cuentaBancariaDAO.update(cuentaBancaria);
        }

        Calendar calendar = Calendar.getInstance();

        TransaccionCheque transaccionCheque = new TransaccionCheque();

        transaccionCheque.setCheque(cheque);
        transaccionCheque.setEstado(true);
        transaccionCheque.setFecha(calendar.getTime());
        transaccionCheque.setHora(calendar.getTime());
        transaccionCheque.setHistorialCaja(historialCaja);
        transaccionCheque.setMonto(monto.abs().negate());
        transaccionCheque.setNumeroDocumento(numeroDocumento);
        transaccionCheque.setTipoDocumento(tipoDocumento);
        transaccionCheque.setPersona(persona);
        transaccionCheque.setObservacion(numeroDocumento +"/"+ persona);
        transaccionCheque.setSaldoDisponible(saldoDisponible);
        transaccionCheque.setNumeroOperacion(this.getNumeroOperacion());
        transaccionCheque.setTrabajador("Doc:" + natural.getTipoDocumento().getAbreviatura() + "/"
                + natural.getNumeroDocumento() + "Trabajador:" + natural.getApellidoPaterno() + " "
                + natural.getApellidoMaterno() + "," + natural.getNombres());
        transaccionChequeDAO.create(transaccionCheque);

        cheque.setEstado(EstadoCheque.COBRADO);
        cheque.setFechaCambioEstado(calendar.getTime());
        cheque.setMonto(monto.abs().negate());
        cheque.setNumeroDocumento(cheque.getNumeroDocumento() == null ? numeroDocumento : cheque
                .getNumeroDocumento());
        cheque.setTipoDocumento(cheque.getTipoDocumento() == null ? tipoDocumento : cheque.getTipoDocumento());
        cheque.setPersona(cheque.getPersona() == null ? persona : cheque.getPersona());
        chequeDAO.update(cheque);

        // actualizar saldo caja
        this.actualizarSaldoCaja(monto.abs().negate(), cuentaBancaria.getMoneda().getIdMoneda());

        return transaccionCheque.getIdTransaccionCheque();
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public void extornarTransaccion(BigInteger idTransaccion) throws RollbackFailureException {

    	HistorialTransaccionCaja transaccion = historialTransaccionCajaDAO.find(idTransaccion);       
        
        if (transaccion.getTipoCuenta().equalsIgnoreCase(TipoCuentaBancaria.LIBRE.toString()) || transaccion.getTipoCuenta().equalsIgnoreCase(TipoCuentaBancaria.RECAUDADORA.toString())) {
            if(transaccion.getTipoTransaccion().equalsIgnoreCase("COBRO_CHEQUE")) {
                extornarCobroCheque(idTransaccion);
            } else if( transaccion.getTipoTransaccion().equalsIgnoreCase(Tipotransaccionbancaria.DEPOSITO.toString()) || transaccion.getTipoTransaccion().equalsIgnoreCase(Tipotransaccionbancaria.RETIRO.toString()) ) {
                extornarTransaccionBancaria(transaccion.getIdTransaccion());   
            } else {
                throw new RollbackFailureException("Tipo de operacion no valida");
            }
        } 
        else if (transaccion.getTipoCuenta().equalsIgnoreCase("APORTE"))
            extornarTransaccionCuentaAporte(transaccion.getIdTransaccion());
        else if (transaccion.getTipoCuenta().equalsIgnoreCase("COMPRA_VENTA"))
            extornarTransaccionCompraVenta(transaccion.getIdTransaccion());
        else if (transaccion.getTipoCuenta().equalsIgnoreCase("TRANSFERENCIA"))
            extornarTransferenciaBancaria(transaccion.getIdTransaccion());        
        else
            throw new RollbackFailureException("Tipo de cuenta no encontrada");
    }
    
    @Override
    public void extornarGiro(BigInteger idGiro) throws RollbackFailureException {
        Giro giro = giroDAO.find(idGiro);
        
        if(giro.getEstado().equals(EstadoGiro.ENVIADO)){
            //extornar cualquier fecha
            giro.setEstado(EstadoGiro.CANCELADO);
            giroDAO.update(giro);
        } else if(giro.getEstado().equals(EstadoGiro.COBRADO)) {
            //extornar en el dia            
            DateTime startDate = new DateTime(giro.getFechaDesembolso());
            DateTime endDate = new DateTime(DateUtils.sumarRestarMinutosFecha(Calendar.getInstance().getTime(), 30));

            if(Minutes.minutesBetween(startDate, endDate).getMinutes() < 30){
                giro.setEstado(EstadoGiro.ENVIADO);
                giroDAO.update(giro);
            } else {
                throw new RollbackFailureException("Un giro cobrado solo puede ser extornado dentro de la media hora despues de retirar el dinero");
            }                       
        } else if (giro.getEstado().equals(EstadoGiro.CANCELADO)){
            //no se puede extornar
            throw new RollbackFailureException("Giro cancelado, no se puede realizar modificaciones");
        } else {
            //operacion no valida
            throw new RollbackFailureException("Estado de giro no valido");
        }        
    }

    private void extornarTransaccionBancaria(BigInteger idTransaccion) throws RollbackFailureException {
        TransaccionBancaria transaccionBancaria = transaccionBancariaDAO.find(idTransaccion);
        if (transaccionBancaria.getTipoTransaccion().equals(Tipotransaccionbancaria.DEPOSITO)) {
            extornarCuentaBancariaDeposito(transaccionBancaria);   
        } else if (transaccionBancaria.getTipoTransaccion().equals(Tipotransaccionbancaria.RETIRO)) {
            extornarCuentaBancariaRetiro(transaccionBancaria);   
        } else {
            throw new RollbackFailureException("Operacion no valida");
        }

        // despues de extornar se debe de recalcular los saldos disponibles de
        // la cuenta bancaria
        QueryParameter queryParameter = QueryParameter.with("idCuentaBancaria",
                transaccionBancaria.getCuentaBancaria().getIdCuentaBancaria()).and("fecha",
                transaccionBancaria.getHora());

        List<TransaccionBancaria> transaccionesBancarias = transaccionBancariaDAO.findByNamedQuery(
                TransaccionBancaria.findByIdCuentaAndFecha, queryParameter.parameters());
        for (TransaccionBancaria transaccionBancariaPosterior : transaccionesBancarias) {
            BigDecimal nuevoSaldoDisponible = transaccionBancariaPosterior.getSaldoDisponible().subtract(
                    transaccionBancaria.getMonto());
            transaccionBancariaPosterior.setSaldoDisponible(nuevoSaldoDisponible);
            transaccionBancariaDAO.update(transaccionBancariaPosterior);
        }

        // EXTORNAR SOBREGIROS CREADOS
        if (transaccionBancaria.getTipoTransaccion().equals(Tipotransaccionbancaria.RETIRO)) {
            // eliminar los sobregiros asociados a la transaccion
            QueryParameter queryParameter1 = QueryParameter.with("idTransaccionBancaria",
                    transaccionBancaria.getIdTransaccionBancaria());
            List<SobreGiroBancario> sobreGiros = sobreGiroBancarioDAO.findByNamedQuery(
                    SobreGiroBancario.findByIdTransaccionBancaria, queryParameter1.parameters());
            for (SobreGiroBancario sobreGiroBancario : sobreGiros) {
                Set<HistorialPagoSobreGiroBancario> historiales = sobreGiroBancario.getHistorialPagos();
                if (historiales.isEmpty()) {
                    sobreGiroBancarioDAO.delete(sobreGiroBancario);
                } else {
                    throw new EJBException(
                            "La operacion no puede ser extornada pues existen transacciones posteriores a esta.");
                }
            }
        } else {
            QueryParameter queryParameter1 = QueryParameter.with("idTransaccionBancaria",
                    transaccionBancaria.getIdTransaccionBancaria());
            List<HistorialPagoSobreGiroBancario> historialesSobreGiro = historialPagoSobreGiroBancarioDAO
                    .findByNamedQuery(HistorialPagoSobreGiroBancario.findByIdTransaccionBancaria,
                            queryParameter1.parameters());
            List<SobreGiroBancario> sobreGiros = new ArrayList<>();
            for (HistorialPagoSobreGiroBancario historialPagoSobreGiroBancario : historialesSobreGiro) {
                sobreGiros.add(historialPagoSobreGiroBancario.getSobreGiroBancario());
            }
            // eliminar los historiales de sobre giro asociados a la transaccion
            for (HistorialPagoSobreGiroBancario historialPagoSobreGiroBancario : historialesSobreGiro) {
                historialPagoSobreGiroBancarioDAO.delete(historialPagoSobreGiroBancario);
            }
            // re evaluar los sobregiros asociados para volerlos a ACTIVAR
            for (SobreGiroBancario sobreGiroBancario : sobreGiros) {
                BigDecimal montoPagado = BigDecimal.ZERO;
                Set<HistorialPagoSobreGiroBancario> historiales = sobreGiroBancario.getHistorialPagos();
                for (HistorialPagoSobreGiroBancario historialPagoSobreGiroBancario : historiales) {
                    BigDecimal montoTrans = historialPagoSobreGiroBancario.getMonto();
                    montoPagado = montoPagado.add(montoTrans);
                }
                BigDecimal saldoQueFaltaPagar = sobreGiroBancario.getMonto().subtract(montoPagado);
                if (saldoQueFaltaPagar.compareTo(BigDecimal.ZERO) == 1) {
                    sobreGiroBancario.setEstado(EstadoSobreGiroBancario.ACTIVO);
                    sobreGiroBancarioDAO.update(sobreGiroBancario);
                }
            }
        }
    }

    private void extornarCuentaBancariaDeposito(TransaccionBancaria transaccionBancaria)
            throws RollbackFailureException {
        CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(transaccionBancaria.getCuentaBancaria()
                .getIdCuentaBancaria());
        HistorialCaja historialCajaActivo = this.getHistorialActivo();

        if (transaccionBancaria.getEstado() == true
                && cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO)
                && transaccionBancaria.getHistorialCaja().getIdHistorialCaja() == historialCajaActivo
                        .getIdHistorialCaja()) {
            // if
            // (cuentaBancaria.getSaldo().compareTo(transaccionBancaria.getMonto())
            // != -1) {
            Caja caja = this.getCaja();
            BigDecimal saldoActualBovedaCaja = new BigDecimal(0.00);
            Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
            for (BovedaCaja bovedaCaja : bovedasCajas) {
                Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
                if (transaccionBancaria.getMoneda().equals(monedaBoveda)) {
                    saldoActualBovedaCaja = bovedaCaja.getSaldo();
                    break;
                }
            }

            if (saldoActualBovedaCaja.compareTo(transaccionBancaria.getMonto()) != -1) {
                cuentaBancaria.setSaldo(cuentaBancaria.getSaldo().subtract(
                        transaccionBancaria.getMonto().abs()));
                actualizarSaldoCaja(transaccionBancaria.getMonto().abs().negate(), transaccionBancaria
                        .getMoneda().getIdMoneda());
                transaccionBancaria.setEstado(false);
            } else
                throw new RollbackFailureException(
                        "Error al Extornar Transacci&oacute;n: Saldo insuficiente en caja");
            // } else
            // throw new
            // RollbackFailureException("Error al Extornar Transacci&oacute;n: La cuenta bancaria no tiene suficiente dinero");
        } else
            throw new RollbackFailureException(
                    "Error al Extornar Transacci&oacute;n: Transacci&oacute;n o cuenta bancaria no activa");
    }

    private void extornarCuentaBancariaRetiro(TransaccionBancaria transaccionBancaria)
            throws RollbackFailureException {
        CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(transaccionBancaria.getCuentaBancaria()
                .getIdCuentaBancaria());
        HistorialCaja historialCajaActivo = this.getHistorialActivo();
        if (transaccionBancaria.getEstado() == true
                && cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO)
                && transaccionBancaria.getHistorialCaja().getIdHistorialCaja() == historialCajaActivo
                        .getIdHistorialCaja()) {
            cuentaBancaria.setSaldo(cuentaBancaria.getSaldo().add(transaccionBancaria.getMonto().abs()));
            transaccionBancaria.setEstado(false);
            actualizarSaldoCaja(transaccionBancaria.getMonto().abs(), transaccionBancaria.getMoneda()
                    .getIdMoneda());
        } else
            throw new RollbackFailureException(
                    "Error al Extornar Transacci&oacute;n: Transacci&oacute;n o cuenta bancaria no activa");
    }

    private void extornarTransaccionCuentaAporte(BigInteger idTransaccion) throws RollbackFailureException {
        TransaccionCuentaAporte transaccionCuentaAporte = transaccionCuentaAporteDAO.find(idTransaccion);
        CuentaAporte cuentaAporte = cuentaAporteDAO.find(transaccionCuentaAporte.getCuentaAporte()
                .getIdCuentaaporte());
        HistorialCaja historialCajaActivo = this.getHistorialActivo();

        if (transaccionCuentaAporte.getEstado() == true
                && cuentaAporte.getEstadoCuenta().equals(EstadoCuentaAporte.ACTIVO)
                && transaccionCuentaAporte.getHistorialCaja().getIdHistorialCaja() == historialCajaActivo
                        .getIdHistorialCaja()) {
            Caja caja = this.getCaja();
            BigDecimal saldoActualBovedaCaja = new BigDecimal(0.00);
            Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
            for (BovedaCaja bovedaCaja : bovedasCajas) {
                Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
                if (cuentaAporte.getMoneda().equals(monedaBoveda)) {
                    saldoActualBovedaCaja = bovedaCaja.getSaldo();
                    break;
                }
            }

            if (saldoActualBovedaCaja.compareTo(transaccionCuentaAporte.getMonto()) != -1) {
                cuentaAporte.setSaldo(cuentaAporte.getSaldo().subtract(transaccionCuentaAporte.getMonto()));
                transaccionCuentaAporte.setEstado(false);
                actualizarSaldoCaja(transaccionCuentaAporte.getMonto().abs().negate(), cuentaAporte
                        .getMoneda().getIdMoneda());
            } else
                throw new RollbackFailureException(
                        "Error al Extornar Transacci&oacute;n: Saldo insuficiente en caja");
        } else
            throw new RollbackFailureException(
                    "Error al Extornar Transacci&oacute;n: Transacci&oacute;n o cuenta aporte no activa");
    }

    private void extornarTransaccionCompraVenta(BigInteger idTransaccion) throws RollbackFailureException {
        TransaccionCompraVenta transaccionCompraVenta = transaccionCompraVentaDAO.find(idTransaccion);
        HistorialCaja historialCajaActivo = this.getHistorialActivo();

        if (transaccionCompraVenta.getEstado() == true
                && transaccionCompraVenta.getHistorialCaja().getIdHistorialCaja() == historialCajaActivo
                        .getIdHistorialCaja()) {
            Caja caja = this.getCaja();
            BigDecimal saldoActualBovedaCaja = new BigDecimal(0.00);
            Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
            for (BovedaCaja bovedaCaja : bovedasCajas) {
                Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
                if (transaccionCompraVenta.getMonedaRecibida().equals(monedaBoveda)) {
                    saldoActualBovedaCaja = bovedaCaja.getSaldo();
                    break;
                }
            }

            if (saldoActualBovedaCaja.compareTo(transaccionCompraVenta.getMontoRecibido()) != -1) {
                actualizarSaldoCaja(transaccionCompraVenta.getMontoRecibido().abs().negate(),
                        transaccionCompraVenta.getMonedaRecibida().getIdMoneda());
                actualizarSaldoCaja(transaccionCompraVenta.getMontoEntregado().abs(), transaccionCompraVenta
                        .getMonedaEntregada().getIdMoneda());
                transaccionCompraVenta.setEstado(false);
            } else
                throw new RollbackFailureException(
                        "Error al Extornar Transacci&oacute;n: Saldo insuficiente en caja");
        } else
            throw new RollbackFailureException(
                    "Error al Extornar Transacci&oacute;n: Transacci&oacute;n no activa");
    }

    private void extornarCobroCheque(BigInteger idTransaccion) throws RollbackFailureException {
    	TransaccionCheque transaccionCheque = transaccionChequeDAO.find(idTransaccion);       
        Cheque cheque = transaccionCheque.getCheque();             
        Chequera chequera = cheque.getChequera();              
        CuentaBancaria cuentaBancaria = chequera.getCuentaBancaria();                      
       
        HistorialCaja historialCajaActivo = this.getHistorialActivo();                                    

        if (transaccionCheque.getEstado() == true && cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO) && transaccionCheque.getHistorialCaja().getIdHistorialCaja() == historialCajaActivo.getIdHistorialCaja()) {           
            Caja caja = this.getCaja();
            BigDecimal saldoActualBovedaCaja = new BigDecimal(0.00);
            Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
            for (BovedaCaja bovedaCaja : bovedasCajas) {
                Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
                if (cuentaBancaria.getMoneda().equals(monedaBoveda)) {
                    saldoActualBovedaCaja = bovedaCaja.getSaldo();
                    break;
                }
            }
            
            if (saldoActualBovedaCaja.compareTo(transaccionCheque.getMonto().abs()) != -1) {
                cuentaBancaria.setSaldo(cuentaBancaria.getSaldo().add(transaccionCheque.getMonto().abs()));
                actualizarSaldoCaja(transaccionCheque.getMonto().abs(), cuentaBancaria.getMoneda().getIdMoneda());
                transaccionCheque.setEstado(false);
            } else {
                throw new RollbackFailureException("Error al Extornar Transacci&oacute;n: Saldo insuficiente en caja");   
            }      
                       
            cheque.setEstado(EstadoCheque.POR_COBRAR);
            cheque.setFechaCambioEstado(Calendar.getInstance().getTime());
            chequeDAO.update(cheque);
        } else {
            throw new RollbackFailureException("Error al Extornar Transacci&oacute;n: Transacci&oacute;n o cuenta bancaria no activa");   
        }            
                
        // despues de extornar se debe de recalcular los saldos disponibles de
        // la cuenta bancaria
        QueryParameter queryParameter = QueryParameter.with("idCuentaBancaria", cuentaBancaria.getIdCuentaBancaria()).and("fecha",transaccionCheque.getHora());

        List<TransaccionBancaria> transaccionesBancarias = transaccionBancariaDAO.findByNamedQuery(TransaccionBancaria.findByIdCuentaAndFecha, queryParameter.parameters());
        for (TransaccionBancaria transaccionBancariaPosterior : transaccionesBancarias) {
            BigDecimal nuevoSaldoDisponible = transaccionBancariaPosterior.getSaldoDisponible().subtract(transaccionCheque.getMonto().abs());
            transaccionBancariaPosterior.setSaldoDisponible(nuevoSaldoDisponible);
            transaccionBancariaDAO.update(transaccionBancariaPosterior);

        }                

    }

    private void extornarTransferenciaBancaria(BigInteger idTransaccion) throws RollbackFailureException {
        // TransferenciaBancaria transferenciaBancaria =
        // transferenciaBancariaDAO.find(idTransaccion);
        throw new RollbackFailureException("Todavia no es posible extornar las transferencias");
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger[] crearCuentaBancariaPlazoFijoConDeposito(TipoCuentaBancaria tipoCuentaBancaria,
            String codigoAgencia, BigInteger idMoneda, BigDecimal monto, BigDecimal tasaInteres,
            TipoPersona tipoPersona, BigInteger idPersona, Integer periodo, int cantRetirantes,
            List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException {
        BigInteger idCuentaBancaria = cuentaBancariaServiceTS.create(TipoCuentaBancaria.PLAZO_FIJO,
                codigoAgencia, idMoneda, tasaInteres, tipoPersona, idPersona, new Integer(periodo),
                cantRetirantes, titulares, beneficiarios);
        CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(idCuentaBancaria);
        String numeroCuenta = cuentaBancaria.getNumeroCuenta();
        BigInteger idTransaccion = crearTransaccionBancaria(numeroCuenta, monto,
                "APERTURA CUENTA PLAZO FIJO", null);
        return new BigInteger[] { idCuentaBancaria, idTransaccion };
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger cancelarCuentaBancariaConRetiro(BigInteger id) throws RollbackFailureException {
        CuentaBancaria cuentaBancaria = cuentaBancariaDAO.find(id);
        if (cuentaBancaria == null)
            throw new RollbackFailureException("Cuenta bancaria no encontrada");
        if (!cuentaBancaria.getEstado().equals(EstadoCuentaBancaria.ACTIVO))
            throw new RollbackFailureException("La cuenta no esta activa, no se puede cancelar");
        if (cuentaBancaria.getTipoCuentaBancaria().equals(TipoCuentaBancaria.PLAZO_FIJO)) {
            if (cuentaBancaria.getFechaCierre().compareTo(Calendar.getInstance().getTime()) == 1)
                throw new RollbackFailureException(
                        "La cuenta PLAZO_FIJO tiene fecha de cierre aun no vencida");
        }

        cuentaBancariaServiceTS.capitalizarCuenta(id);

        String numeroCuenta = cuentaBancaria.getNumeroCuenta();
        BigDecimal monto = cuentaBancaria.getSaldo().negate();
        String referencia = "RETIRO POR CANCELACION DE CUENTA";

        BigInteger idTransaccion = crearTransaccionBancaria(numeroCuenta, monto, referencia, null);
        cuentaBancariaServiceTS.cancelarCuentaBancaria(id);
        return idTransaccion;
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger cancelarSocioConRetiro(BigInteger idSocio) throws RollbackFailureException {
        Socio socio = socioDAO.find(idSocio);
        if (socio == null)
            throw new RollbackFailureException("Socio no encontrado");
        if (socio.getEstado() == false)
            throw new RollbackFailureException("Socio ya esta inactivo.");
        CuentaAporte cuentaAporte = socio.getCuentaAporte();
        if (cuentaAporte == null)
            throw new RollbackFailureException("Cuenta de aporte no existente");
        if (!cuentaAporte.getEstadoCuenta().equals(EstadoCuentaAporte.ACTIVO))
            throw new RollbackFailureException(
                    "Cuenta de aportes CONGELADO, no se puede hacer el retiro de fondos");

        BigInteger idTransaccion = retiroCuentaAporte(idSocio);
        socioServiceTS.inactivarSocio(idSocio);
        return idTransaccion;
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearPendienteCaja(TipoPendienteCaja tipoPendienteCaja, BigInteger idBoveda,
            BigDecimal monto, String observacion, BigInteger idPendienteRelacionado)
            throws RollbackFailureException {

        // Buscar pendiente relacionado si es que existe
        PendienteCaja pendienteRelacionado = null;
        if (idPendienteRelacionado != null) {
            pendienteRelacionado = pendienteCajaDAO.find(idPendienteRelacionado);
            if (pendienteRelacionado == null) {
                throw new RollbackFailureException("Pendiente relacionado no fue encontrado");
            }
        }

        Caja caja = getCaja();
        Trabajador trabajador = getTrabajador();
        if (trabajador == null)
            throw new RollbackFailureException("No se encontró un trabajador para la caja");

        Boveda boveda = bovedaDAO.find(idBoveda);
        if (boveda == null)
            throw new RollbackFailureException("Boveda no encontrada");

        Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
        BovedaCaja bovedaCajaTransaccion = null;
        for (BovedaCaja bovedaCaja : bovedasCajas) {
            Boveda bov = bovedaCaja.getBoveda();
            if (bov.equals(boveda)) {
                bovedaCajaTransaccion = bovedaCaja;
                break;
            }
        }
        if (bovedaCajaTransaccion == null)
            throw new RollbackFailureException("La caja y la boveda seleccionados no estan relacionados");

        // validar
        switch (tipoPendienteCaja) {
        case FALTANTE:
            if (monto.compareTo(BigDecimal.ZERO) >= 0) {
                throw new RollbackFailureException("Pendiente FALTANTE, el monto debe ser negativo");
            }
            break;
        case SOBRANTE:
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RollbackFailureException("Pendiente SOBRANTE, el monto debe ser positivo");
            }
            break;
        case PAGO:
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RollbackFailureException("Pendiente PAGO, el monto debe ser positivo");
            }
            if (idPendienteRelacionado == null) {
                throw new RollbackFailureException("Debe indicar que pendiente desea pagar");
            }
            if (!pendienteRelacionado.getMoneda().equals(boveda.getMoneda())) {
                throw new RollbackFailureException(
                        "La moneda del pendiente no coincide con la moneda de boveda seleccionada");
            }
            // No dejar pagar mas de la deuda
            QueryParameter queryParameter = QueryParameter.with("idPendienteCaja",
                    pendienteRelacionado.getIdPendienteCaja());
            List<PendienteCajaFaltanteView> result = pendienteCajaFaltanteViewDAO.findByNamedQuery(
                    PendienteCajaFaltanteView.findByIdPendienteCaja, queryParameter.parameters());

            BigDecimal montoPorPagar = BigDecimal.ZERO;
            if (!result.isEmpty()) {
                if (result.size() != 1) {
                    throw new RollbackFailureException(
                            "Error interno, se encontraron mas de un resultado en pagos");
                } else {
                    montoPorPagar = result.get(0).getMontoPorPagar();
                }
            } else {
                montoPorPagar = pendienteRelacionado.getMonto();
            }
            if (monto.abs().compareTo(montoPorPagar.abs()) == 1) {
                throw new RollbackFailureException("El monto:" + monto
                        + " supera el monto de la deuda restante:" + montoPorPagar);
            }

            break;
        case RETIRO:
            if (monto.compareTo(BigDecimal.ZERO) >= 0) {
                throw new RollbackFailureException("Pendiente RETIRO, el monto debe ser negativo");
            }
            break;
        default:
            break;
        }

        // obteniendo el historial de la caja
        HistorialCaja historialCaja = this.getHistorialActivo();

        // verificando el saldo negativo
        BigDecimal saldoActual = bovedaCajaTransaccion.getSaldo();
        BigDecimal saldoFinal = saldoActual.add(monto);
        if (saldoFinal.compareTo(BigDecimal.ZERO) == -1)
            throw new RollbackFailureException("Saldo actual de caja:" + saldoActual.toString()
                    + "; transaccion:" + monto.toString() + ". No se puede realizar transaccion");

        Calendar calendar = Calendar.getInstance();
        PendienteCaja pendienteCaja = new PendienteCaja();
        pendienteCaja.setHistorialCajaCreacion(historialCaja);
        pendienteCaja.setMoneda(boveda.getMoneda());
        pendienteCaja.setMonto(monto);
        pendienteCaja.setFecha(calendar.getTime());
        pendienteCaja.setHora(calendar.getTime());
        pendienteCaja.setTipoPendiente(tipoPendienteCaja);
        pendienteCaja.setPendienteRelacionado(pendienteRelacionado);
        pendienteCaja.setTrabajadorCrea(trabajador.getPersonaNatural().getApellidoPaterno() + " "
                + trabajador.getPersonaNatural().getApellidoMaterno() + ", "
                + trabajador.getPersonaNatural().getNombres());
        pendienteCaja.setObservacion(observacion);
        pendienteCajaDAO.create(pendienteCaja);

        // modificando el saldo de boveda
        bovedaCajaTransaccion.setSaldo(saldoFinal);
        bovedaCajaDAO.update(bovedaCajaTransaccion);

        return pendienteCaja.getIdPendienteCaja();
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransaccionBovedaCaja(BigInteger idBoveda, Set<GenericDetalle> detalleTransaccion,
            TransaccionBovedaCajaOrigen origen) throws RollbackFailureException {
        Boveda boveda = bovedaDAO.find(idBoveda);
        Caja caja = getCaja();
        if (boveda == null || caja == null)
            throw new RollbackFailureException("Caja o Boveda no encontrada");

        if (!boveda.getEstado())
            throw new RollbackFailureException("Boveda origen inactiva");
        if (!boveda.getEstadoMovimiento())
            throw new RollbackFailureException("Boveda origen congelada, no se puede hacer movimientos");

        Moneda moneda = boveda.getMoneda();
        HistorialCaja historialCaja = getHistorialActivo();
        HistorialBoveda historialBoveda = null;

        // obteniendo historial de boveda
        QueryParameter queryParameter1 = QueryParameter.with("idboveda", idBoveda);
        List<HistorialBoveda> listHistBovedas = historialBovedaDAO.findByNamedQuery(
                HistorialBoveda.findByHistorialActivo, queryParameter1.parameters());
        if (listHistBovedas.size() > 1) {
            throw new RollbackFailureException("La boveda tiene mas de un historial activo");
        } else {
            for (HistorialBoveda hist : listHistBovedas) {
                historialBoveda = hist;
            }
        }
        if (historialBoveda == null)
            throw new RollbackFailureException("Boveda no tiene historiales activos");

        // determinando los saldos
        BigDecimal totalTransaccion = BigDecimal.ZERO;
        BigDecimal totalBoveda = BigDecimal.ZERO;
        BigDecimal totalCajaByMoneda = BigDecimal.ZERO;
        Set<DetalleHistorialBoveda> detHistBoveda = historialBoveda.getDetalleHistorialBovedas();
        Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
        for (GenericDetalle detalle : detalleTransaccion) {
            BigDecimal subtotal = detalle.getSubtotal();
            totalTransaccion = totalTransaccion.add(subtotal);
        }
        for (DetalleHistorialBoveda detalle : detHistBoveda) {
            BigInteger cantidad = detalle.getCantidad();
            BigDecimal valor = detalle.getMonedaDenominacion().getValor();
            BigDecimal subtotal = valor.multiply(new BigDecimal(cantidad));
            totalBoveda = totalBoveda.add(subtotal);
        }
        for (BovedaCaja bovedaCaja : bovedasCajas) {
            Boveda bovedaCaj = bovedaCaja.getBoveda();
            if (bovedaCaj.equals(boveda)) {
                totalCajaByMoneda = bovedaCaja.getSaldo();
                break;
            }
        }
        // verificando los que los saldos sean correctos
        if (totalTransaccion.compareTo(BigDecimal.ZERO) < 1)
            throw new RollbackFailureException("Monto de transaccion:" + totalTransaccion.toString()
                    + " invalido");
        switch (origen) {
        case CAJA:
            if (totalCajaByMoneda.compareTo(totalTransaccion) == -1)
                throw new RollbackFailureException("Monto de transaccion:" + totalTransaccion.toString()
                        + "; saldo disponible:" + totalCajaByMoneda.toString()
                        + "; no se puede realizar la transaccion");
            break;
        case BOVEDA:
            if (totalBoveda.compareTo(totalTransaccion) == -1)
                throw new RollbackFailureException("Monto de transaccion:" + totalTransaccion.toString()
                        + "; saldo disponible:" + totalCajaByMoneda.toString()
                        + "; no se puede realizar la transaccion");
            break;
        default:
            throw new RollbackFailureException("Origen de transaccion no definido");
        }

        // creando la transaccion
        TransaccionBovedaCaja transaccionBovedaCaja = new TransaccionBovedaCaja();
        Calendar calendar = Calendar.getInstance();

        transaccionBovedaCaja.setEstadoConfirmacion(false);
        transaccionBovedaCaja.setEstadoSolicitud(true);
        transaccionBovedaCaja.setFecha(calendar.getTime());
        transaccionBovedaCaja.setHora(calendar.getTime());
        transaccionBovedaCaja.setHistorialBoveda(historialBoveda);
        transaccionBovedaCaja.setHistorialCaja(historialCaja);
        transaccionBovedaCaja.setOrigen(TransaccionBovedaCajaOrigen.CAJA);
        transaccionBovedaCaja.setSaldoDisponibleOrigen(totalCajaByMoneda.add(totalTransaccion));
        transaccionBovedaCaja.setSaldoDisponibleDestino(totalBoveda.subtract(totalTransaccion));
        transaccionBovedaCajaDAO.create(transaccionBovedaCaja);

        // creando el detalle de transaccion
        List<MonedaDenominacion> denominaciones = monedaServiceNT.getDenominaciones(moneda.getIdMoneda());
        for (GenericDetalle detalle : detalleTransaccion) {
            TransaccionBovedaCajaDetalle det = new TransaccionBovedaCajaDetalle();
            det.setCantidad(detalle.getCantidad());
            det.setTransaccionBovedaCaja(transaccionBovedaCaja);
            for (MonedaDenominacion monedaDenominacion : denominaciones) {
                BigDecimal valorDenominacion = monedaDenominacion.getValor();
                BigDecimal valorDetalle = detalle.getValor();
                if (valorDenominacion.compareTo(valorDetalle) == 0) {
                    det.setMonedaDenominacion(monedaDenominacion);
                    break;
                }
            }
            detalleTransaccionBovedaCajaDAO.create(det);
        }
        return transaccionBovedaCaja.getIdTransaccionBovedaCaja();
    }

    // @AllowedTo(Permission.ABIERTO)
    // @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransaccionBovedaCajaOrigenBoveda(BigInteger idBoveda, BigInteger idcaja,
            Set<GenericDetalle> detalleTransaccion, TransaccionBovedaCajaOrigen origen)
            throws RollbackFailureException {
        Boveda boveda = bovedaDAO.find(idBoveda);
        Caja caja = cajaDAO.find(idcaja);
        if (boveda == null || caja == null)
            throw new RollbackFailureException("Caja o Boveda no encontrada");

        if (!boveda.getEstado())
            throw new RollbackFailureException("Boveda inactiva");
        if (!boveda.getEstadoMovimiento())
            throw new RollbackFailureException("Boveda congelada, no se puede hacer movimientos");
        if (!caja.getEstado())
            throw new RollbackFailureException("Caja inactiva");
        if (!caja.getEstadoMovimiento())
            throw new RollbackFailureException("Caja congelada, no se puede hacer movimientos");

        Moneda moneda = boveda.getMoneda();
        HistorialCaja historialCaja = getHistorialActivo(caja.getIdCaja());
        HistorialBoveda historialBoveda = null;

        // obteniendo historial de boveda
        QueryParameter queryParameter1 = QueryParameter.with("idboveda", idBoveda);
        List<HistorialBoveda> listHistBovedas = historialBovedaDAO.findByNamedQuery(
                HistorialBoveda.findByHistorialActivo, queryParameter1.parameters());
        if (listHistBovedas.size() > 1) {
            throw new RollbackFailureException("La boveda tiene mas de un historial activo");
        } else {
            for (HistorialBoveda hist : listHistBovedas) {
                historialBoveda = hist;
            }
        }
        if (historialBoveda == null)
            throw new RollbackFailureException("Boveda no tiene historiales activos");

        // determinando los saldos
        BigDecimal totalTransaccion = BigDecimal.ZERO;
        BigDecimal totalBoveda = BigDecimal.ZERO;
        BigDecimal totalCajaByMoneda = BigDecimal.ZERO;
        Set<DetalleHistorialBoveda> detHistBoveda = historialBoveda.getDetalleHistorialBovedas();
        Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
        for (GenericDetalle detalle : detalleTransaccion) {
            BigDecimal subtotal = detalle.getSubtotal();
            totalTransaccion = totalTransaccion.add(subtotal);
        }
        for (DetalleHistorialBoveda detalle : detHistBoveda) {
            BigInteger cantidad = detalle.getCantidad();
            BigDecimal valor = detalle.getMonedaDenominacion().getValor();
            BigDecimal subtotal = valor.multiply(new BigDecimal(cantidad));
            totalBoveda = totalBoveda.add(subtotal);
        }
        for (BovedaCaja bovedaCaja : bovedasCajas) {
            Boveda bovedaCaj = bovedaCaja.getBoveda();
            if (bovedaCaj.equals(boveda)) {
                totalCajaByMoneda = bovedaCaja.getSaldo();
                break;
            }
        }
        // verificando los que los saldos sean correctos
        if (totalTransaccion.compareTo(BigDecimal.ZERO) < 1)
            throw new RollbackFailureException("Monto de transaccion:" + totalTransaccion.toString()
                    + " invalido");

        if (totalBoveda.compareTo(totalTransaccion) == -1) {
            throw new RollbackFailureException("Monto de transaccion:" + totalTransaccion.toString()
                    + "; saldo disponible:" + totalCajaByMoneda.toString()
                    + "; no se puede realizar la transaccion");
        }

        // creando la transaccion
        TransaccionBovedaCaja transaccionBovedaCaja = new TransaccionBovedaCaja();
        Calendar calendar = Calendar.getInstance();

        transaccionBovedaCaja.setEstadoConfirmacion(false);
        transaccionBovedaCaja.setEstadoSolicitud(true);
        transaccionBovedaCaja.setFecha(calendar.getTime());
        transaccionBovedaCaja.setHora(calendar.getTime());
        transaccionBovedaCaja.setHistorialBoveda(historialBoveda);
        transaccionBovedaCaja.setHistorialCaja(historialCaja);
        transaccionBovedaCaja.setOrigen(TransaccionBovedaCajaOrigen.BOVEDA);
        transaccionBovedaCaja.setSaldoDisponibleOrigen(totalBoveda.subtract(totalTransaccion));
        transaccionBovedaCaja.setSaldoDisponibleDestino(totalCajaByMoneda.add(totalTransaccion));
        transaccionBovedaCajaDAO.create(transaccionBovedaCaja);

        // creando el detalle de transaccion
        List<MonedaDenominacion> denominaciones = monedaServiceNT.getDenominaciones(moneda.getIdMoneda());
        for (GenericDetalle detalle : detalleTransaccion) {
            TransaccionBovedaCajaDetalle det = new TransaccionBovedaCajaDetalle();
            det.setCantidad(detalle.getCantidad());
            det.setTransaccionBovedaCaja(transaccionBovedaCaja);
            for (MonedaDenominacion monedaDenominacion : denominaciones) {
                BigDecimal valorDenominacion = monedaDenominacion.getValor();
                BigDecimal valorDetalle = detalle.getValor();
                if (valorDenominacion.compareTo(valorDetalle) == 0) {
                    det.setMonedaDenominacion(monedaDenominacion);
                    break;
                }
            }
            detalleTransaccionBovedaCajaDAO.create(det);
        }
        return transaccionBovedaCaja.getIdTransaccionBovedaCaja();
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransaccionCajaCaja(BigInteger idCajadestino, BigInteger idMoneda,
            BigDecimal monto, String observacion) throws RollbackFailureException {
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new RollbackFailureException("Monto de transaccion no valido, debe de ser mayor que cero");

        Caja cajaDestino = cajaDAO.find(idCajadestino);
        Caja cajaOrigen = this.getCaja();
        Moneda monedaTransaccion = monedaDAO.find(idMoneda);

        if (cajaOrigen.equals(cajaDestino))
            throw new RollbackFailureException("Caja origen y destino deben de ser diferentes");

        BovedaCaja bovedaCajaOrigen = null;
        Set<BovedaCaja> bovedaCajasOrigen = cajaOrigen.getBovedaCajas();
        BovedaCaja bovedaCajaDestino = null;
        Set<BovedaCaja> bovedaCajasDestino = cajaDestino.getBovedaCajas();
        for (BovedaCaja bc : bovedaCajasOrigen) {
            Boveda boveda = bc.getBoveda();
            Moneda moneda = boveda.getMoneda();
            if (moneda.equals(monedaTransaccion)) {
                bovedaCajaOrigen = bc;
            }
        }
        for (BovedaCaja bc : bovedaCajasDestino) {
            Boveda boveda = bc.getBoveda();
            Moneda moneda = boveda.getMoneda();
            if (moneda.equals(monedaTransaccion)) {
                bovedaCajaDestino = bc;
            }
        }
        if (bovedaCajaOrigen == null)
            throw new RollbackFailureException("Moneda no encontrada en la caja Origen");
        if (bovedaCajaDestino == null)
            throw new RollbackFailureException("Moneda no encontrada en la caja Destino");

        BigDecimal saldoActualCajaOrigen = bovedaCajaOrigen.getSaldo();
        BigDecimal saldoActualCajaDestino = bovedaCajaDestino.getSaldo();
        if (saldoActualCajaOrigen.compareTo(monto) == -1)
            throw new RollbackFailureException("El saldo de caja es:" + saldoActualCajaOrigen.toString()
                    + ";monto de transaccion:" + monto.toString() + ". No se puede realizar la transaccion");

        // saldos disponibles
        BigDecimal saldoDisponibleOrigen = saldoActualCajaOrigen.subtract(monto);
        BigDecimal saldoDisponibleDestino = saldoActualCajaDestino.add(monto);

        // creando la transaccion
        Calendar calendar = Calendar.getInstance();

        HistorialCaja historialCajaOrigen = getHistorialActivo();
        HistorialCaja historialCajaDestino = getHistorialActivo(cajaDestino.getIdCaja());

        TransaccionCajaCaja transaccion = new TransaccionCajaCaja();
        transaccion.setMonto(monto);
        transaccion.setFecha(calendar.getTime());
        transaccion.setHora(calendar.getTime());
        transaccion.setEstadoConfirmacion(false);
        transaccion.setEstadoSolicitud(true);
        transaccion.setHistorialCajaOrigen(historialCajaOrigen);

        transaccion.setHistorialCajaDestino(historialCajaDestino);
        transaccion.setMoneda(monedaTransaccion);
        transaccion.setObservacion(observacion);
        transaccion.setSaldoDisponibleOrigen(saldoDisponibleOrigen);
        transaccion.setSaldoDisponibleDestino(saldoDisponibleDestino);

        transaccionCajaCajaDAO.create(transaccion);

        return transaccion.getIdTransaccionCajaCaja();
    }

    @Override
    public void cancelarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja)
            throws RollbackFailureException {
        TransaccionBovedaCaja transaccionBovedaCaja = transaccionBovedaCajaDAO.find(idTransaccionBovedaCaja);
        if (transaccionBovedaCaja == null)
            throw new RollbackFailureException("Transaccion no encontrada");
        if (transaccionBovedaCaja.getEstadoConfirmacion() == true)
            throw new RollbackFailureException("La transaccion ya fue CONFIRMADA, no se puede cancelar");
        if (transaccionBovedaCaja.getEstadoSolicitud() == false)
            throw new RollbackFailureException(
                    "La transaccion ya fue CANCELADA, no se puede cancelar nuevamente");
        transaccionBovedaCaja.setEstadoSolicitud(false);
        transaccionBovedaCajaDAO.update(transaccionBovedaCaja);
    }

    @Override
    public void confirmarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja)
            throws RollbackFailureException {
        TransaccionBovedaCaja transaccionBovedaCaja = transaccionBovedaCajaDAO.find(idTransaccionBovedaCaja);

        if (transaccionBovedaCaja == null)
            throw new RollbackFailureException("Transaccion no Encontrada");
        if (transaccionBovedaCaja.getEstadoSolicitud() == false)
            throw new RollbackFailureException("La transaccion ya fue CANCELADA, no se puede confirmar");
        if (transaccionBovedaCaja.getEstadoConfirmacion() == true)
            throw new RollbackFailureException(
                    "La transaccion ya fue CONFIRMADA, no se puede confirmar nuevamente");

        TransaccionBovedaCajaView view = transaccionBovedaCajaViewDAO.find(idTransaccionBovedaCaja);
        Boveda boveda = transaccionBovedaCaja.getHistorialBoveda().getBoveda();
        BigDecimal monto = view.getMonto();
        Moneda moneda = transaccionBovedaCaja.getHistorialBoveda().getBoveda().getMoneda();

        switch (transaccionBovedaCaja.getOrigen()) {
        case CAJA:
            this.actualizarSaldoCaja(transaccionBovedaCaja.getHistorialCaja().getCaja().getIdCaja(),
                    monto.negate(), moneda.getIdMoneda());
            this.actualizarSaldoBoveda(boveda.getIdBoveda(),
                    transaccionBovedaCaja.getTransaccionBovedaCajaDetalls(), 1);
            break;
        case BOVEDA:
            this.actualizarSaldoCaja(monto, moneda.getIdMoneda());
            this.actualizarSaldoBoveda(boveda.getIdBoveda(),
                    transaccionBovedaCaja.getTransaccionBovedaCajaDetalls(), -1);
            break;
        default:
            throw new RollbackFailureException("Origen de transaccion no valido");
        }
        transaccionBovedaCaja.setEstadoConfirmacion(true);
        transaccionBovedaCajaDAO.update(transaccionBovedaCaja);
    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public void cancelarTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) throws RollbackFailureException {
        TransaccionCajaCaja transaccion = transaccionCajaCajaDAO.find(idTransaccionCajaCaja);
        if (transaccion == null)
            throw new RollbackFailureException("Transaccion no encontrada");
        if (transaccion.getEstadoConfirmacion() == true)
            throw new RollbackFailureException("Transaccion ya fue CONFIRMADA, no se puede cancelar");
        if (transaccion.getEstadoSolicitud() == false)
            throw new RollbackFailureException(
                    "Transaccion ya fue CANCELADA, no se puede cancelar nuevamente");
        transaccion.setEstadoSolicitud(false);
        transaccionCajaCajaDAO.update(transaccion);
    }

    @AllowedTo(Permission.ABIERTO)
    @Override
    public void confirmarTransaccionCajaCaja(BigInteger idTransaccionCajaCaja)
            throws RollbackFailureException {
        TransaccionCajaCaja transaccion = transaccionCajaCajaDAO.find(idTransaccionCajaCaja);
        if (transaccion == null)
            throw new RollbackFailureException("Transaccion no encontrada");
        if (transaccion.getEstadoSolicitud() == false)
            throw new RollbackFailureException("Transaccion ya fue CANCELADA, no se puede confirmar");
        if (transaccion.getEstadoConfirmacion() == true)
            throw new RollbackFailureException(
                    "Transaccion ya fue CONFIRMADA, no se puede confirmar nuevamente");
        Caja cajaOrigen = transaccion.getHistorialCajaOrigen().getCaja();
        BigInteger idCajaOrigen = cajaOrigen.getIdCaja();
        BigDecimal monto = transaccion.getMonto();
        Moneda moneda = transaccion.getMoneda();
        this.actualizarSaldoCaja(monto, moneda.getIdMoneda());
        this.actualizarSaldoCaja(idCajaOrigen, monto.negate(), moneda.getIdMoneda());
        transaccion.setEstadoConfirmacion(true);
        transaccionCajaCajaDAO.update(transaccion);
    }

    //@AllowedTo(Permission.ABIERTO)
    //@AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransaccionGiro(BigInteger idAgenciaOrigen, BigInteger idAgenciaDestino,
            String numeroDocumentoEmisor, String clienteEmisor, String numeroDocumentoReceptor,
            String clienteReceptor, BigInteger idMoneda, BigDecimal monto, BigDecimal comision,
            LugarPagoComision lugarPagoComision, boolean estadoPagoComision) throws RollbackFailureException {

        Calendar calendar = Calendar.getInstance();

        Agencia agenciaOrigen = agenciaDAO.find(idAgenciaOrigen);
        Agencia agenciaDestino = agenciaDAO.find(idAgenciaDestino);
        Moneda moneda = monedaDAO.find(idMoneda);

        Giro giro = new Giro();

        giro.setAgenciaOrigen(agenciaOrigen);
        giro.setAgenciaDestino(agenciaDestino);

        giro.setNumeroDocumentoEmisor(numeroDocumentoEmisor);
        giro.setClienteEmisor(clienteEmisor);
        giro.setNumeroDocumentoReceptor(numeroDocumentoReceptor);
        giro.setClienteReceptor(clienteReceptor);

        giro.setMoneda(moneda);
        giro.setMonto(monto);
        giro.setComision(comision);
        giro.setLugarPagoComision(lugarPagoComision);
        giro.setEstadoPagoComision(estadoPagoComision);

        giro.setEstado(EstadoGiro.ENVIADO);

        giro.setFechaEnvio(calendar.getTime());

        giroDAO.create(giro);

        return giro.getIdGiro();

    }

    @AllowedTo(Permission.ABIERTO)
    @AllowedToEstadoMovimiento(EstadoMovimiento.DESCONGELADO)
    @Override
    public BigInteger crearTransaccionSobreGiro(BigInteger idSocio, BigInteger idMoneda, BigDecimal monto,
            BigDecimal interes, Date fechaLimitePago) throws RollbackFailureException {

        Socio socio = socioDAO.find(idSocio);
        Moneda moneda = monedaDAO.find(idMoneda);
        if (socio == null)
            throw new RollbackFailureException("Socio no encontrado");
        if (moneda == null)
            throw new RollbackFailureException("Moneda no encontrado");
        if (monto.compareTo(BigDecimal.ZERO) != 1) {
            throw new RollbackFailureException("Monto invalido para transaccion");
        }

        // crear trasccion
        Calendar calendar = Calendar.getInstance();

        SobreGiro sobreGiro = new SobreGiro();
        sobreGiro.setSocio(socio);
        sobreGiro.setMonto(monto);
        sobreGiro.setMoneda(moneda);
        sobreGiro.setInteres(interes);
        sobreGiro.setFechaCreacion(calendar.getTime());
        sobreGiro.setFechaLimitePago(fechaLimitePago);
        sobreGiro.setEstado(EstadoSobreGiro.ACTIVO);
        sobreGiroDAO.create(sobreGiro);

        // actualizar saldo caja
        this.actualizarSaldoCaja(monto.abs().negate(), moneda.getIdMoneda());
        return sobreGiro.getIdSobreGiro();
    }

    @Override
    public BigInteger crearTransaccionHistorialSobreGiro(BigInteger idSobreGiro, BigDecimal monto)
            throws RollbackFailureException {
        SobreGiro sobreGiro = sobreGiroDAO.find(idSobreGiro);
        if (sobreGiro == null)
            throw new RollbackFailureException("Sobregiro no encontrado");
        if (!sobreGiro.getEstado().equals(EstadoSobreGiro.ACTIVO))
            throw new RollbackFailureException("Sobregiro no activo");
        if (monto.compareTo(BigDecimal.ZERO) != 1) {
            throw new RollbackFailureException("Monto invalido para transaccion");
        }

        BigDecimal montoTotalDeuda = sobreGiro.getMonto().add(sobreGiro.getInteres());
        BigDecimal montoActualPagado = BigDecimal.ZERO;
        Set<HistorialPagoSobreGiro> list = sobreGiro.getHistorialPagos();
        for (HistorialPagoSobreGiro historialPagoSobreGiro : list) {
            montoActualPagado = montoActualPagado.add(historialPagoSobreGiro.getMonto());
        }

        if (montoTotalDeuda.compareTo(montoActualPagado.add(monto)) == 0) {
            sobreGiro.setEstado(EstadoSobreGiro.PAGADO);
            sobreGiroDAO.update(sobreGiro);
        } else if (montoTotalDeuda.compareTo(montoActualPagado.add(monto)) == 1) {

        } else if (montoTotalDeuda.compareTo(montoActualPagado.add(monto)) == -1) {
            throw new RollbackFailureException("El monto enviado supera al saldo total de deuda");
        }

        // crear trasccion
        Calendar calendar = Calendar.getInstance();
        HistorialPagoSobreGiro historialPagoSobreGiro = new HistorialPagoSobreGiro();
        historialPagoSobreGiro.setFecha(calendar.getTime());
        historialPagoSobreGiro.setMonto(monto);
        historialPagoSobreGiro.setSobreGiro(sobreGiro);
        historialPagoSobreGiroDAO.create(historialPagoSobreGiro);

        // actualizar saldo caja
        this.actualizarSaldoCaja(monto.abs(), sobreGiro.getMoneda().getIdMoneda());
        return sobreGiro.getIdSobreGiro();
    }

}
