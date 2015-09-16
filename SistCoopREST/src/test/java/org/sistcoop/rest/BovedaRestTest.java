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
package org.sistcoop.rest;

import java.math.BigInteger;
import java.util.Set;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sistemafinanciero.controller.AgenciaServiceBeanNT;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.impl.AgenciaBeanDAO;
import org.sistemafinanciero.entity.Accionista;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.BovedaCaja;
import org.sistemafinanciero.entity.BovedaCajaId;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.CuentaBancariaInteresGenera;
import org.sistemafinanciero.entity.CuentaBancariaTasa;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.Departamento;
import org.sistemafinanciero.entity.DetalleHistorialBoveda;
import org.sistemafinanciero.entity.DetalleHistorialCaja;
import org.sistemafinanciero.entity.DetalleTransaccionCaja;
import org.sistemafinanciero.entity.Distrito;
import org.sistemafinanciero.entity.Entidad;
import org.sistemafinanciero.entity.EstadocuentaAportesView;
import org.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.sistemafinanciero.entity.HistorialBoveda;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.MonedaDenominacion;
import org.sistemafinanciero.entity.Pais;
import org.sistemafinanciero.entity.PendienteCaja;
import org.sistemafinanciero.entity.PersonaJuridica;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Provincia;
import org.sistemafinanciero.entity.Servicio;
import org.sistemafinanciero.entity.Socio;
import org.sistemafinanciero.entity.Sucursal;
import org.sistemafinanciero.entity.TasaInteres;
import org.sistemafinanciero.entity.TipoDocumento;
import org.sistemafinanciero.entity.TipoServicio;
import org.sistemafinanciero.entity.Titular;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TrabajadorCaja;
import org.sistemafinanciero.entity.TrabajadorCajaId;
import org.sistemafinanciero.entity.TransaccionBancaria;
import org.sistemafinanciero.entity.TransaccionBovedaCaja;
import org.sistemafinanciero.entity.TransaccionBovedaCajaDetalle;
import org.sistemafinanciero.entity.TransaccionBovedaOtro;
import org.sistemafinanciero.entity.TransaccionBovedaOtroDetall;
import org.sistemafinanciero.entity.TransaccionCajaCaja;
import org.sistemafinanciero.entity.TransaccionCompraVenta;
import org.sistemafinanciero.entity.TransaccionCuentaAporte;
import org.sistemafinanciero.entity.TransferenciaBancaria;
import org.sistemafinanciero.entity.ValorTasaInteres;
import org.sistemafinanciero.entity.VariableSistema;
import org.sistemafinanciero.entity.type.EstadoCuentaAporte;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.EstadoSocioAporte;
import org.sistemafinanciero.entity.type.Estadocivil;
import org.sistemafinanciero.entity.type.Sexo;
import org.sistemafinanciero.entity.type.TasaInteresType;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoEmpresa;
import org.sistemafinanciero.entity.type.TipoPendienteCaja;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.entity.type.Tipotransaccionbancaria;
import org.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.sistemafinanciero.entity.type.Tipotransaccioncuentaaporte;
import org.sistemafinanciero.entity.type.TransaccionBovedaCajaOrigen;
import org.sistemafinanciero.entity.type.Variable;
import org.sistemafinanciero.service.nt.AbstractServiceNT;
import org.sistemafinanciero.service.nt.AgenciaServiceNT;
import org.sistemafinanciero.util.Resources;

@RunWith(Arquillian.class)
public class BovedaRestTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClasses(Accionista.class, Agencia.class, Beneficiario.class, Boveda.class, BovedaCaja.class, BovedaCajaId.class, Caja.class, CuentaAporte.class, CuentaBancaria.class, CuentaBancariaInteresGenera.class, CuentaBancariaTasa.class, CuentaBancariaView.class, Departamento.class, DetalleHistorialBoveda.class, DetalleHistorialCaja.class, DetalleTransaccionCaja.class, Distrito.class, Entidad.class, EstadocuentaAportesView.class, EstadocuentaBancariaView.class, HistorialBoveda.class, HistorialCaja.class, HistorialTransaccionCaja.class, Moneda.class, MonedaDenominacion.class,
						Pais.class, PendienteCaja.class, PersonaJuridica.class, PersonaNatural.class, Provincia.class, Servicio.class, Socio.class, Sucursal.class, TasaInteres.class, TipoDocumento.class, TipoServicio.class, Titular.class, Trabajador.class, TrabajadorCaja.class, TrabajadorCajaId.class, TransaccionBancaria.class, TransaccionBovedaCaja.class, TransaccionBovedaCajaDetalle.class, TransaccionBovedaOtro.class, TransaccionBovedaOtroDetall.class, TransaccionCajaCaja.class, TransaccionCompraVenta.class, TransaccionCuentaAporte.class, TransferenciaBancaria.class, ValorTasaInteres.class,
						VariableSistema.class,

						Estadocivil.class, EstadoCuentaAporte.class, EstadoCuentaBancaria.class, EstadoSocioAporte.class, Sexo.class, TasaInteresType.class, TipoCuentaBancaria.class, TipoEmpresa.class, TipoPendienteCaja.class, TipoPersona.class, Tipotransaccionbancaria.class, Tipotransaccioncompraventa.class, Tipotransaccioncuentaaporte.class, TransaccionBovedaCajaOrigen.class, Variable.class,

						AbstractServiceNT.class, AgenciaServiceNT.class, AgenciaServiceBeanNT.class, DAO.class, AgenciaBeanDAO.class,

						Resources.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "META-INF/beans.xml")
				// Deploy our test datasource
				.addAsWebInfResource("test-ds.xml");
	}

	@EJB
	AgenciaServiceNT agenciaServiceNT;

	@Test
	public void testRegister() throws Exception {
		Set<Caja> list = agenciaServiceNT.getCajasOfAgencia(BigInteger.ONE);
		System.out.println("Resultado:" + list.toString());
	}

}
