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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.poi.hssf.record.FnGroupCountRecord;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.Cheque;
import org.sistemafinanciero.entity.Chequera;
import org.sistemafinanciero.entity.CuentaBancaria;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PersonaJuridica;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.SocioView;
import org.sistemafinanciero.entity.Titular;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.mail.EmailSessionBean;
import org.sistemafinanciero.rest.CuentaBancariaREST;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.dto.CuentaBancariaDTO;
import org.sistemafinanciero.rest.dto.TitularDTO;
import org.sistemafinanciero.service.nt.AgenciaServiceNT;
import org.sistemafinanciero.service.nt.CuentaBancariaServiceNT;
import org.sistemafinanciero.service.nt.MonedaServiceNT;
import org.sistemafinanciero.service.nt.PersonaJuridicaServiceNT;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.nt.SessionServiceNT;
import org.sistemafinanciero.service.nt.SocioServiceNT;
import org.sistemafinanciero.service.ts.CuentaBancariaServiceTS;
import org.sistemafinanciero.util.NumLetrasJ;
import org.sistemafinanciero.util.NumLetrasJ.Tipo;
import org.sistemafinanciero.util.ProduceObject;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class CuentaBancariaRESTService implements CuentaBancariaREST {

	private final static String baseUrl = "/cuentasBancarias";

	// private final static String certificadoURL = "D:\\pdf";
	private final static String certificadoURL = "//usr//share//jboss//archivos//certificadoPlazoFijo//"; 

	// private final static String cartillaURL = "D:\\cartilla";
	private final static String cartillaURL = "//usr//share//jboss//archivos//cartillaInformacion//";

	@EJB
	EmailSessionBean emailSessionBean;
	
	@EJB
	private CuentaBancariaServiceNT cuentaBancariaServiceNT;

	@EJB
	private CuentaBancariaServiceTS cuentaBancariaServiceTS;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	@EJB
	private PersonaJuridicaServiceNT personaJuridicaServiceNT;

	@EJB
	private SocioServiceNT socioServiceNT;

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
			//Document document = new Document(PageSize.A5.rotate());
			Document document = new Document(PageSize.A4);
			PdfWriter writer = PdfWriter.getInstance(document, file);
			document.open();
			
			//recuperando moneda, redondeando y dando formato
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
			
			//recuperando el plazo en dias
			Date fechaApertura = cuentaBancaria.getFechaApertura();
			Date fechaCierre = cuentaBancaria.getFechaCierre();
			LocalDate localDateApertura = new LocalDate(fechaApertura);
			LocalDate localDateCierre = new LocalDate(fechaCierre);
			Days days = Days.daysBetween(localDateApertura, localDateCierre);
			
			//fuentes
			Font fontTitulo = FontFactory.getFont("Times New Roman", 14, Font.BOLD);
			Font fontSubTitulo = FontFactory.getFont("Times New Roman", 8);
			Font fontContenidoNegrita = FontFactory.getFont("Times New Roman", 10, Font.BOLD);
			Font fontContenidoNormal = FontFactory.getFont("Times New Roman", 10);
			
			
			//dando formato a las fechas
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String fechaAperturaString = df.format(cuentaBancaria.getFechaApertura());
			String fechaVencimientoString = df.format(cuentaBancaria.getFechaCierre());
			
			//ingresando datos al documento
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			
			//parrafo titulo
			Paragraph parrafoTitulo = new Paragraph();
			parrafoTitulo.setFont(fontTitulo);
			parrafoTitulo.setSpacingBefore(30);
			parrafoTitulo.setAlignment(Element.ALIGN_CENTER);
			
			//parrafo subtitulo
			Paragraph parrafoSubTitulo = new Paragraph();
			parrafoSubTitulo.setFont(fontSubTitulo);
			parrafoSubTitulo.setSpacingAfter(30);
			parrafoSubTitulo.setAlignment(Element.ALIGN_CENTER);

			//parrafo contenido
			Paragraph parrafoContenido = new Paragraph();
			parrafoContenido.setIndentationLeft(50);
			parrafoContenido.setAlignment(Element.ALIGN_LEFT);
			
			//parrafo firmas
			Paragraph parrafoFirmas = new Paragraph();
			parrafoFirmas.setAlignment(Element.ALIGN_CENTER);
			
			//agregar titulo al documento
			Chunk titulo = new Chunk("CERTIFICADO DE PLAZO FIJO");
			parrafoTitulo.add(titulo);
			
			//agregar titulo al documento
			Chunk subTitulo;
			if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ZERO) == 0) {
				subTitulo = new Chunk("DEPÓSITO A PLAZO FIJO - DOLARES AMERICANOS");
			} else if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ONE) == 0) {
				subTitulo = new Chunk("DEPÓSITO A PLAZO FIJO - NUEVOS SOLES");
			} else {
				subTitulo = new Chunk("DEPÓSITO A PLAZO FIJO - EUROS");
			}
			parrafoSubTitulo.add(subTitulo);
			
			//agregando contenido al documento
			//Agencia
			Chunk agencia1 = new Chunk("AGENCIA", fontContenidoNegrita);
			Chunk agencia2 = new Chunk(": " + agencia.getCodigo() + " - " + agencia.getDenominacion().toUpperCase(), fontContenidoNormal);
			parrafoContenido.add(agencia1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(agencia2);
			parrafoContenido.add("\n");
			
			//cuenta
			Chunk numeroCuenta1 = new Chunk("Nº CUENTA", fontContenidoNegrita);
			Chunk numeroCuenta2 = new Chunk(": " + cuentaBancaria.getNumeroCuenta(), fontContenidoNormal);
			parrafoContenido.add(numeroCuenta1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(numeroCuenta2);
			parrafoContenido.add("\n");
			
			//codigo cliente
			Chunk codigoSocio1 = new Chunk("CODIGO CLIENTE", fontContenidoNegrita);
			Chunk codigoSocio2 = new Chunk(": " + cuentaBancaria.getIdSocio().toString(), fontContenidoNormal);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(codigoSocio1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(codigoSocio2);
			parrafoContenido.add("\n");
			
			//cliente
			Chunk socio1 = new Chunk("CLIENTE", fontContenidoNegrita);
			Chunk socio2 = new Chunk(": " + cuentaBancaria.getSocio(), fontContenidoNormal);
			parrafoContenido.add(socio1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(socio2);
			parrafoContenido.add("\n");
			
			//tipo cuenta
			Chunk tipoCuenta1 = new Chunk("TIPO CUENTA", fontContenidoNegrita);
			Chunk tipoCuenta2 = new Chunk(": " + "INDIVIDUAL", fontContenidoNormal);
			parrafoContenido.add(tipoCuenta1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(tipoCuenta2);
			parrafoContenido.add("\n");
			
			//tipo moneda
			Chunk tipoMoneda1 = new Chunk("TIPO MONEDA", fontContenidoNegrita);
			Chunk tipoMoneda2;
			if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ZERO) == 0) {
				tipoMoneda2 = new Chunk(": " + "DOLARES AMERICANOS", fontContenidoNormal);
			} else if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ONE) == 0) {
				tipoMoneda2 = new Chunk(": " + "NUEVOS SOLES", fontContenidoNormal);
			} else {
				tipoMoneda2 = new Chunk(": " + "EUROS", fontContenidoNormal);
			}
			parrafoContenido.add(tipoMoneda1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(tipoMoneda2);
			parrafoContenido.add("\n");
			
			//Monto
			Chunk monto1 = new Chunk("MONTO", fontContenidoNegrita);
			Chunk monto2 = new Chunk(": " + moneda.getSimbolo() + df1.format(saldo) + " - " + NumLetrasJ.Convierte(integerValue.toString() + "", Tipo.Pronombre).toUpperCase() + " Y " + decimalString + "/100 " + moneda.getDenominacion(), fontContenidoNormal);
			parrafoContenido.add(monto1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(monto2);
			parrafoContenido.add("\n");

			//Plazo
			Chunk plazo1 = new Chunk("PLAZO", fontContenidoNegrita);
			Chunk plazo2 = new Chunk(": " + days.getDays() + " DÍAS", fontContenidoNormal);
			parrafoContenido.add(plazo1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(plazo2);
			parrafoContenido.add("\n");
			
			//Fecha Apertura
			Chunk fechaApertura1 = new Chunk("FEC. APERTURA", fontContenidoNegrita);
			Chunk fechaApertura2 = new Chunk(": " + fechaAperturaString, fontContenidoNormal);
			parrafoContenido.add(fechaApertura1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(fechaApertura2);
			parrafoContenido.add("\n");
			
			//Fecha Vencimiento
			Chunk fechaVencimiento1 = new Chunk("FEC. VENCIMIENTO", fontContenidoNegrita);
			Chunk fechaVencimiento2 = new Chunk(": " + fechaVencimientoString, fontContenidoNormal);
			parrafoContenido.add(fechaVencimiento1);
			parrafoContenido.add(Chunk.SPACETABBING);
			parrafoContenido.add(fechaVencimiento2);
			parrafoContenido.add("\n");
			
			//tasa efectiva anual
			Chunk tasaEfectivaAnual1 = new Chunk("TASA EFECTIVA ANUAL", fontContenidoNegrita);
			Chunk tasaEfectivaAnual2 = new Chunk(": " + cuentaBancaria.getTasaInteres().multiply(new BigDecimal(100)).toString() + "%", fontContenidoNormal);
			parrafoContenido.add(tasaEfectivaAnual1);
			parrafoContenido.add(tasaEfectivaAnual2);
			parrafoContenido.add("\n");
			
			//frecuencia de capitalizacion
			Chunk frecuenciaCapitalizacion1 = new Chunk("FREC. CAPITALIZACION", fontContenidoNegrita);
			Chunk frecuenciaCapitalizacion2 = new Chunk(": " + "DIARIA", fontContenidoNormal);
			parrafoContenido.add(frecuenciaCapitalizacion1);
			parrafoContenido.add(frecuenciaCapitalizacion2);
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			
			//importante
			Chunk importante = new Chunk("IMPORTANTE: ", fontContenidoNegrita);
			Chunk importanteDetalle1 = new Chunk("DEPÓSITO CUBIERTO POR EL FONDO DE SEGURO DE DEPOSITOS ESTABLECIDO POR EL BANCO CENTRAL DE RESERVA DEL PERÚ HASTA S/.82,073.00.", fontSubTitulo);
			Chunk importanteDetalle2 = new Chunk("LAS PERSONAS JURÍDICAS SIN FINES DE LUCRO SON CUBIERTAS POR EL FONDO DE SEGURO DE DEPÓSITOS.", fontSubTitulo);
			parrafoContenido.add(importante);
			parrafoContenido.add(importanteDetalle1);
			parrafoContenido.add("\n");
			parrafoContenido.add(importanteDetalle2);
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			
			//certificado intranferible
			Chunk certificadoIntransferible = new Chunk("CERTIFICADO INTRANSFERIBLE.",fontContenidoNegrita);
			parrafoContenido.add(certificadoIntransferible);
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			parrafoContenido.add("\n");
			
			//Firmas
			Chunk subGion = new Chunk("___________________",fontContenidoNormal);
			Chunk firmaCajero = new Chunk("CAJERO", fontContenidoNormal);
			Chunk firmaCliente = new Chunk("CLIENTE", fontContenidoNormal);
	
			parrafoFirmas.add(subGion);
			parrafoFirmas.add(Chunk.SPACETABBING);
			parrafoFirmas.add(Chunk.SPACETABBING);
			parrafoFirmas.add(subGion);
			parrafoFirmas.add("\n");
			parrafoFirmas.add(firmaCajero);
			parrafoFirmas.add(Chunk.SPACETABBING);
			parrafoFirmas.add(Chunk.SPACETABBING);
			parrafoFirmas.add(Chunk.SPACETABBING);
			parrafoFirmas.add(firmaCliente);

			//agregando los parrafos al documento
			document.add(parrafoTitulo);
			document.add(parrafoSubTitulo);
			document.add(parrafoContenido);
			document.add(parrafoFirmas);
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
	public Response getCartillaInformacion(BigInteger id) {
		OutputStream file;

		CuentaBancariaView cuentaBancaria = cuentaBancariaServiceNT.findById(id);
		SocioView socio = socioServiceNT.findById(cuentaBancaria.getIdSocio());
		Set<Titular> listTitulares = cuentaBancariaServiceNT.getTitulares(id, false);

		Moneda moneda = monedaServiceNT.findById(cuentaBancaria.getIdMoneda());

		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		BaseColor baseColor = BaseColor.LIGHT_GRAY;
		Font font = FontFactory.getFont("Arial", 10f);
		Font fontBold = FontFactory.getFont("Arial", 10f, Font.BOLD);
		try {
			file = new FileOutputStream(new File(cartillaURL + "\\" + id + ".pdf"));
			Document document = new Document(PageSize.A4);// *4
			PdfWriter writer = PdfWriter.getInstance(document, file);
			document.open();

			/******************* TITULO ******************/
			//Image img = Image.getInstance("/images/logo_coop_contrato.png");
			Image img = Image.getInstance("//usr//share//jboss//archivos//logoCartilla//logo_coop_contrato.png");
			img.setAlignment(Image.LEFT | Image.UNDERLYING);
			document.add(img);

			Paragraph parrafoPrincipal = new Paragraph();
			parrafoPrincipal.setSpacingAfter(30);
			parrafoPrincipal.setSpacingBefore(50);
			parrafoPrincipal.setAlignment(Element.ALIGN_CENTER);

			Chunk titulo = new Chunk("CARTILLA DE INFORMACIÓN\n");
			Font fuenteTitulo = new Font();
			fuenteTitulo.setSize(18);
			fuenteTitulo.setFamily("Arial");
			fuenteTitulo.setStyle(Font.BOLD);
			titulo.setFont(fuenteTitulo);
			parrafoPrincipal.add(titulo);

			if (cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.LIBRE)) {

				/******************* TIPO CUENTA Y TEXTO DE INTRODUCCION CUENTA LIBRE **********************/
				Chunk subTituloAhorro = new Chunk("APERTURA CUENTA LIBRE\n");
				Font fuenteSubtituloAhorro = new Font();
				fuenteSubtituloAhorro.setSize(13);
				fuenteSubtituloAhorro.setFamily("Arial");
				fuenteSubtituloAhorro.setStyle(Font.BOLD);
				subTituloAhorro.setFont(fuenteSubtituloAhorro);
				parrafoPrincipal.add(subTituloAhorro);

				document.add(parrafoPrincipal);

				Chunk mensajeIntroAhorro = new Chunk("La apertura de una cuenta libre generará intereses y demás beneficios complementarios de acuerdo al saldo promedio mensual o saldo diario establecido en la Cartilla de Información. Para estos efectos, se entiende por saldo promedio mensual, la suma del saldo diario dividida entre el número de días del mes. La cuenta libre podrá generar comisiones y gastos de acuerdo a las condiciones aceptadas en la Cartilla de Información.\n\n", font);
				mensajeIntroAhorro.setLineHeight(13);

				Paragraph parrafoIntroAhorro = new Paragraph();
				parrafoIntroAhorro.setLeading(11f);
				parrafoIntroAhorro.add(mensajeIntroAhorro);
				parrafoIntroAhorro.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(parrafoIntroAhorro);
			}

			if (cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.PLAZO_FIJO)) {

				/******************* TIPO CUENTA Y TEXTO DE INTRODUCCION CUENTA PLAZO FIJO **********************/
				Chunk subTituloPF = new Chunk("APERTURA CUENTA A PLAZO FIJO\n");
				Font fuenteSubtituloPF = new Font();
				fuenteSubtituloPF.setSize(13);
				fuenteSubtituloPF.setFamily("Arial");
				fuenteSubtituloPF.setStyle(Font.BOLD | Font.UNDERLINE);
				subTituloPF.setFont(fuenteSubtituloPF);
				parrafoPrincipal.add(subTituloPF);

				document.add(parrafoPrincipal);

				Chunk mensajeIntroPF = new Chunk("La presente cartilla de información forma parte integrante de las condiciones aplicables a los contratos de depósitos y servicios complementarios suscritos por las partes y tiene por finalidad establecer el detalle de las tareas de interés que se retribuirá al cliente.\n\n", font);
				mensajeIntroPF.setLineHeight(12);

				Paragraph parrafoIntroPF = new Paragraph();
				parrafoIntroPF.setLeading(11f);
				parrafoIntroPF.add(mensajeIntroPF);
				parrafoIntroPF.setAlignment(Element.ALIGN_JUSTIFIED);
				
				document.add(parrafoIntroPF);
			}

			if (cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.RECAUDADORA)) {

				/******************* TIPO CUENTA Y TEXTO DE INTRODUCCION CUENTA RECAUDADORA **********************/
				Chunk subTituloCC = new Chunk("APERTURA CUENTA RECAUDADORA\n");
				Font fuenteSubtituloCC = new Font();
				fuenteSubtituloCC.setSize(13);
				fuenteSubtituloCC.setFamily("Arial");
				fuenteSubtituloCC.setStyle(Font.BOLD | Font.UNDERLINE);
				subTituloCC.setFont(fuenteSubtituloCC);
				parrafoPrincipal.add(subTituloCC);

				document.add(parrafoPrincipal);

				Chunk mensajeIntroCC = new Chunk("El cliente podrá disponer del saldo de su cuenta en cualquier momento a través de ventanillas (en horarios de atención al público de las Agencias y Oficinas de la casa de Multivalores del Sur). Asimismo, podrá realizar abonos en su cuenta en efectivo, a través de cheques u órdenes de pago en cualquier momento a través de las ventanillas.\n\n", font);
				mensajeIntroCC.setLineHeight(12);

				Paragraph parrafoIntroCC = new Paragraph();
				parrafoIntroCC.setLeading(11f);
				parrafoIntroCC.add(mensajeIntroCC);
				parrafoIntroCC.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(parrafoIntroCC);
			}

			/******************* DATOS BASICOS DEL SOCIO **********************/
			PdfPTable table1 = new PdfPTable(4);
			table1.setWidthPercentage(100);

			PdfPCell cabecera1 = new PdfPCell(new Paragraph("DATOS BÁSICOS DEL CLIENTE", fontBold));
			cabecera1.setColspan(4);
			cabecera1.setBackgroundColor(baseColor);

			PdfPCell cellCodigoSocio = new PdfPCell(new Paragraph("Codigo Cliente:", fontBold));
			cellCodigoSocio.setColspan(1);
			cellCodigoSocio.setBorder(Rectangle.NO_BORDER);

			PdfPCell cellCodigoSocioValue = new PdfPCell(new Paragraph(socio.getIdsocio().toString(), font));
			cellCodigoSocioValue.setColspan(3);
			cellCodigoSocioValue.setBorder(Rectangle.NO_BORDER);

			PdfPCell cellApellidosNombres = new PdfPCell(new Paragraph(cuentaBancaria.getTipoPersona().equals(TipoPersona.NATURAL) ? "Apellidos y Nombres:" : "Razón Social:", fontBold));
			cellApellidosNombres.setColspan(1);
			cellApellidosNombres.setBorder(Rectangle.NO_BORDER);

			PdfPCell cellApellidosNombresValue = new PdfPCell(new Paragraph(cuentaBancaria.getSocio(), font));
			cellApellidosNombresValue.setColspan(3);
			cellApellidosNombresValue.setBorder(Rectangle.NO_BORDER);

			table1.addCell(cabecera1);
			table1.addCell(cellCodigoSocio);
			table1.addCell(cellCodigoSocioValue);
			table1.addCell(cellApellidosNombres);
			table1.addCell(cellApellidosNombresValue);

			PdfPCell cellDNI = new PdfPCell(new Paragraph(socio.getTipoDocumento(), fontBold));
			cellDNI.setBorder(Rectangle.NO_BORDER);
			PdfPCell cellDNIValue = new PdfPCell(new Paragraph(socio.getNumeroDocumento(), font));
			cellDNIValue.setBorder(Rectangle.NO_BORDER);
			PdfPCell cellFechaNaciemiento = new PdfPCell(new Paragraph(cuentaBancaria.getTipoPersona().equals(TipoPersona.NATURAL) ? "Fecha de Nacimiento:" : "Fecha de Constitución", fontBold));
			cellFechaNaciemiento.setBorder(Rectangle.NO_BORDER);
			PdfPCell cellFechaNacimientoValue = new PdfPCell(new Paragraph(DATE_FORMAT.format(socio.getFechaNacimiento()), font));
			cellFechaNacimientoValue.setBorder(Rectangle.NO_BORDER);

			table1.addCell(cellDNI);
			table1.addCell(cellDNIValue);
			table1.addCell(cellFechaNaciemiento);
			table1.addCell(cellFechaNacimientoValue);

			document.add(table1);
			document.add(new Paragraph("\n"));

			/******************* TITULARES **********************/
			PdfPTable table2 = new PdfPTable(7);
			table2.setWidthPercentage(100);

			PdfPCell cabecera2 = new PdfPCell(new Paragraph("TITULARES", fontBold));
			cabecera2.setColspan(7);
			cabecera2.setBackgroundColor(baseColor);
			table2.addCell(cabecera2);

			PdfPCell cellTipoDocumentoCab = new PdfPCell(new Paragraph("Tipo Doc.", fontBold));
			PdfPCell cellNumeroDocumentoCab = new PdfPCell(new Paragraph("Num. Doc.", fontBold));
			PdfPCell cellApellidoPaternoCab = new PdfPCell(new Paragraph("Ap. Paterno", fontBold));
			PdfPCell cellApellidoMaternoCab = new PdfPCell(new Paragraph("Ap. Materno", fontBold));
			PdfPCell cellNombresCab = new PdfPCell(new Paragraph("Nombres", fontBold));
			PdfPCell cellSexoCab = new PdfPCell(new Paragraph("Sexo", fontBold));
			PdfPCell cellFechaNacimientoCab = new PdfPCell(new Paragraph("Fec. Nac.", fontBold));

			cellTipoDocumentoCab.setBorder(Rectangle.NO_BORDER);
			cellNumeroDocumentoCab.setBorder(Rectangle.NO_BORDER);
			cellApellidoPaternoCab.setBorder(Rectangle.NO_BORDER);
			cellApellidoMaternoCab.setBorder(Rectangle.NO_BORDER);
			cellNombresCab.setBorder(Rectangle.NO_BORDER);
			cellSexoCab.setBorder(Rectangle.NO_BORDER);
			cellFechaNacimientoCab.setBorder(Rectangle.NO_BORDER);

			table2.addCell(cellTipoDocumentoCab);
			table2.addCell(cellNumeroDocumentoCab);
			table2.addCell(cellApellidoPaternoCab);
			table2.addCell(cellApellidoMaternoCab);
			table2.addCell(cellNombresCab);
			table2.addCell(cellSexoCab);
			table2.addCell(cellFechaNacimientoCab);

			for (Titular titular : listTitulares) {
				PersonaNatural personaNatural = titular.getPersonaNatural();

				PdfPCell cellTipoDocumento = new PdfPCell(new Paragraph(personaNatural.getTipoDocumento().getAbreviatura(), font));
				PdfPCell cellNumeroDocumento = new PdfPCell(new Paragraph(personaNatural.getNumeroDocumento(), font));
				PdfPCell cellApellidoPaterno = new PdfPCell(new Paragraph(personaNatural.getApellidoPaterno(), font));
				PdfPCell cellApellidoMaterno = new PdfPCell(new Paragraph(personaNatural.getApellidoMaterno(), font));
				PdfPCell cellNombres = new PdfPCell(new Paragraph(personaNatural.getNombres(), font));
				PdfPCell cellSexo = new PdfPCell(new Paragraph(personaNatural.getSexo().toString(), font));
				PdfPCell cellFechaNacimiento = new PdfPCell(new Paragraph(DATE_FORMAT.format(personaNatural.getFechaNacimiento()), font));

				cellTipoDocumento.setBorder(Rectangle.NO_BORDER);
				cellNumeroDocumento.setBorder(Rectangle.NO_BORDER);
				cellApellidoPaterno.setBorder(Rectangle.NO_BORDER);
				cellApellidoMaterno.setBorder(Rectangle.NO_BORDER);
				cellNombres.setBorder(Rectangle.NO_BORDER);
				cellSexo.setBorder(Rectangle.NO_BORDER);
				cellFechaNacimiento.setBorder(Rectangle.NO_BORDER);

				table2.addCell(cellTipoDocumento);
				table2.addCell(cellNumeroDocumento);
				table2.addCell(cellApellidoPaterno);
				table2.addCell(cellApellidoMaterno);
				table2.addCell(cellNombres);
				table2.addCell(cellSexo);
				table2.addCell(cellFechaNacimiento);
			}

			document.add(table2);

			if (cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.LIBRE) || cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.RECAUDADORA)) {
				Paragraph modalidadCuenta = new Paragraph();
				String value;
				Chunk modalidad = new Chunk("Modalidad de la Cuenta: ", fontBold);

				if (cuentaBancaria.getCantidadRetirantes() == 1)
					value = "INDIVIDUAL";
				else
					value = "MANCOMUNADA";

				Chunk modalidadValue = new Chunk(value + "\n\n", font);
				modalidadCuenta.add(modalidad);
				modalidadCuenta.add(modalidadValue);
				document.add(modalidadCuenta);
			}

			if (cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.PLAZO_FIJO)) {
				document.add(new Paragraph("\n"));
			}

			/******************* PRODUCTOS Y SERVICIOS **********************/
			PdfPTable table3 = new PdfPTable(4);
			table3.setWidthPercentage(100);

			PdfPCell cabecera3 = new PdfPCell(new Paragraph("PRODUCTOS Y SERVICIOS", fontBold));
			cabecera3.setColspan(4);
			cabecera3.setBackgroundColor(baseColor);
			table3.addCell(cabecera3);

			PdfPCell cellProductoCab = new PdfPCell(new Paragraph("Producto", fontBold));
			PdfPCell cellMonedaCab = new PdfPCell(new Paragraph("Moneda", fontBold));
			PdfPCell cellNumeroCuentaCab = new PdfPCell(new Paragraph("Número Cuenta", fontBold));
			PdfPCell cellFechaAperturaCab = new PdfPCell(new Paragraph("Fecha Apertura", fontBold));
			cellProductoCab.setBorder(Rectangle.NO_BORDER);
			cellMonedaCab.setBorder(Rectangle.NO_BORDER);
			cellNumeroCuentaCab.setBorder(Rectangle.NO_BORDER);
			cellFechaAperturaCab.setBorder(Rectangle.NO_BORDER);
			table3.addCell(cellProductoCab);
			table3.addCell(cellMonedaCab);
			table3.addCell(cellNumeroCuentaCab);
			table3.addCell(cellFechaAperturaCab);

			PdfPCell cellProducto = new PdfPCell(new Paragraph("CUENTA " + cuentaBancaria.getTipoCuenta().toString(), font));
			PdfPCell cellMoneda = new PdfPCell(new Paragraph(moneda.getDenominacion(), font));
			PdfPCell cellNumeroCuenta = new PdfPCell(new Paragraph(cuentaBancaria.getNumeroCuenta(), font));
			PdfPCell cellFechaApertura = new PdfPCell(new Paragraph(DATE_FORMAT.format(cuentaBancaria.getFechaApertura()), font));
			cellProducto.setBorder(Rectangle.NO_BORDER);
			cellMoneda.setBorder(Rectangle.NO_BORDER);
			cellNumeroCuenta.setBorder(Rectangle.NO_BORDER);
			cellFechaApertura.setBorder(Rectangle.NO_BORDER);
			table3.addCell(cellProducto);
			table3.addCell(cellMoneda);
			table3.addCell(cellNumeroCuenta);
			table3.addCell(cellFechaApertura);

			document.add(table3);
			document.add(new Paragraph("\n"));

			/******************* DECLARACIONES Y FIRMAS **********************/
			PdfPTable table4 = new PdfPTable(1);
			table4.setWidthPercentage(100);

			PdfPCell cabecera4 = new PdfPCell(new Paragraph("DECLARACIONES Y FIRMAS", fontBold));
			cabecera4.setBackgroundColor(baseColor);
			table4.addCell(cabecera4);
			
			Paragraph parrafoDeclaraciones = new Paragraph();
			
			// Declaraciones Finales
			Chunk parrafoDeclaracionesFinalesCab = new Chunk("\nDECLARACIONES FINALES: ", fontBold);
			Paragraph parrafoDeclaracionesFinalesValue = new Paragraph();

			Chunk declaracionFinal = new Chunk("EL CLIENTE, declara expresamente que previamente a la suscripción del presente Contrato y la “Cartilla de Información” que le ha sido entregada para su lectura, ha recibido toda la información necesaria acerca de las Condiciones Generales y Especiales aplicables al tipo de servicio contratado, tasas de interés, comisiones y gastos, habiéndosele absuelto todas sus consultas y/o dudas, por lo que declara tener pleno y exacto conocimiento de las condiciones de el(los) servicio(s) contratado(s).\n", font);
			declaracionFinal.setLineHeight(13);
			parrafoDeclaracionesFinalesValue.add(declaracionFinal);

			parrafoDeclaraciones.add(parrafoDeclaracionesFinalesCab);
			parrafoDeclaraciones.add(parrafoDeclaracionesFinalesValue);

			PdfPCell declaraciones = new PdfPCell(parrafoDeclaraciones);
			declaraciones.setBorder(Rectangle.NO_BORDER);
			declaraciones.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table4.addCell(declaraciones);

			document.add(table4);

			// firmas
			Chunk firmaP01 = new Chunk(".......................................");
			Chunk firmaP02 = new Chunk(".......................................\n");
			Chunk firma01 = new Chunk("Multivalores del Sur");
			Chunk firma02 = new Chunk("      El Cliente       ");

			Paragraph firmas = new Paragraph("\n\n\n\n\n\n\n\n");
			firmas.setAlignment(Element.ALIGN_CENTER);

			firmas.add(firmaP01);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(firmaP02);

			firmas.add(firma01);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(firma02);
			
			document.add(firmas);
			
			
			// Contrato de Cuentas bancarias
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
			String fechaAhora = df.format(cuentaBancaria.getFechaApertura());
			
			Font fontTituloContrato = FontFactory.getFont("Arial", 12f, Element.ALIGN_CENTER);
			Font fontSubtituloContrato = FontFactory.getFont("Arial", 10f, Font.BOLD);
			Font fontDescripcionContrato = FontFactory.getFont("Arial", 10f);
			
			Paragraph tituloContrato = new Paragraph("\n\n\n\n\n\n\n\n");
			Paragraph contenidoContrato = new Paragraph();
			Paragraph piePaginaContrato = new Paragraph();
			tituloContrato.setAlignment(Element.ALIGN_CENTER);
			tituloContrato.setSpacingAfter(10f);
			contenidoContrato.setAlignment(Element.ALIGN_JUSTIFIED);
			contenidoContrato.setLeading(11f);
			piePaginaContrato.setAlignment(Element.ALIGN_RIGHT);
			
			if (cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.LIBRE)) {
				//titulo y descripcion
				Chunk tituloContratoAhorro = new Chunk("CONTRATO DE CUENTA LIBRE Y SERVICIOS FINANCIEROS CONEXOS\n", fontTituloContrato);
				Chunk descripcionContratoAhorro = new Chunk("Conste por el presente documento el CONTRATO DE CUENTA LIBRE Y SERVICIOS FINANCIEROS CONEXOS, que celebran, de una parte “MULTIVALORES DEL SUR”, a quien en adelante se le denominará MULTIVALORES DEL SUR, y de la otra parte EL CLIENTE, cuyas generales de Ley y su firma puesta en señal de conformidad y aceptación de todos los términos del presente contrato, constan en el presente instrumento.\n"
														 + "Los términos y condiciones de este contrato que constan a continuación, serán de observancia y aplicación respecto de la/s cuenta/s de libres y servicios financieros conexos, así como las transacciones y operaciones sobre la cuenta libre que mantenga EL CLIENTE con MULTIVALORES DEL SUR (en conjunto los “Servicios Financieros”), en tanto no se contrapongan a otros de carácter específico contenidos y/o derivados de contratos y/o solicitudes suscritas y/o aceptadas bajo cualquier medio o mecanismo entre EL CLIENTE y MULTIVALORES DEL SUR.\n"
														 + "Ninguno de los términos de este contrato exonera a EL CLIENTE de cumplir los requisitos y formalidades que la ley y/o MULTIVALORES DEL SUR exijan para la prestación y/o realización de determinados servicios, y/o productos y/u operaciones y/o transacciones.\n\n", fontDescripcionContrato);
				
				//primer subtitulo y descripcion
				Chunk subtitulo1ContratoAhorro = new Chunk("OPERACIONES Y SERVICIOS FINANCIEROS EN GENERAL\n", fontSubtituloContrato);
				Chunk contenido1ContratoAhorro = new Chunk("1. El presente contrato, concede a EL CLIENTE, el derecho de usar los productos y servicios de MULTIVALORES DEL SUR, que integran sus canales terminales de depósitos y/o retiros y Consulta, banca telefónica, banca Internet y aquellos otros que MULTIVALORES DEL SUR pudiera poner a disposición de EL CLIENTE cualquier canal de distribución que estime pertinente, tales como página Web, e-mail, mensaje de texto, mensajes, entre otros.\n"
														 + "EL CLIENTE declara que MULTIVALORES DEL SUR ha cumplido con las disposiciones legales sobre transparencia en la información y en ese sentido le ha brindado en forma previa toda la información relevante.\n"
														 + "2. Las partes acuerdan que la(s) cuenta(s) y/o el(los) depósito(s) que EL CLIENTE tuviese abiertos o constituidos y/o que abra o constituya en el futuro, podrán ser objeto de afiliaciones a prestaciones adicionales o de ampliaciones de los servicios que ofrece MULTIVALORES DEL SUR.\n\n", fontDescripcionContrato);
				
				//segundo subtitulo y descripcion
				Chunk subtitulo2ContratoAhorro = new Chunk("DISPOSICIONES GENERALES\n",fontSubtituloContrato);
				Chunk contenido2ContratoAhorro = new Chunk("3. Queda acordado por las partes que, como consecuencia de variaciones en las condiciones de mercado, cambios en las estructuras de costos, decisiones comerciales internas, MULTIVALORES DEL SUR podrá modificar las tasas de interés, que son fijas, comisiones y gastos aplicables a los Servicios Financieros, y en general, cualquiera de las condiciones aquí establecidas, debiendo comunicar la modificación a EL CLIENTE con una anticipación no menor a cuarenta y cinco (45) días calendarios a la fecha o momento a partir de la cual entrará en vigencia la respectiva modificación.\n"
														 + "De no estar EL CLIENTE conforme con las modificaciones comunicadas tendrá la facultad de dar por concluido el presente contrato de pleno derecho, sin penalización alguna cursando una comunicación escrita a MULTIVALORES DEL SUR.\n"
														 + "4. En caso que EL CLIENTE sea persona jurídica o persona natural representada por apoderados o representantes legales debidamente autorizados y registrados en MULTIVALORES DEL SUR, este último no asumirá responsabilidad alguna por las consecuencias de las operaciones que los citados representantes o apoderados hubieren efectuado en su representación, aún cuando sus poderes hubieren sido revocados o modificados, salvo que tales revocaciones o modificaciones hubieren sido puestas en conocimiento de MULTIVALORES DEL SUR por escrito y adjuntando los instrumentos pertinentes.\n"
														 + "5. MULTIVALORES DEL SUR no asume responsabilidad alguna si por caso fortuito o de fuerza mayor no pudiera cumplir con cualquiera de las obligaciones materia del presente contrato y/o con las instrucciones de EL CLIENTE que tengan relación con los Servicios Financieros, materia del presente contrato. En tales casos MULTIVALORES DEL SUR, sin responsabilidad alguna para sí, dará cumplimiento a la obligación y/o instrucción tan pronto desaparezca la causa que impidiera su atención oportuna.\n"
														 + "Se consideran como causas de fuerza mayor o caso fortuito, sin que la enumeración sea limitativa, las siguientes: a) Interrupción del sistema de cómputo, red de teleproceso local o de telecomunicaciones; b) Falta de fluido eléctrico; c) Terremotos, incendios, inundaciones y otros similares; d) Actos y consecuencias de vandalismo, terrorismo y conmoción civil; e) Huelgas y paros; f) Actos y consecuencias imprevisibles debidamente justificadas por MULTIVALORES DEL SUR; g) Suministros y abastecimientos a sistemas y canales de distribución de productos y servicios.\n\n", fontDescripcionContrato);
				
				//tercer subtitulo y descripcion
				Chunk subtitulo3ContratoAhorro = new Chunk("CONDICIONES ESPECIALES APLICABLES A LAS CUENTAS LIBRES\n", fontSubtituloContrato);
				Chunk contenido3ContratoAhorro = new Chunk("6. Las cuentas de depósito de cuentas libres están sujetas a las disposiciones contenidas en el Art. 229 de la Ley 26702. MULTIVALORES DEL SUR entrega al titular de la cuenta su correspondiente comprobante de apertura. Toda cantidad que se abone y/o retire de la cuenta de depósito de cuentas libres constará en hojas sueltas o soportes mecánicos y/o informáticos que se entregue a EL CLIENTE.\n\n", fontDescripcionContrato);
				
				//pie pagina contrato
				Chunk piePagina = new Chunk("Ayacucho" + ", " + fechaAhora, fontDescripcionContrato);
				
				tituloContrato.add(tituloContratoAhorro);
				contenidoContrato.add(descripcionContratoAhorro);
				contenidoContrato.add(subtitulo1ContratoAhorro);
				contenidoContrato.add(contenido1ContratoAhorro);
				contenidoContrato.add(subtitulo2ContratoAhorro);
				contenidoContrato.add(contenido2ContratoAhorro);
				contenidoContrato.add(subtitulo3ContratoAhorro);
				contenidoContrato.add(contenido3ContratoAhorro);
				piePaginaContrato.add(piePagina);
				
				document.add(tituloContrato);
				document.add(contenidoContrato);
				document.add(piePaginaContrato);
			}

			if (cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.PLAZO_FIJO)) {
				//titulo y descripcion
				Chunk tituloContratoPF = new Chunk("CONTRATO DE DEPOSITO A PLAZO FIJO\n", fontTituloContrato);
				Chunk descripcionContratoPF = new Chunk("El presente contrato regula las Condiciones que “MULTIVALORES DEL SUR”, en adelante MULTIVALORES DEL SUR, establece para el servicio de DEPOSITO A PLAZO FIJO que brindará a favor de EL CLIENTE, cuyas generales de ley y domicilio constan al final del presente documento.\n\n", fontDescripcionContrato);
				
				//primer subtitulo y descripcion
				Chunk subtitulo1ContratoPF = new Chunk("CONDICIONES GENERALES\n\n", fontSubtituloContrato);
				Chunk contenido1ContratoPF = new Chunk("1. MULTIVALORES DEL SUR abre la cuenta de depósito a plazo fijo a solicitud de EL CLIENTE, con la información necesaria proporcionada por EL CLIENTE para su identificación y manejo, la misma que tiene el carácter de declaración jurada y que se obliga actualizar cuando exista algún cambio.\n"
													 + "MULTIVALORES DEL SUR, se reserva el derecho de aceptar o denegar la solicitud de apertura de la(s) cuenta(s), así como verificar la información proporcionada. Tratándose de personas naturales, la(s) cuenta(s) de depósito podrá(n) ser: i) Individuales; ii) Mancomunadas o conjuntas,  a elección y libre decisión de EL CLIENTE. Las personas naturales, se identificarán y presentarán copia de su documento oficial de identidad y las Personas Jurídicas presentarán copia del Registro Único del Contribuyente, del documento de su constitución y la acreditación de sus representantes legales o apoderados con facultades suficientes para abrir y operar cuentas bancarias.\n"
													 + "2. La(s) cuenta(s), sin excepción alguna, que mantenga EL CLIENTE en MULTIVALORES DEL SUR, deberá(n) ser manejada(s) personalmente por él o por sus representantes legales acreditados ante MULTIVALORES DEL SUR. Los menores de edad, los analfabetos, los que adolezcan de incapacidad relativa o absoluta, serán representados por personas autorizadas legalmente. La(s) cuenta(s) y todas las operaciones que sobre ésta(s) realice(n) directamente EL CLIENTE o a través de sus representantes a través de los medios proporcionados por MULTIVALORES DEL SUR se considerarán hechas por EL CLIENTE, bajo su responsabilidad. Será obligación de EL CLIENTE comunicar a MULTIVALORES DEL SUR de cualquier designación, modificación o revocación de poderes otorgados a sus representantes, acreditándose en su caso con los instrumentos públicos o privados que se requieran conforme a las normas sobre la materia. Tratándose de cuentas mancomunadas conjuntas el retiro procederá en ventanilla con los titulares acreditados para tal fin.\n"
													 + "3. Las partes acuerdan que MULTIVALORES DEL SUR pagará a EL CLIENTE una tasa de interés compensatoria efectiva anual que tenga vigente en su tarifario, para este tipo de operaciones pasivas, por el tiempo efectivo de permanencia de su(s) depósito(s), según el periodo de capitalización pactado. En los casos en que MULTIVALORES DEL SUR considere el cobro de comisiones por los servicios prestados a EL CLIENTE, así como los gastos que haya tenido que asumir directamente con terceros derivados de la(s) operaciones solicitado(s) por EL CLIENTE.\n"
													 + "4. El(los) depósito(s) podrá(n) efectuarse en dinero en efectivo.\n"
													 + "5. MULTIVALORES DEL SUR queda facultada por EL CLIENTE respecto a su(s) cuenta(s) de depósito a plazo fijo que mantenga para cargar el costo de comisiones, seguros, tributos y otros gastos.\n"
													 + "6. Los fondos existentes en todos los depósitos que EL CLIENTE pudiera mantener en MULTIVALORES DEL SUR, podrán ser afectados para respaldar las obligaciones directas o indirectas, existentes o futuras, en cualquier moneda que EL CLIENTE adeude o que expresamente haya garantizado frente a MULTIVALORES DEL SUR, por capital, intereses, comisiones, tributos, gastos, u otro concepto, quedando MULTIVALORES DEL SUR facultada a aplicarlos de manera parcial o total para la amortización y/o cancelación de dichas obligaciones, para tal efecto MULTIVALORES DEL SUR podrá en cualquier momento, y a su sólo criterio, realizar la consolidación y/o la compensación entre los saldos deudores y acreedores que EL CLIENTE pudiera tener en los depósitos que mantenga abiertos en MULTIVALORES DEL SUR, conforme a la Ley General del Sistema Financiero.\n"
													 + "7. Realizar todas las operaciones de cambio de moneda que sean necesarias, y al tipo de cambio vigente en el día en que se realiza la operación.\n"
													 + "8. MULTIVALORES DEL SUR podrá cerrar o cancelar la(s) cuenta(s) de depósito: a) A solicitud de EL CLIENTE y previo pago de todo saldo deudor u obligación que pudiera mantener pendiente; b) Cuando sea informada por escrito o tome conocimiento del fallecimiento o liquidación del patrimonio del titular.\n\n\n\n\n\n", fontDescripcionContrato);
				
				//pie pagina contrato
				Chunk piePagina = new Chunk("Ayacucho" + ", " + fechaAhora, fontDescripcionContrato);
				
				tituloContrato.add(tituloContratoPF);
				contenidoContrato.add(descripcionContratoPF);
				contenidoContrato.add(subtitulo1ContratoPF);
				contenidoContrato.add(contenido1ContratoPF);
				piePaginaContrato.add(piePagina);
				
				document.add(tituloContrato);
				document.add(contenidoContrato);
				document.add(piePaginaContrato);
			}
			
			if (cuentaBancaria.getTipoCuenta().equals(TipoCuentaBancaria.RECAUDADORA)) {
				//titulo y descripcion
				Chunk tituloContratoCC = new Chunk("CONTRATO DE CUENTA RECAUDADORA Y SERVICIOS FINANCIEROS CONEXOS\n", fontTituloContrato);
				Chunk descripcionContratoCC = new Chunk("Conste por el presente documento el CONTRATO DE CUENTA RECAUDADORA Y SERVICIOS FINANCIEROS CONEXOS, que celebran, de una parte “MULTIVALORES DEL SUR”, a quien en adelante se le denominará MULTIVALORES DEL SUR, y de la otra parte EL CLIENTE, cuyas generales de Ley y su firma puesta en señal de conformidad y aceptación de todos los términos del presente contrato, constan en el presente instrumento.\n"
														 + "Los términos y condiciones de este contrato que constan a continuación, serán de observancia y aplicación respecto de la cuenta recaudadora y servicios financieros conexos, así como las transacciones y operaciones sobre las cuentas recaudadoras que mantenga EL CLIENTE con MULTIVALORES DEL SUR (en conjunto los “Servicios Financieros”), en tanto no se contrapongan a otros de carácter específico contenidos y/o derivados de contratos y/o solicitudes suscritas y/o aceptadas bajo cualquier medio o mecanismo entre EL CLIENTE y MULTIVALORES DEL SUR.\n"
														 + "Ninguno de los términos de este contrato exonera a EL CLIENTE de cumplir los requisitos y formalidades que la ley y/o MULTIVALORES DEL SUR exijan para la prestación y/o realización de determinados servicios, y/o productos y/u operaciones y/o transacciones.\n\n", fontDescripcionContrato);
				
				//primer subtitulo y descripcion
				Chunk subtitulo1ContratoCC = new Chunk("OPERACIONES Y SERVICIOS FINANCIEROS  EN GENERAL\n", fontSubtituloContrato);
				Chunk contenido1ContratoCC = new Chunk("1. El presente contrato, concede a EL CLIENTE, el derecho de usar los productos y servicios de MULTIVALORES DEL SUR, que integran sus canales terminales de depósitos y/o retiros y Consulta, banca telefónica, banca Internet y aquellos otros que MULTIVALORES DEL SUR pudiera poner a disposición de EL CLIENTE cualquier canal de distribución que estime pertinente, tales como página Web, e-mail, mensaje de texto, mensajes, entre otros.\n"
													 + "EL CLIENTE declara que MULTIVALORES DEL SUR ha cumplido con las disposiciones legales sobre transparencia en la información y en ese sentido le ha brindado en forma previa toda la información relevante.\n"
													 + "2. Las partes acuerdan que la(s) cuenta(s) y/o el(los) depósito(s) que EL CLIENTE tuviese abiertos o constituidos y/o que abra o constituya en el futuro, podrán ser objeto de afiliaciones a prestaciones adicionales o de ampliaciones de los servicios que ofrece MULTIVALORES DEL SUR.\n\n", fontDescripcionContrato);
				
				//segundo subtitulo y descripcion
				Chunk subtitulo2ContratoCC = new Chunk("DISPOSICIONES GENERALES\n",fontSubtituloContrato);
				Chunk contenido2ContratoCC = new Chunk("3. Queda acordado por las partes que, como consecuencia de variaciones en las condiciones de mercado, cambios en las estructuras de costos, decisiones comerciales internas, MULTIVALORES DEL SUR podrá modificar las tasas de interés, que son fijas, comisiones y gastos aplicables a los Servicios Financieros, y en general, cualquiera de las condiciones aquí establecidas, debiendo comunicar la modificación a EL CLIENTE con una anticipación no menor a cuarenta y cinco (45) días calendarios a la fecha o momento a partir de la cual entrará en vigencia la respectiva modificación.\n"
													 + "De no estar EL CLIENTE conforme con las modificaciones comunicadas tendrá la facultad de dar por concluido el presente contrato de pleno derecho, sin penalización alguna cursando una comunicación escrita a MULTIVALORES DEL SUR.\n"
													 + "4. En caso que EL CLIENTE sea persona jurídica o persona natural representada por apoderados o representantes legales debidamente autorizados y registrados en MULTIVALORES DEL SUR, este último no asumirá responsabilidad alguna por las consecuencias de las operaciones que los citados representantes o apoderados hubieren efectuado en su representación, aun cuando sus poderes hubieren sido revocados o modificados, salvo que tales revocaciones o modificaciones hubieren sido puestas en conocimiento de MULTIVALORES DEL SUR por escrito y adjuntando los instrumentos pertinentes.\n"
													 + "5. MULTIVALORES DEL SUR no asume responsabilidad alguna si por caso fortuito o de fuerza mayor no pudiera cumplir con cualquiera de las obligaciones materia del presente contrato y/o con las instrucciones de EL CLIENTE que tengan relación con los Servicios Financieros, materia del presente contrato. En tales casos MULTIVALORES DEL SUR, sin responsabilidad alguna para sí, dará cumplimiento a la obligación y/o instrucción tan pronto desaparezca la causa que impidiera su atención oportuna.\n"
													 + "Se consideran como causas de fuerza mayor o caso fortuito, sin que la enumeración sea limitativa, las siguientes: a) Interrupción del sistema de cómputo, red de teleproceso local o de telecomunicaciones; b) Falta de fluido eléctrico; c) Terremotos, incendios, inundaciones y otros similares; d) Actos y consecuencias de vandalismo, terrorismo y conmoción civil; e) Huelgas y paros; f) Actos y consecuencias imprevisibles debidamente justificadas por MULTIVALORES DEL SUR; g) Suministros y abastecimientos a sistemas y canales de distribución de productos y servicios.\n\n", fontDescripcionContrato);
				
				//tercer subtitulo y descripcion
				Chunk subtitulo3ContratoCC = new Chunk("CONDICIONES ESPECIALES APLICABLES A LAS CUENTAS RECAUDADORAS\n", fontSubtituloContrato);
				Chunk contenido3ContratoCC = new Chunk("6. Las cuentas de depósito, están sujetas a las disposiciones contenidas en el Art. 229 de la Ley 26702. MULTIVALORES DEL SUR entrega al titular de la cuenta su correspondiente comprobante de apertura. Toda cantidad que se abone y/o retire de la cuenta libre constará en hojas sueltas o soportes mecánicos y/o informáticos que se entregue a EL CLIENTE.\n\n\n\n", fontDescripcionContrato);
				
				//pie pagina contrato
				Chunk piePagina = new Chunk("Ayacucho" + ", " + fechaAhora, fontDescripcionContrato);
				
				tituloContrato.add(tituloContratoCC);
				contenidoContrato.add(descripcionContratoCC);
				contenidoContrato.add(subtitulo1ContratoCC);
				contenidoContrato.add(contenido1ContratoCC);
				contenidoContrato.add(subtitulo2ContratoCC);
				contenidoContrato.add(contenido2ContratoCC);
				contenidoContrato.add(subtitulo3ContratoCC);
				contenidoContrato.add(contenido3ContratoCC);
				piePaginaContrato.add(piePagina);
				
				document.add(tituloContrato);
				document.add(contenidoContrato);
				document.add(piePaginaContrato);
			}
			
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
			reader = new PdfReader(cartillaURL + "\\" + id + ".pdf");
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
	public Response getEstadoCuenta(BigInteger id, Long desde, Long hasta, Boolean estado) {
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		List<EstadocuentaBancariaView> list = cuentaBancariaServiceNT.getEstadoCuenta(id, dateDesde, dateHasta, estado);
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
		try {
			BigInteger idCuenta = null;
			if (tipoPersona.equals(TipoPersona.NATURAL)) {
				PersonaNatural persona = personaNaturalServiceNT.find(cuentaBancaria.getIdTipoDocumento(), cuentaBancaria.getNumeroDocumento());
				idCuenta = cuentaBancariaServiceTS.create(tipoCuentaBancaria, agencia.getCodigo(), idMoneda, tasaInteres, tipoPersona, persona.getIdPersonaNatural(), periodo, cantRetirantes, titulares, beneficiarios);
			} else if (tipoPersona.equals(TipoPersona.JURIDICA)) {
				PersonaJuridica persona = personaJuridicaServiceNT.find(cuentaBancaria.getIdTipoDocumento(), cuentaBancaria.getNumeroDocumento());
				idCuenta = cuentaBancariaServiceTS.create(tipoCuentaBancaria, agencia.getCodigo(), idMoneda, tasaInteres, tipoPersona, persona.getIdPersonaJuridica(), periodo, cantRetirantes, titulares, beneficiarios);
			}
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

	@Override
	public Response getChequera(BigInteger idCuentaBancaria, BigInteger idChequera) {
		Chequera chequera = cuentaBancariaServiceNT.getChequera(idChequera);
		if (chequera != null) {
			return Response.status(Status.OK).entity(chequera).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}
	}

	@Override
	public Response getUltimaChequera(BigInteger idCuentaBancaria) {
		Chequera chequera = cuentaBancariaServiceNT.getChequeraUltima(idCuentaBancaria);
		if (chequera != null) {
			return Response.status(Status.OK).entity(chequera).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}
	}

	@Override
	public Response getChequeras(BigInteger idCuentaBancaria) {
		List<Chequera> list = cuentaBancariaServiceNT.getChequeras(idCuentaBancaria);
		return Response.status(Status.OK).entity(list).build();
	}

	@Override
	public Response createChequera(BigInteger idCuentaBancaria, Chequera chequera) {
		Response response;
		try {
			BigInteger idChequera = cuentaBancariaServiceTS.crearChequera(idCuentaBancaria, chequera.getCantidad());
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idChequera)).build();
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
	public Response desactivarChequera(BigInteger idCuentaBancaria, BigInteger idChequera) {
		Response response;
		try {
			cuentaBancariaServiceTS.desactivarChequera(idChequera);
			response = Response.ok().build();
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
	public Response desactivarCheque(BigInteger idCuentaBancaria, BigInteger numeroChequeUnico) {
		Response response;
		try {
			cuentaBancariaServiceTS.desactivarCheque(numeroChequeUnico);
			response = Response.ok().build();
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
	public Response congelarCheque(BigInteger idCuentaBancaria, BigInteger numeroChequeUnico) {
		Response response;
		try {
			cuentaBancariaServiceTS.congelarCheque(numeroChequeUnico);
			response = Response.ok().build();
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
	public Response descongelarCheque(BigInteger idCuentaBancaria, BigInteger numeroChequeUnico) {
		Response response;
		try {
			cuentaBancariaServiceTS.descongelarCheque(numeroChequeUnico);
			response = Response.ok().build();
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
	public Response getChequesOfChequera(BigInteger idCuentaBancaria, BigInteger idChequera) {
		Chequera chequera = cuentaBancariaServiceNT.getChequera(idChequera);
		if (chequera != null) {
			List<Cheque> list = cuentaBancariaServiceNT.getCheques(chequera.getIdChequera());
			return Response.status(Status.OK).entity(list).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}		
	}

	@Override
	public Response getCheque(BigInteger idCuentaBancaria, BigInteger idChequera, BigInteger idCheque) {
		Cheque cheque = cuentaBancariaServiceNT.getCheque(idCheque);
		if (cheque != null) {			
			return Response.status(Status.OK).entity(cheque).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}	
	}

	@Override
	public Response getChequeByNumeroUnico(BigInteger numeroChequeUnico) {
		Cheque cheque = cuentaBancariaServiceNT.getChequeByNumeroUnico(numeroChequeUnico);
		if (cheque != null) {			
			return Response.status(Status.OK).entity(cheque).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}	
	}

	@Override
	public Response getChequeByNumeroUnicoCuentaBancaria(BigInteger numeroChequeUnico) {
		CuentaBancariaView cuentaBancariaView = cuentaBancariaServiceNT.findByNumeroCheque(numeroChequeUnico);
		if (cuentaBancariaView != null) {			
			return Response.status(Status.OK).entity(cuentaBancariaView).build();
		} else {
			return Response.status(Status.NO_CONTENT).build();
		}
	}	

	@Override
	public Response enviarEstadoCuentaPdf(BigInteger idCuentaBancaria, Long desde, Long hasta) {
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		
		Set<Titular> titulares = cuentaBancariaServiceNT.getTitulares(idCuentaBancaria, true);
		List<String> emails = new ArrayList<String>();
		for (Titular titular : titulares) {
			PersonaNatural personaNatural = titular.getPersonaNatural();
			String email = personaNatural.getEmail();
			if(email != null)
				emails.add(email);			
		}
		
		CuentaBancariaView cuentaBancariaView = cuentaBancariaServiceNT.findById(idCuentaBancaria);
		List<EstadocuentaBancariaView> list = cuentaBancariaServiceNT.getEstadoCuenta(idCuentaBancaria, dateDesde, dateHasta, true);
		
		emailSessionBean.sendMailPdf(cuentaBancariaView, list, emails, dateDesde, dateHasta);
		
		return Response.status(Status.NO_CONTENT).build();
	}

	@Override
	public Response enviarEstadoCuentaExcel(BigInteger idCuentaBancaria, Long desde, Long hasta) {
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		
		Set<Titular> titulares = cuentaBancariaServiceNT.getTitulares(idCuentaBancaria, true);
		List<String> emails = new ArrayList<String>();
		for (Titular titular : titulares) {
			PersonaNatural personaNatural = titular.getPersonaNatural();
			String email = personaNatural.getEmail();
			emails.add(email);			
		}
		
		CuentaBancariaView cuentaBancariaView = cuentaBancariaServiceNT.findById(idCuentaBancaria);
		List<EstadocuentaBancariaView> list = cuentaBancariaServiceNT.getEstadoCuenta(idCuentaBancaria, dateDesde, dateHasta, true);
		
		emailSessionBean.sendMailExcel(cuentaBancariaView, list, emails, dateDesde, dateHasta);
		
		return Response.status(Status.NO_CONTENT).build();
	}
	
	@Override
	public Response getEstadoCuentaPdf(BigInteger idCuentaBancaria, Long desde, Long hasta) { 									
		Date dateDesde = (desde != null ? new Date(desde) : null);
		Date dateHasta = (desde != null ? new Date(hasta) : null);
		
		//dando formato a las fechas
		SimpleDateFormat fechaformato = new SimpleDateFormat("dd/MM/yyyy");
		String fechaDesde = fechaformato.format(dateDesde);
		String fechaHasta = fechaformato.format(dateHasta);
		
		Set<Titular> titulares = cuentaBancariaServiceNT.getTitulares(idCuentaBancaria, true);
		List<String> emails = new ArrayList<String>();
		for (Titular titular : titulares) {
			PersonaNatural personaNatural = titular.getPersonaNatural();
			String email = personaNatural.getEmail();
			if(email != null)
				emails.add(email);			
		}	
		CuentaBancariaView cuentaBancariaView = cuentaBancariaServiceNT.findById(idCuentaBancaria);
		List<EstadocuentaBancariaView> list = cuentaBancariaServiceNT.getEstadoCuenta(idCuentaBancaria, dateDesde, dateHasta, true);
		
		/**obteniendo la moneda y dando formato**/
		Moneda moneda = monedaServiceNT.findById(cuentaBancariaView.getIdMoneda());
		NumberFormat df1 = NumberFormat.getCurrencyInstance();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setCurrencySymbol("");
		dfs.setGroupingSeparator(',');
		dfs.setMonetaryDecimalSeparator('.');
		((DecimalFormat) df1).setDecimalFormatSymbols(dfs);
		
		
		/**PDF**/
		ByteArrayOutputStream outputStream = null;
		outputStream = new ByteArrayOutputStream();
		
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, outputStream);
			document.open();

			document.addTitle("Estado de Cuenta");
			document.addSubject("Estado de Cuenta");
			document.addKeywords("email");
			document.addAuthor("MULTISERVICIOS DEL SUR");
			document.addCreator("MULTISERVICIOS DEL SUR");

			Paragraph saltoDeLinea = new Paragraph();
			document.add(saltoDeLinea);
		} catch (DocumentException e1) {			
			e1.printStackTrace();
		}
		
		/******************* TITULO ******************/
		try {
			//Image img = Image.getInstance("/images/logo_coop_contrato.png");
			Image img = Image.getInstance("//usr//share//jboss//archivos//logoCartilla//logo_coop_contrato.png");
			img.setAlignment(Image.LEFT | Image.UNDERLYING);
			document.add(img);

			Paragraph parrafoPrincipal = new Paragraph();
			parrafoPrincipal.setSpacingAfter(30);
			//parrafoPrincipal.setSpacingBefore(50);
			parrafoPrincipal.setAlignment(Element.ALIGN_CENTER);
			parrafoPrincipal.setIndentationLeft(100);
			parrafoPrincipal.setIndentationRight(50);
			
			Paragraph parrafoSecundario = new Paragraph();
			parrafoSecundario.setSpacingAfter(20);
			parrafoSecundario.setSpacingBefore(-20);
			parrafoSecundario.setAlignment(Element.ALIGN_LEFT);
			parrafoSecundario.setIndentationLeft(160);
			parrafoSecundario.setIndentationRight(10);

			Chunk titulo = new Chunk("ESTADO DE CUENTA");
			Font fuenteTitulo = new Font(FontFamily.UNDEFINED, 13, Font.BOLD);
			titulo.setFont(fuenteTitulo);
			parrafoPrincipal.add(titulo);
			
			Font fuenteDatosCliente = new Font(FontFamily.UNDEFINED, 10);
			Date fechaSistema = new Date();
			SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String fechaActual = formatFecha.format(fechaSistema);
			
			if (cuentaBancariaView.getTipoPersona() == TipoPersona.NATURAL) {
				Chunk clientePNNombres = new Chunk("CLIENTE       : " + cuentaBancariaView.getSocio() + "\n");
				Chunk clientePNDni = new Chunk(cuentaBancariaView.getTipoDocumento() + "                : " + cuentaBancariaView.getNumeroDocumento() + "\n");
				//Chunk clientePNTitulares = new Chunk("TITULAR(ES): " + cuentaBancariaView.getTitulares() + "\n");
				Chunk clientePNFecha = new Chunk("FECHA          : " + fechaActual + "\n\n");
				
				Chunk tipoCuentaPN = new Chunk("CUENTA " + cuentaBancariaView.getTipoCuenta() + " Nº "+ cuentaBancariaView.getNumeroCuenta() + "\n");
				Chunk tipoMonedaPN;
				
				if (cuentaBancariaView.getIdMoneda().compareTo(BigInteger.ZERO) == 0) {
					tipoMonedaPN = new Chunk("MONEDA: " + "DOLARES AMERICANOS" + "\n");
				} else if (cuentaBancariaView.getIdMoneda().compareTo(BigInteger.ONE) == 0) {
					tipoMonedaPN = new Chunk("MONEDA: " + "NUEVOS SOLES" + "\n");
				} else {
					tipoMonedaPN = new Chunk("MONEDA: " + "EUROS" + "\n");
				}
				
				Chunk fechaEstadoCuenta = new Chunk("ESTADO DE CUENTA DEL " + fechaDesde + " AL "+ fechaHasta);
				//obteniedo titulares
				/*String tPN = cuentaBancariaView.getTitulares();
				String[] arrayTitulares = tPN.split(",");
				Chunk clientePNTitulares = new Chunk("Titular(es):");
				for (int i = 0; i < arrayTitulares.length; i++) {
					String string = arrayTitulares[i];
				}*/
				
				clientePNNombres.setFont(fuenteDatosCliente);
				clientePNDni.setFont(fuenteDatosCliente);
				//clientePNTitulares.setFont(fuenteDatosCliente);
				clientePNFecha.setFont(fuenteDatosCliente);
				tipoCuentaPN.setFont(fuenteDatosCliente);
				tipoMonedaPN.setFont(fuenteDatosCliente);
				fechaEstadoCuenta.setFont(fuenteDatosCliente);
				
				parrafoSecundario.add(clientePNNombres);
				parrafoSecundario.add(clientePNDni);
				//parrafoSecundario.add(clientePNTitulares);
				parrafoSecundario.add(clientePNFecha);
				parrafoSecundario.add(tipoCuentaPN);
				parrafoSecundario.add(tipoMonedaPN);
				parrafoSecundario.add(fechaEstadoCuenta);
				
			} else {
				Chunk clientePJNombre = new Chunk("CLIENTE       : " + cuentaBancariaView.getSocio() + "\n");
				Chunk clientePJRuc = new Chunk(cuentaBancariaView.getTipoDocumento() + "               : " + cuentaBancariaView.getNumeroDocumento() + "\n");
				//Chunk clientePJTitulares = new Chunk("TITULAR(ES): " + cuentaBancariaView.getTitulares() + "\n");
				Chunk clientePJFecha = new Chunk("FECHA          : " + fechaActual + "\n\n");
				
				Chunk tipoCuentaPJ = new Chunk("CUENTA " + cuentaBancariaView.getTipoCuenta() + " Nº "+ cuentaBancariaView.getNumeroCuenta() + "\n");
				Chunk tipoMonedaPJ;
				
				if (cuentaBancariaView.getIdMoneda().compareTo(BigInteger.ZERO) == 0) {
					tipoMonedaPJ = new Chunk("MONEDA: " + "DOLARES AMERICANOS" + "\n");
				} else if (cuentaBancariaView.getIdMoneda().compareTo(BigInteger.ONE) == 0) {
					tipoMonedaPJ = new Chunk("MONEDA: " + "NUEVOS SOLES" + "\n");
				} else {
					tipoMonedaPJ = new Chunk("MONEDA: " + "EUROS" + "\n");
				}
				
				Chunk fechaEstadoCuenta = new Chunk("ESTADO DE CUENTA DEL " + fechaDesde + " AL "+ fechaHasta);
				//obteniedo titulares
				/*String tPN = cuentaBancariaView.getTitulares();
				String[] arrayTitulares = tPN.split(",");
				Chunk clientePNTitulares = new Chunk("Titular(es):");
				for (int i = 0; i < arrayTitulares.length; i++) {
					String string = arrayTitulares[i];
				}*/
				
				clientePJNombre.setFont(fuenteDatosCliente);
				clientePJRuc.setFont(fuenteDatosCliente);
				//clientePJTitulares.setFont(fuenteDatosCliente);
				clientePJFecha.setFont(fuenteDatosCliente);
				tipoCuentaPJ.setFont(fuenteDatosCliente);
				tipoMonedaPJ.setFont(fuenteDatosCliente);
				fechaEstadoCuenta.setFont(fuenteDatosCliente);
				
				parrafoSecundario.add(clientePJNombre);
				parrafoSecundario.add(clientePJRuc);
				//parrafoSecundario.add(clientePJTitulares);
				parrafoSecundario.add(clientePJFecha);
				parrafoSecundario.add(tipoCuentaPJ);
				parrafoSecundario.add(tipoMonedaPJ);
				parrafoSecundario.add(fechaEstadoCuenta);
				
			}
			
			document.add(parrafoPrincipal);
			document.add(parrafoSecundario);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Font fontTableCabecera = new Font(FontFamily.UNDEFINED, 9, Font.BOLD);
		Font fontTableCuerpo = new Font(FontFamily.UNDEFINED, 9, Font.NORMAL);
		
		float[] columnWidths = { 5f, 4f, 2.8f, 10f, 3.5f, 4f};
		//float[] columnWidths = { 5f, 4f, 2.8f, 10f, 3.5f, 4f, 2.8f};
		PdfPTable table = new PdfPTable(columnWidths);
		table.setWidthPercentage(100);

		PdfPCell cellFechaHoraCabecera = new PdfPCell(new Paragraph("FECHA Y HORA", fontTableCabecera));
		PdfPCell cellTransaccionCabecera = new PdfPCell(new Paragraph("TIPO TRANS.", fontTableCabecera));
		PdfPCell cellOperacionCabecera = new PdfPCell(new Paragraph("NUM. OP.", fontTableCabecera));
		PdfPCell cellReferenciaCabecera = new PdfPCell(new Paragraph("REFERENCIA", fontTableCabecera));
		PdfPCell cellMontoCabecera = new PdfPCell(new Paragraph("MONTO", fontTableCabecera));
		PdfPCell cellSaldoDisponibleCabecera = new PdfPCell(new Paragraph("DISPONIBLE", fontTableCabecera));
		//PdfPCell cellEstado = new PdfPCell(new Paragraph("ESTADO", fontTableCabecera));

		table.addCell(cellFechaHoraCabecera);
		table.addCell(cellTransaccionCabecera);
		table.addCell(cellOperacionCabecera);
		table.addCell(cellReferenciaCabecera);
		table.addCell(cellMontoCabecera);
		table.addCell(cellSaldoDisponibleCabecera);
		//table.addCell(cellEstado);

		for (EstadocuentaBancariaView estadocuentaBancariaView : list) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			String fecHoraFormat = sdf.format(estadocuentaBancariaView.getHora());
			
			if(estadocuentaBancariaView.getEstado()){
				PdfPCell cellFechaHora = new PdfPCell(new Paragraph(fecHoraFormat, fontTableCuerpo));
				table.addCell(cellFechaHora);
				PdfPCell cellTipoTrasaccion = new PdfPCell(new Paragraph(estadocuentaBancariaView.getTipoTransaccionTransferencia(), fontTableCuerpo));
				table.addCell(cellTipoTrasaccion);
				PdfPCell cellNumOperacion = new PdfPCell(new Paragraph(estadocuentaBancariaView.getNumeroOperacion().toString(), fontTableCuerpo));
				table.addCell(cellNumOperacion);
				PdfPCell cellReferencia = new PdfPCell(new Paragraph(estadocuentaBancariaView.getReferencia(), fontTableCuerpo));
				table.addCell(cellReferencia);
				PdfPCell cellMonto = new PdfPCell(new Paragraph(df1.format(estadocuentaBancariaView.getMonto()), fontTableCuerpo));
				cellMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cellMonto);
				PdfPCell cellSaldoDisponible = new PdfPCell(new Paragraph(df1.format(estadocuentaBancariaView.getSaldoDisponible()), fontTableCuerpo));
				cellSaldoDisponible.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cellSaldoDisponible);
				/*if (estadocuentaBancariaView.getEstado()) {
					PdfPCell cellEstadoActivo = new PdfPCell(new Paragraph("Activo", fontTableCuerpo));
					table.addCell(cellEstadoActivo);
				} else {
					PdfPCell cellEstadoExtornado = new PdfPCell(new Paragraph("Extornado", fontTableCuerpo));
					table.addCell(cellEstadoExtornado);
				}*/
			}
		}
		
		Paragraph saldoDisponible = new Paragraph();
		saldoDisponible.setAlignment(Element.ALIGN_CENTER);
		Chunk textoSaldoDisponible = new Chunk("SALDO DISPONIBLE: "+ moneda.getSimbolo() + df1.format(cuentaBancariaView.getSaldo()), fontTableCabecera);
		textoSaldoDisponible.setFont(fontTableCabecera);
		saldoDisponible.add(textoSaldoDisponible);

		try {
			document.add(table);
			document.add(saldoDisponible);
		} catch (DocumentException e) {			
			e.printStackTrace();
		}

		document.close();
				
		return Response.ok(outputStream.toByteArray()).type("application/pdf").build();					
	}

}
