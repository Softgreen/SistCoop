/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sistemafinanciero.rest.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.PersonaJuridica;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.type.LugarPagoComision;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPendienteCaja;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.SessionREST;
import org.sistemafinanciero.rest.dto.CuentaBancariaDTO;
import org.sistemafinanciero.rest.dto.TransaccionBancariaDTO;
import org.sistemafinanciero.rest.dto.TransaccionChequeDTO;
import org.sistemafinanciero.rest.dto.TransaccionCompraVentaDTO;
import org.sistemafinanciero.rest.dto.TransaccionCuentaAporteDTO;
import org.sistemafinanciero.rest.dto.TransaccionGiroDTO;
import org.sistemafinanciero.rest.dto.TransaccionHistorialSobreGiroDTO;
import org.sistemafinanciero.rest.dto.TransaccionSobreGiroDTO;
import org.sistemafinanciero.rest.dto.TransferenciaBancariaDTO;
import org.sistemafinanciero.service.nt.PersonaJuridicaServiceNT;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.nt.SessionServiceNT;
import org.sistemafinanciero.service.ts.SessionServiceTS;

@NoCache
public class SessionRESTService implements SessionREST {

    @EJB
    private SessionServiceNT sessionServiceNT;

    @EJB
    private SessionServiceTS sessionServiceTS;

    @EJB
    private PersonaNaturalServiceNT personaNaturalServiceNT;

    @EJB
    private PersonaJuridicaServiceNT personaJuridicaServiceNT;

    @Override
    public Response getCajaOfSession() {
        Caja caja = sessionServiceNT.getCajaOfSession();
        Response response = Response.status(Response.Status.OK).entity(caja).build();
        return response;
    }

    @Override
    public Response abrirCajaOfSession() {
        Response response;
        try {
            BigInteger idHistorial = sessionServiceTS.abrirCaja();
            response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idHistorial)).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response cerrarCajaOfSession(Set<GenericMonedaDetalle> detalle) {
        Response response;
        try {
            Map<Boveda, BigDecimal> diferencia = sessionServiceTS.getDiferenciaSaldoCaja(detalle);
            if (diferencia.size() > 0) {
                JsonArrayBuilder result = Json.createArrayBuilder();
                for (Boveda boveda : diferencia.keySet()) {
                    BigDecimal dif = diferencia.get(boveda);
                    JsonObject obj = Json.createObjectBuilder().add("idboveda", boveda.getIdBoveda())
                            .add("boveda", boveda.getDenominacion()).add("monto", dif).build();
                    result.add(obj);
                }
                return Response.status(Response.Status.BAD_REQUEST).entity(result.build()).build();
            }
            BigInteger idHistorialCaja = sessionServiceTS.cerrarCaja(detalle);
            JsonObject model = Json.createObjectBuilder().add("message", "Caja cerrada")
                    .add("id", idHistorialCaja).build();
            return Response.status(Response.Status.OK).entity(model).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response getAgenciaOfSession() {
        Agencia agencia = sessionServiceNT.getAgenciaOfSession();
        Response response = Response.status(Response.Status.OK).entity(agencia).build();
        return response;
    }

    @Override
    public Response getUsuario(SecurityContext context) {
        KeycloakPrincipal p = (KeycloakPrincipal) context.getUserPrincipal();
        KeycloakSecurityContext kcSecurityContext = p.getKeycloakSecurityContext();
        String username = kcSecurityContext.getToken().getPreferredUsername();
        Response response = Response.status(Response.Status.OK).entity(username).build();
        return response;
    }

    @Override
    public Response getPersonaOfSession() {
        PersonaNatural personaNatural = sessionServiceNT.getPersonaOfSession();
        Response response = Response.status(Response.Status.OK).entity(personaNatural).build();
        return response;
    }

    @Override
    public Response desactivarSocio(@PathParam("id") BigInteger id) {
        Response response;
        try {
            BigInteger idTransaccion = sessionServiceTS.cancelarSocioConRetiro(id);
            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response createCuentaPlazoFijo(CuentaBancariaDTO cuentaBancaria) {
        Response response;

        TipoCuentaBancaria tipoCuentaBancaria = cuentaBancaria.getTipoCuenta();
        BigInteger idMoneda = cuentaBancaria.getIdMoneda();
        BigDecimal monto = cuentaBancaria.getMonto();
        BigDecimal tasaInteres = cuentaBancaria.getTasaInteres();
        TipoPersona tipoPersona = cuentaBancaria.getTipoPersona();
        int periodo = cuentaBancaria.getPeriodo();
        int cantRetirantes = cuentaBancaria.getCantRetirantes();
        List<BigInteger> titulares = cuentaBancaria.getTitulares();
        List<Beneficiario> beneficiarios = cuentaBancaria.getBeneficiarios();

        Agencia agencia = sessionServiceNT.getAgenciaOfSession();

        try {
            BigInteger[] idCuentaAndTransaccion = null;
            if (tipoPersona.equals(TipoPersona.NATURAL)) {
                PersonaNatural persona = personaNaturalServiceNT.find(cuentaBancaria.getIdTipoDocumento(),
                        cuentaBancaria.getNumeroDocumento());
                idCuentaAndTransaccion = sessionServiceTS.crearCuentaBancariaPlazoFijoConDeposito(
                        tipoCuentaBancaria, agencia.getCodigo(), idMoneda, monto, tasaInteres, tipoPersona,
                        persona.getIdPersonaNatural(), periodo, cantRetirantes, titulares, beneficiarios);
            } else if (tipoPersona.equals(TipoPersona.JURIDICA)) {
                PersonaJuridica persona = personaJuridicaServiceNT.find(cuentaBancaria.getIdTipoDocumento(),
                        cuentaBancaria.getNumeroDocumento());
                idCuentaAndTransaccion = sessionServiceTS.crearCuentaBancariaPlazoFijoConDeposito(
                        tipoCuentaBancaria, agencia.getCodigo(), idMoneda, monto, tasaInteres, tipoPersona,
                        persona.getIdPersonaJuridica(), periodo, cantRetirantes, titulares, beneficiarios);
            }
            JsonObject model = Json.createObjectBuilder().add("message", "Cuenta creada")
                    .add("id", idCuentaAndTransaccion[0]).add("idTransaccion", idCuentaAndTransaccion[1])
                    .build();
            return Response.status(Response.Status.OK).entity(model).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearPendienteCaja(TipoPendienteCaja tipoPendienteCaja, BigInteger idboveda,
            BigDecimal monto, String observacion, BigInteger idPendienteRelacionado) {
        Response response;
        try {
            BigInteger idPendiente = sessionServiceTS.crearPendienteCaja(tipoPendienteCaja, idboveda, monto,
                    observacion, idPendienteRelacionado);
            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idPendiente))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearTransaccionCuentaAporte(TransaccionCuentaAporteDTO transaccion) {
        Response response;
        try {
            BigInteger idSocio = transaccion.getIdSocio();
            BigDecimal monto = transaccion.getMonto();
            int mes = transaccion.getMes();
            int anio = transaccion.getAnio();
            String referencia = transaccion.getReferencia();
            BigInteger idAporte = sessionServiceTS.crearAporte(idSocio, monto, mes, anio, referencia);
            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idAporte))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearTransaccionCompraVenta(TransaccionCompraVentaDTO transaccion) {
        Response response;
        try {
            Tipotransaccioncompraventa tipoTransaccion = transaccion.getTipoOperacion();
            BigInteger idMonedaRecibido = transaccion.getIdMonedaRecibida();
            BigInteger idMonedaEntregado = transaccion.getIdMonedaEntregada();
            BigDecimal montoRecibido = transaccion.getMontoRecibido();
            BigDecimal montoEntregado = transaccion.getMontoEntregado();
            BigDecimal tasaCambio = transaccion.getTasaCambio();
            String referencia = transaccion.getReferencia();
            BigInteger idTransaccion = sessionServiceTS.crearTransaccionCompraVenta(tipoTransaccion,
                    idMonedaRecibido, idMonedaEntregado, montoRecibido, montoEntregado, tasaCambio,
                    referencia);
            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearTransaccionBancaria(TransaccionBancariaDTO transaccion) {
        Response response;
        try {
            String numeroCuenta = transaccion.getNumeroCuenta();
            BigDecimal monto = transaccion.getMonto();
            String referencia = transaccion.getReferencia();
            BigDecimal interes = transaccion.getInteres();
            BigInteger idTransaccion = sessionServiceTS.crearTransaccionBancaria(numeroCuenta, monto,
                    referencia, interes);
            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearTransaccionCheque(TransaccionChequeDTO transaccion) {
        Response response;
        try {
            String numeroChequeUnico = transaccion.getNumeroChequeUnico();
            String tipoDocumento = transaccion.getTipoDocumento();
            String numeroDocumento = transaccion.getNumeroDocumento();
            String persona = transaccion.getPersona();
            String observacion = transaccion.getObservacion();
            BigDecimal monto = transaccion.getMonto();

            BigInteger idTransaccion = sessionServiceTS.crearTransaccionCheque(numeroChequeUnico, monto,
                    tipoDocumento, numeroDocumento, persona, observacion);
            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearTransferencia(TransferenciaBancariaDTO transferencia) {
        Response response;
        try {
            String numeroCuentaOrigen = transferencia.getNumeroCuentaOrigen();
            String numeroCuentaDestino = transferencia.getNumeroCuentaDestino();
            BigDecimal monto = transferencia.getMonto();
            String referencia = transferencia.getReferencia();

            BigInteger idTransferencia = sessionServiceTS.crearTransferenciaBancaria(numeroCuentaOrigen,
                    numeroCuentaDestino, monto, referencia);
            response = Response.status(Response.Status.CREATED)
                    .entity(Jsend.getSuccessJSend(idTransferencia)).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response cancelarCuentaBancariaConRetiro(BigInteger id) {
        Response response;
        try {
            BigInteger idTransaccion = sessionServiceTS.cancelarCuentaBancariaConRetiro(id);
            response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response createTransaccionCajaCaja(BigInteger idCaja, BigInteger idMoneda, BigDecimal monto,
            String observacion) {
        Response response;
        try {
            BigInteger idTransaccion = sessionServiceTS.crearTransaccionCajaCaja(idCaja, idMoneda, monto,
                    observacion);
            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response confirmarTransaccionCajaCaja(BigInteger id) {
        Response response;
        try {
            sessionServiceTS.confirmarTransaccionCajaCaja(id);
            response = Response.status(Response.Status.NO_CONTENT).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response cancelarTransaccionCajaCaja(BigInteger id) {
        Response response;
        try {
            sessionServiceTS.cancelarTransaccionCajaCaja(id);
            response = Response.status(Response.Status.NO_CONTENT).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response createTransaccionBovedaCaja(TransaccionBovedaCajaOrigen origen,
            Set<GenericDetalle> detalleTransaccion, BigInteger idboveda, BigInteger idcaja) {
        Response response;
        BigInteger idTransaccion = null;
        try {
            if (origen.equals(TransaccionBovedaCajaOrigen.CAJA)) {
                idTransaccion = sessionServiceTS.crearTransaccionBovedaCaja(idboveda, detalleTransaccion,
                        origen);
            }
            if (origen.equals(TransaccionBovedaCajaOrigen.BOVEDA)) {
                idTransaccion = sessionServiceTS.crearTransaccionBovedaCajaOrigenBoveda(idboveda, idcaja,
                        detalleTransaccion, origen);
            }
            response = Response.status(Response.Status.OK).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response confirmarTransaccionBovedaCaja(BigInteger id) {
        Response response;
        try {
            sessionServiceTS.confirmarTransaccionBovedaCaja(id);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response cancelarTransaccionBovedaCaja(BigInteger id) {
        Response response;
        try {
            sessionServiceTS.cancelarTransaccionBovedaCaja(id);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response extornar(BigInteger id) {
        Response response = null;
        try {
            sessionServiceTS.extornarTransaccion(id);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearTransaccionGiro(TransaccionGiroDTO transaccion) {
        Response response;
        try {
            BigInteger idAgenciaOrigen = transaccion.getIdAgenciaOrigen();
            BigInteger idAgenciaDestino = transaccion.getIdAgenciaDestino();
            String numeroDocumentoEmisor = transaccion.getNumeroDocumentoEmisor();
            String clienteEmisor = transaccion.getClienteEmisor();
            String numeroDocumentoReceptor = transaccion.getNumeroDocumentoReceptor();
            String clienteReceptor = transaccion.getClienteReceptor();
            BigInteger idMoneda = transaccion.getIdMoneda();
            BigDecimal monto = transaccion.getMonto();
            BigDecimal comision = transaccion.getComision();
            LugarPagoComision lugarPagoComision = transaccion.getLugarPagoComision();
            boolean estadoPagoComision = transaccion.isEstadoPagoComision();

            BigInteger idTransaccion = sessionServiceTS.crearTransaccionGiro(idAgenciaOrigen,
                    idAgenciaDestino, numeroDocumentoEmisor, clienteEmisor, numeroDocumentoReceptor,
                    clienteReceptor, idMoneda, monto, comision, lugarPagoComision, estadoPagoComision);

            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response extornarTransaccionGiro(BigInteger idGiro) {
        Response response;
        try {
            sessionServiceTS.extornarGiro(idGiro);
            response = Response.status(Response.Status.OK).build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearTransaccionSobreGiro(TransaccionSobreGiroDTO transaccion) {
        Response response;
        try {
            BigInteger idSocio = transaccion.getIdSocio();
            BigInteger idMoneda = transaccion.getIdMoneda();

            BigDecimal monto = transaccion.getMonto();
            BigDecimal interes = transaccion.getInteres();
            Date fechaLimitePago = new Date(transaccion.getFechaLimitePago());

            BigInteger idTransaccion = sessionServiceTS.crearTransaccionSobreGiro(idSocio, idMoneda, monto,
                    interes, fechaLimitePago);

            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

    @Override
    public Response crearTransaccionHistorialSobreGiro(TransaccionHistorialSobreGiroDTO historial) {
        Response response;
        try {
            BigInteger idSobreGiro = historial.getIdSobreGiro();
            BigDecimal monto = historial.getMonto();

            BigInteger idTransaccion = sessionServiceTS
                    .crearTransaccionHistorialSobreGiro(idSobreGiro, monto);

            response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTransaccion))
                    .build();
        } catch (RollbackFailureException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        } catch (EJBException e) {
            Jsend jsend = Jsend.getErrorJSend(e.getMessage());
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
        }
        return response;
    }

}
