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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Titular;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.CuentaBancariaREST;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.dto.CuentaBancariaDTO;
import org.sistemafinanciero.rest.dto.TitularDTO;
import org.sistemafinanciero.service.nt.AgenciaServiceNT;
import org.sistemafinanciero.service.nt.CuentaBancariaServiceNT;
import org.sistemafinanciero.service.nt.MonedaServiceNT;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.nt.SessionServiceNT;
import org.sistemafinanciero.service.ts.CuentaBancariaServiceTS;
import org.sistemafinanciero.util.NumLetrasJ;
import org.sistemafinanciero.util.NumLetrasJ.Tipo;
import org.sistemafinanciero.util.ProduceObject;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class CuentaBancariaRESTService implements CuentaBancariaREST {

	private final static String baseUrl = "/cuentasBancarias";

	private final static String certificadoURL = "D:\\pdf";

	@EJB
	private CuentaBancariaServiceNT cuentaBancariaServiceNT;

	@EJB
	private CuentaBancariaServiceTS cuentaBancariaServiceTS;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	@EJB
	private MonedaServiceNT monedaServiceNT;
	
	@EJB
	private AgenciaServiceNT agenciaServiceNT;

	@EJB
	private SessionServiceNT sessionServiceNT;

	@Override
	public Response findAll(String filterText, TipoCuentaBancaria[] tipoCuenta, TipoPersona[] tipoPersona, EstadoCuentaBancaria[] tipoEstadoCuenta, Integer offset, Integer limit) {
		List<CuentaBancariaView> list = cuentaBancariaServiceNT.findAllView(filterText, tipoCuenta, tipoPersona, tipoEstadoCuenta, offset, limit);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response count() {
		int count = cuentaBancariaServiceNT.count();
		Response response = Response.status(Response.Status.OK).entity(count).build();
		return response;
	}

	@Override
	public Response buscarByNumeroCuenta(String numeroCuenta) {
		CuentaBancariaView cuentaBancariaView = cuentaBancariaServiceNT.findByNumeroCuenta(numeroCuenta);
		Response response = Response.status(Response.Status.OK).entity(cuentaBancariaView).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		CuentaBancariaView cuentaBancariaView = cuentaBancariaServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(cuentaBancariaView).build();
		return response;
	}

	@Override
	public Response getCertificado(BigInteger id) {
		OutputStream file;

		CuentaBancariaView cuentaBancaria = cuentaBancariaServiceNT.findById(id);
		String codigoAgencia = ProduceObject.getCodigoAgenciaFromNumeroCuenta(cuentaBancaria.getNumeroCuenta());
		
		Agencia agencia = agenciaServiceNT.findByCodigo(codigoAgencia);
		
		if (agencia == null) {
			JsonObject model = Json.createObjectBuilder().add("message", "Agencia no encontrado").build();
			return Response.status(Response.Status.NOT_FOUND).entity(model).build();
		}		

		try {
			file = new FileOutputStream(new File(certificadoURL + "\\" + id + ".pdf"));
			Document document = new Document(PageSize.A5.rotate());
			PdfWriter writer = PdfWriter.getInstance(document, file);
			document.open();

			Font font = FontFactory.getFont("Times-Roman", 7);

			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));

			Paragraph paragraph1 = new Paragraph();
			paragraph1.setFont(font);
			Chunk numeroCuenta1 = new Chunk("Nº CUENTA:");
			Chunk numeroCuenta2 = new Chunk(cuentaBancaria.getNumeroCuenta());
			paragraph1.add(numeroCuenta1);
			paragraph1.add(Chunk.SPACETABBING);
			paragraph1.add(numeroCuenta2);
			document.add(paragraph1);

			Paragraph paragraph2 = new Paragraph();
			paragraph2.setFont(font);
			Chunk agencia1 = new Chunk("AGENCIA:");
			Chunk agencia2 = new Chunk(agencia.getCodigo() + " - " + agencia.getDenominacion().toUpperCase());
			paragraph2.add(agencia1);
			paragraph2.add(Chunk.SPACETABBING);
			paragraph2.add(Chunk.SPACETABBING);
			paragraph2.add(agencia2);
			document.add(paragraph2);

			Moneda moneda = monedaServiceNT.findById(cuentaBancaria.getIdMoneda());
			BigDecimal saldo = cuentaBancaria.getSaldo();
			BigDecimal decimalValue = saldo.subtract(saldo.setScale(0, RoundingMode.FLOOR)).movePointRight(saldo.scale());
			Long integerValue = saldo.longValue();

			String decimalString = decimalValue.toString();
			if (decimalString.length() < 2)
				decimalString = "0" + decimalString;

			NumberFormat df1 = NumberFormat.getCurrencyInstance();
			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
			dfs.setCurrencySymbol("");
			dfs.setGroupingSeparator(',');
			dfs.setMonetaryDecimalSeparator('.');
			((DecimalFormat) df1).setDecimalFormatSymbols(dfs);

			Paragraph paragraph3 = new Paragraph();
			paragraph3.setFont(font);
			Chunk monto1 = new Chunk("MONTO:");
			Chunk monto2 = new Chunk(moneda.getSimbolo() + df1.format(saldo) + " - " + NumLetrasJ.Convierte(integerValue.toString() + "", Tipo.Pronombre).toUpperCase() + " Y " + decimalString + "/100 " + moneda.getDenominacion());
			paragraph3.add(monto1);
			paragraph3.add(Chunk.SPACETABBING);
			paragraph3.add(Chunk.SPACETABBING);
			paragraph3.add(monto2);
			document.add(paragraph3);

			Paragraph paragraph4 = new Paragraph();
			paragraph4.setFont(font);
			Chunk socio1 = new Chunk("SOCIO:");
			Chunk socio2 = new Chunk(cuentaBancaria.getSocio());
			Chunk codigoSocio1 = new Chunk("CODIGO:");
			Chunk codigoSocio2 = new Chunk(cuentaBancaria.getIdSocio().toString());
			paragraph4.add(socio1);
			paragraph4.add(Chunk.SPACETABBING);
			paragraph4.add(Chunk.SPACETABBING);
			paragraph4.add(socio2);
			paragraph4.add(Chunk.SPACETABBING);
			paragraph4.add(codigoSocio1);
			paragraph4.add(Chunk.SPACETABBING);
			paragraph4.add(codigoSocio2);
			document.add(paragraph4);

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String fechaAperturaString = df.format(cuentaBancaria.getFechaApertura());
			String fechaVencimientoString = df.format(cuentaBancaria.getFechaCierre());

			Paragraph paragraph6 = new Paragraph();
			paragraph6.setFont(font);
			Chunk fechaApertura1 = new Chunk("F. APERTURA:");
			Chunk fechaApertura2 = new Chunk(fechaAperturaString);
			Chunk fechaVencimiento1 = new Chunk("F. VENCIMIENTO:");
			Chunk fechaVencimiento2 = new Chunk(fechaVencimientoString);
			paragraph6.add(fechaApertura1);
			paragraph6.add(Chunk.SPACETABBING);
			paragraph6.add(fechaApertura2);
			paragraph6.add(Chunk.SPACETABBING);
			paragraph6.add(Chunk.SPACETABBING);
			paragraph6.add(Chunk.SPACETABBING);
			paragraph6.add(fechaVencimiento1);
			paragraph6.add(Chunk.SPACETABBING);
			paragraph6.add(fechaVencimiento2);
			document.add(paragraph6);

			Date fechaApertura = cuentaBancaria.getFechaApertura();
			Date fechaCierre = cuentaBancaria.getFechaCierre();
			LocalDate localDateApertura = new LocalDate(fechaApertura);
			LocalDate localDateCierre = new LocalDate(fechaCierre);
			Days days = Days.daysBetween(localDateApertura, localDateCierre);

			Paragraph paragraph5 = new Paragraph();
			paragraph5.setFont(font);
			Chunk tasa1 = new Chunk("TASA INTERES EFECTIVA:");
			Chunk tasa2 = new Chunk(cuentaBancaria.getTasaInteres().multiply(new BigDecimal(100)).toString());
			Chunk plazo1 = new Chunk("PLAZO:");
			Chunk plazo2 = new Chunk(days.getDays() + " DIAS");
			paragraph5.add(tasa1);
			paragraph5.add(Chunk.SPACETABBING);
			paragraph5.add(tasa2);
			paragraph5.add(Chunk.SPACETABBING);
			paragraph5.add(Chunk.SPACETABBING);
			paragraph5.add(plazo1);
			paragraph5.add(Chunk.SPACETABBING);
			paragraph5.add(plazo2);
			document.add(paragraph5);

			document.close();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		PdfReader reader;
		try {
			reader = new PdfReader(certificadoURL + "\\" + id + ".pdf");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PdfStamper pdfStamper = new PdfStamper(reader, out);
			AcroFields acroFields = pdfStamper.getAcroFields();
			acroFields.setField("field_title", "test");
			pdfStamper.close();
			reader.close();
			return Response.ok(out.toByteArray()).type("application/pdf").build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Jsend.getErrorJSend("No encontrado")).build();
	}

	@Override
	public Response getEstadoCuenta(BigInteger id, Long desde, Long hasta) {
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		List<EstadocuentaBancariaView> list = cuentaBancariaServiceNT.getEstadoCuenta(id, dateDesde, dateHasta);
		return Response.status(Response.Status.OK).entity(list).build();
	}

	@Override
	public Response congelar(BigInteger id) {
		Response response;
		try {
			cuentaBancariaServiceTS.congelarCuentaBancaria(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response descongelar(BigInteger id) {
		Response response;
		try {
			cuentaBancariaServiceTS.descongelarCuentaBancaria(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response recalcular(BigInteger id, CuentaBancariaDTO cuenta) {
		Response response;

		int periodo = cuenta.getPeriodo();
		BigDecimal tasaInteres = cuenta.getTasaInteres();
		try {
			cuentaBancariaServiceTS.recalcularCuentaPlazoFijo(id, periodo, tasaInteres);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response renovar(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response cancelarCuentaBancaria(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response create(CuentaBancariaDTO cuentaBancaria) {
		Response response;

		TipoCuentaBancaria tipoCuentaBancaria = cuentaBancaria.getTipoCuenta();
		BigInteger idMoneda = cuentaBancaria.getIdMoneda();
		BigDecimal tasaInteres = cuentaBancaria.getTasaInteres();
		TipoPersona tipoPersona = cuentaBancaria.getTipoPersona();
		int periodo = cuentaBancaria.getPeriodo();
		int cantRetirantes = cuentaBancaria.getCantRetirantes();
		List<BigInteger> titulares = cuentaBancaria.getTitulares();
		List<Beneficiario> beneficiarios = cuentaBancaria.getBeneficiarios();

		Agencia agencia = sessionServiceNT.getAgenciaOfSession();
		PersonaNatural persona = personaNaturalServiceNT.find(cuentaBancaria.getIdTipoDocumento(), cuentaBancaria.getNumeroDocumento());
		try {
			BigInteger idCuenta = cuentaBancariaServiceTS.create(tipoCuentaBancaria, agencia.getCodigo(), idMoneda, tasaInteres, tipoPersona, persona.getIdPersonaNatural(), periodo, cantRetirantes, titulares, beneficiarios);
			response = Response.status(Response.Status.CREATED).build();
			URI resource = new URI(baseUrl + "/" + idCuenta.toString());
			response = Response.created(resource).entity(Jsend.getSuccessJSend(idCuenta)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		} catch (URISyntaxException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response update(CuentaBancaria caja) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getTitulares(BigInteger id, Boolean estado) {
		if (estado == null)
			estado = false;
		Set<Titular> list = cuentaBancariaServiceNT.getTitulares(id, estado);
		Response response = Response.status(Response.Status.CREATED).entity(list).build();
		return response;
	}

	@Override
	public Response getTitular(BigInteger id, BigInteger idTitular) {
		Titular titular = cuentaBancariaServiceNT.findTitularById(idTitular);
		Response response = Response.status(Response.Status.OK).entity(titular).build();
		return response;
	}

	@Override
	public Response createTitular(BigInteger id, TitularDTO titular) {
		Response response;

		BigInteger idTipoDocumento = titular.getIdTipoDocumento();
		String numeroDocumento = titular.getNumeroDocumento();
		PersonaNatural personaNatural = personaNaturalServiceNT.find(idTipoDocumento, numeroDocumento);
		if (personaNatural != null) {
			try {
				BigInteger idTitular = cuentaBancariaServiceTS.addTitular(id, personaNatural.getIdPersonaNatural());
				response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idTitular)).build();
			} catch (RollbackFailureException e) {
				Jsend jsend = Jsend.getErrorJSend(e.getMessage());
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
			}
		} else {
			response = Response.status(Response.Status.BAD_REQUEST).entity(Jsend.getErrorJSend("Titular no encontrado")).build();
		}
		return response;
	}

	@Override
	public Response updateTitular(BigInteger id, BigInteger idTitular) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response deleteTitular(BigInteger id, BigInteger idTitular) {
		Response response;
		try {
			cuentaBancariaServiceTS.deleteTitular(idTitular);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getBeneficiarios(BigInteger id) {
		Set<Beneficiario> list = cuentaBancariaServiceNT.getBeneficiarios(id);
		Response response = Response.status(Response.Status.CREATED).entity(list).build();
		return response;
	}

	@Override
	public Response getBeneficiario(BigInteger id, BigInteger idBeneficiario) {
		Beneficiario beneficiario = cuentaBancariaServiceNT.findBeneficiarioById(idBeneficiario);
		Response response = Response.status(Response.Status.OK).entity(beneficiario).build();
		return response;
	}

	@Override
	public Response createBeneficiario(BigInteger id, Beneficiario beneficiario) {
		Response response;
		try {
			BigInteger idBeneficiario = cuentaBancariaServiceTS.addBeneficiario(id, beneficiario);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idBeneficiario)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response updateBeneficiario(BigInteger id, BigInteger idBeneficiario, Beneficiario beneficiario) {
		Response response;
		try {
			cuentaBancariaServiceTS.updateBeneficiario(idBeneficiario, beneficiario);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response deleteBeneficiario(BigInteger id, BigInteger idBeneficiario) {
		try {
			cuentaBancariaServiceTS.deleteBeneficiario(idBeneficiario);
			return Response.status(Status.OK).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}
