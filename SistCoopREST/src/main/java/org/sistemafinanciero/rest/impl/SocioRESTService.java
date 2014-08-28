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
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.SocioView;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.SocioREST;
import org.sistemafinanciero.rest.dto.ApoderadoDTO;
import org.sistemafinanciero.rest.dto.SocioDTO;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.nt.SessionServiceNT;
import org.sistemafinanciero.service.nt.SocioServiceNT;
import org.sistemafinanciero.service.ts.SocioServiceTS;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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

public class SocioRESTService implements SocioREST {

	private final static String baseUrl = "/socios";
	private final static String cartillaURL = "D:\\cartilla";

	@EJB
	private SocioServiceNT socioServiceNT;

	@EJB
	private SocioServiceTS socioServiceTS;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	@EJB
	private SessionServiceNT sessionServiceNT;

	@Override
	public Response listAll(String filterText, Boolean estadoCuentaAporte, Boolean estadoSocio, Integer offset, Integer limit) {
		List<SocioView> list = socioServiceNT.findAllView(filterText, estadoCuentaAporte, estadoSocio, offset, limit);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response countAll() {
		int count = socioServiceNT.count();
		Response response = Response.status(Response.Status.OK).entity(count).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		SocioView socio = socioServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(socio).build();
		return response;
	}

	@Override
	public Response getCuentaAporte(BigInteger id) {
		CuentaAporte cuentaAporte = socioServiceNT.getCuentaAporte(id);
		Response response = Response.status(Response.Status.OK).entity(cuentaAporte).build();
		return response;
	}

	@Override
	public Response congelarCuentaAporte(BigInteger id) {
		Response response;
		try {
			socioServiceTS.congelarCuentaAporte(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response descongelarCuentaAporte(BigInteger id) {
		Response response;
		try {
			socioServiceTS.descongelarCuentaAporte(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getApoderado(BigInteger id) {
		PersonaNatural apoderado = socioServiceNT.getApoderado(id);
		Response response = Response.status(Response.Status.OK).entity(apoderado).build();
		return response;
	}

	@Override
	public Response cambiarApoderado(BigInteger idSocio, ApoderadoDTO apoderado) {
		Response response;
		BigInteger idTipoDocumento = apoderado.getIdTipoDocumento();
		String numeroDocumento = apoderado.getNumeroDocumento();
		PersonaNatural personaNatural = personaNaturalServiceNT.find(idTipoDocumento, numeroDocumento);
		if (personaNatural != null) {
			BigInteger idPersonaNatural = personaNatural.getIdPersonaNatural();
			try {
				socioServiceTS.cambiarApoderado(idSocio, idPersonaNatural);
				response = Response.status(Response.Status.NO_CONTENT).build();
			} catch (RollbackFailureException e) {
				Jsend jsend = Jsend.getErrorJSend(e.getMessage());
				response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
			}
		} else {
			Jsend jsend = Jsend.getErrorJSend("Persona no encontrada");
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response eliminarApoderado(BigInteger idSocio) {
		Response response;
		try {
			socioServiceTS.deleteApoderado(idSocio);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getCuentasBancarias(BigInteger id) {
		List<CuentaBancariaView> list = socioServiceNT.getCuentasBancarias(id);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response getAportesHistorial(BigInteger idSocio, Long desde, Long hasta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createSocio(SocioDTO socio) {
		Response response;

		TipoPersona tipoPersona = socio.getTipoPersona();
		BigInteger idDocSocio = socio.getIdTipoDocumentoSocio();
		String numDocSocio = socio.getNumeroDocumentoSocio();
		BigInteger idDocApoderado = socio.getIdTipoDocumentoApoderado();
		String numDocApoderado = socio.getNumeroDocumentoApoderado();

		Agencia agencia = sessionServiceNT.getAgenciaOfSession();
		PersonaNatural apoderado = null;
		if (idDocApoderado != null && numDocApoderado != null)
			apoderado = personaNaturalServiceNT.find(idDocApoderado, numDocApoderado);

		try {
			SocioView socioView = new SocioView();
			socioView.setCodigoAgencia(agencia.getCodigo());
			socioView.setTipoPersona(tipoPersona);
			socioView.setIdTipoDocumento(idDocSocio);
			socioView.setNumeroDocumento(numDocSocio);
			if (apoderado != null)
				socioView.setIdApoderado(apoderado.getIdPersonaNatural());

			BigInteger idSocio = socioServiceTS.create(socioView);
			URI resource = new URI(baseUrl + "/" + idSocio.toString());
			response = Response.created(resource).entity(Jsend.getSuccessJSend(idSocio)).build();
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
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
	public Response desactivarSocio(BigInteger id) {
		Response response;
		try {
			socioServiceTS.inactivarSocio(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getCartillaInformacion(BigInteger id) {
		OutputStream file;

		// CuentaBancariaView cuentaBancaria =
		// cuentaBancariaServiceNT.findById(id);
		SocioView socio = socioServiceNT.findById(id);
		CuentaAporte cuentaAporte = socioServiceNT.getCuentaAporte(id);

		Moneda moneda = cuentaAporte.getMoneda();

		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		BaseColor baseColor = BaseColor.LIGHT_GRAY;
		Font font = FontFactory.getFont("Times-Roman", 10f);
		Font fontBold = FontFactory.getFont("Times-Roman", 10f, Font.BOLD);
		try {
			file = new FileOutputStream(new File(cartillaURL + "\\" + id + ".pdf"));
			Document document = new Document(PageSize.A4);// *4
			PdfWriter writer = PdfWriter.getInstance(document, file);
			document.open();

			/******************* TITULO ******************/
			Image img = Image.getInstance("/images/logo.png");
			img.setAlignment(Image.LEFT | Image.UNDERLYING);
			document.add(img);

			Paragraph parrafoPrincipal = new Paragraph();
			parrafoPrincipal.setAlignment(Element.ALIGN_CENTER);

			Chunk titulo = new Chunk("\n\n\n\nCARTILLA DE INFORMACION\n", fontBold);
			Font fontTitulo = FontFactory.getFont("Times-Roman", 14f);
			titulo.setUnderline(0.2f, -2f);
			titulo.setFont(fontTitulo);
			parrafoPrincipal.add(titulo);

			Chunk subTitulo = new Chunk("APERTURA DE CUENTA DE APORTES\n\n", fontBold);
			Font fontSubTitulo = FontFactory.getFont("Times-Roman", 12f);
			subTitulo.setFont(fontSubTitulo);
			subTitulo.setUnderline(0.2f, -2f);
			parrafoPrincipal.add(subTitulo);

			document.add(parrafoPrincipal);

			Chunk mensaje01 = new Chunk("La apertura de una cuenta de aporte generará intereses y demás beneficios complementarios de acuerdo al saldo promedio mensual o saldo diario establecido en la Cartilla de Información. Para estos efectos, se entiende por saldo promedio mensual, la suma del saldo diario dividida entre el número de días del mes. La cuenta de ahorro podrá generar comisiones y gastos de acuerdo a las condiciones aceptadas en la Cartilla de Información.\n\n");
			mensaje01.setFont(font);

			Paragraph parrafo1 = new Paragraph();
			parrafo1.setLeading(11f);
			parrafo1.add(mensaje01);
			parrafo1.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(parrafo1);

			/******************* DATOS BASICOS DEL SOCIO **********************/
			PdfPTable table1 = new PdfPTable(4);
			table1.setWidthPercentage(100);

			PdfPCell cabecera1 = new PdfPCell(new Paragraph("DATOS BASICOS DEL SOCIO", fontBold));
			cabecera1.setColspan(4);
			cabecera1.setBackgroundColor(baseColor);

			PdfPCell cellApellidosNombres = new PdfPCell(new Paragraph(socio.getTipoPersona().equals(TipoPersona.NATURAL) ? "Apellidos y nombres:" : "Razón social:", font));
			cellApellidosNombres.setBorder(Rectangle.NO_BORDER);
			PdfPCell cellApellidosNombresValue = new PdfPCell(new Paragraph(socio.getSocio(), font));
			cellApellidosNombresValue.setColspan(3);
			cellApellidosNombresValue.setBorder(Rectangle.NO_BORDER);

			table1.addCell(cabecera1);
			table1.addCell(cellApellidosNombres);
			table1.addCell(cellApellidosNombresValue);

			PdfPCell cellDNI = new PdfPCell(new Paragraph(socio.getTipoDocumento(), font));
			cellDNI.setBorder(Rectangle.NO_BORDER);
			PdfPCell cellDNIValue = new PdfPCell(new Paragraph(socio.getNumeroDocumento(), font));
			cellDNIValue.setBorder(Rectangle.NO_BORDER);
			PdfPCell cellFechaNaciemiento = new PdfPCell(new Paragraph(socio.getTipoPersona().equals(TipoPersona.NATURAL) ? "Fecha de nacimiento:" : "Fecha de constitución", font));
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
			/*
			 * PdfPTable table2 = new PdfPTable(7);
			 * table2.setWidthPercentage(100);
			 * 
			 * PdfPCell cabecera2 = new PdfPCell(new Paragraph("TITULARES",
			 * fontBold)); cabecera2.setColspan(7);
			 * cabecera2.setBackgroundColor(baseColor);
			 * table2.addCell(cabecera2);
			 * 
			 * PdfPCell cellTipoDocumentoCab = new PdfPCell(new
			 * Paragraph("Tipo doc.", font)); PdfPCell cellNumeroDocumentoCab =
			 * new PdfPCell(new Paragraph("Nº Doc.", font)); PdfPCell
			 * cellApellidoPaternoCab = new PdfPCell(new
			 * Paragraph("Ap. Paterno", font)); PdfPCell cellApellidoMaternoCab
			 * = new PdfPCell(new Paragraph("Ap. Materno", font)); PdfPCell
			 * cellNombresCab = new PdfPCell(new Paragraph("Nombres", font));
			 * PdfPCell cellSexoCab = new PdfPCell(new Paragraph("Sexo", font));
			 * PdfPCell cellFechaNacimientoCab = new PdfPCell(new
			 * Paragraph("Fec.nac.", font));
			 * 
			 * cellTipoDocumentoCab.setBorder(Rectangle.NO_BORDER);
			 * cellNumeroDocumentoCab.setBorder(Rectangle.NO_BORDER);
			 * cellApellidoPaternoCab.setBorder(Rectangle.NO_BORDER);
			 * cellApellidoMaternoCab.setBorder(Rectangle.NO_BORDER);
			 * cellNombresCab.setBorder(Rectangle.NO_BORDER);
			 * cellSexoCab.setBorder(Rectangle.NO_BORDER);
			 * cellFechaNacimientoCab.setBorder(Rectangle.NO_BORDER);
			 * 
			 * table2.addCell(cellTipoDocumentoCab);
			 * table2.addCell(cellNumeroDocumentoCab);
			 * table2.addCell(cellApellidoPaternoCab);
			 * table2.addCell(cellApellidoMaternoCab);
			 * table2.addCell(cellNombresCab); table2.addCell(cellSexoCab);
			 * table2.addCell(cellFechaNacimientoCab);
			 * 
			 * for (Titular titular : listTitulares) { PersonaNatural
			 * personaNatural = titular.getPersonaNatural();
			 * 
			 * PdfPCell cellTipoDocumento = new PdfPCell(new
			 * Paragraph(personaNatural.getTipoDocumento().getAbreviatura(),
			 * font)); PdfPCell cellNumeroDocumento = new PdfPCell(new
			 * Paragraph(personaNatural.getNumeroDocumento(), font)); PdfPCell
			 * cellApellidoPaterno = new PdfPCell(new
			 * Paragraph(personaNatural.getApellidoPaterno(), font)); PdfPCell
			 * cellApellidoMaterno = new PdfPCell(new
			 * Paragraph(personaNatural.getApellidoMaterno(), font)); PdfPCell
			 * cellNombres = new PdfPCell(new
			 * Paragraph(personaNatural.getNombres(), font)); PdfPCell cellSexo
			 * = new PdfPCell(new Paragraph(personaNatural.getSexo().toString(),
			 * font)); PdfPCell cellFechaNacimiento = new PdfPCell(new
			 * Paragraph(
			 * DATE_FORMAT.format(personaNatural.getFechaNacimiento()), font));
			 * 
			 * cellTipoDocumento.setBorder(Rectangle.NO_BORDER);
			 * cellNumeroDocumento.setBorder(Rectangle.NO_BORDER);
			 * cellApellidoPaterno.setBorder(Rectangle.NO_BORDER);
			 * cellApellidoMaterno.setBorder(Rectangle.NO_BORDER);
			 * cellNombres.setBorder(Rectangle.NO_BORDER);
			 * cellSexo.setBorder(Rectangle.NO_BORDER);
			 * cellFechaNacimiento.setBorder(Rectangle.NO_BORDER);
			 * 
			 * table2.addCell(cellTipoDocumento);
			 * table2.addCell(cellNumeroDocumento);
			 * table2.addCell(cellApellidoPaterno);
			 * table2.addCell(cellApellidoMaterno); table2.addCell(cellNombres);
			 * table2.addCell(cellSexo); table2.addCell(cellFechaNacimiento); }
			 * 
			 * document.add(table2); document.add(new Paragraph("\n"));
			 */

			/******************* PRODUCTOS Y SERVICIOS **********************/
			PdfPTable table3 = new PdfPTable(4);
			table3.setWidthPercentage(100);

			PdfPCell cabecera3 = new PdfPCell(new Paragraph("PRODUCTOS Y SERVICIOS", fontBold));
			cabecera3.setColspan(4);
			cabecera3.setBackgroundColor(baseColor);
			table3.addCell(cabecera3);

			PdfPCell cellProductoCab = new PdfPCell(new Paragraph("Producto", font));
			PdfPCell cellMonedaCab = new PdfPCell(new Paragraph("Moneda", font));
			PdfPCell cellNumeroCuentaCab = new PdfPCell(new Paragraph("Numero cuenta", font));
			PdfPCell cellFechaAperturaCab = new PdfPCell(new Paragraph("Fecha apertura", font));
			cellProductoCab.setBorder(Rectangle.NO_BORDER);
			cellMonedaCab.setBorder(Rectangle.NO_BORDER);
			cellNumeroCuentaCab.setBorder(Rectangle.NO_BORDER);
			cellFechaAperturaCab.setBorder(Rectangle.NO_BORDER);
			table3.addCell(cellProductoCab);
			table3.addCell(cellMonedaCab);
			table3.addCell(cellNumeroCuentaCab);
			table3.addCell(cellFechaAperturaCab);

			PdfPCell cellProducto = new PdfPCell(new Paragraph("CUENTA DE APORTE", font));
			PdfPCell cellMoneda = new PdfPCell(new Paragraph(moneda.getDenominacion(), font));
			PdfPCell cellNumeroCuenta = new PdfPCell(new Paragraph(cuentaAporte.getNumeroCuenta(), font));
			PdfPCell cellFechaApertura = new PdfPCell(new Paragraph(DATE_FORMAT.format(socio.getFechaAsociado()), font));
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

			Chunk subTitulo1 = new Chunk("INFORMACIÓN ADICIONAL\n", font);
			parrafoDeclaraciones.add(subTitulo1);

			// añadiendo las viñetas
			Paragraph enumeracion01 = new Paragraph("- La Tasa de Rendimiento Efectiva Anual (TREA) de una cuenta de ahorros es igual a la Tasa Efectiva Anual (TEA), descontándole las comisiones o gastos aplicables a la cuenta. Por ejemplo, para un depósito de personas naturales de S/. 1,000.00 a 360 días, considerando que durante dicho plazo no existen transacciones adicionales a la apertura de la cuenta, la TEA de 0.25% es igual a la TREA, pues no hay descuento de comisiones ni gastos.\n", font);
			Paragraph enumeracion02 = new Paragraph("- El (LOS) CLIENTE(S) tendrán a su disposición en nuestras ventanillas de atención, los estados de cuenta mensuales de las cuentas de ahorro.\n", font);
			Paragraph enumeracion03 = new Paragraph("- La cancelación de las cuentas de ahorro se efectúa a solicitud del (los) titular(es) de la cuenta, sólo en la Agencia de origen y en casos excepcionales al fallecimiento del titular previa presentación de la documentación legal correspondiente (sucesión intestada o declaratoria de herederos debidamente inscrita en registros públicos), por incapacidad física del titular o por mandato judicial.\n", font);

			parrafoDeclaraciones.add(enumeracion01);
			parrafoDeclaraciones.add(enumeracion02);
			parrafoDeclaraciones.add(enumeracion03);

			//
			Chunk parrafoDeclaracionesFinalesCab = new Chunk("DECLARACIÓN FINAL DEL CLIENTE: ", font);
			Paragraph parrafoDeclaracionesFinalesValue = new Paragraph("Declaro haber leido previamente las condiciones establecidas en el Contrato de Depósito y la Cartilla de Información, asi como haber sido instruido acerca de los alcances y significados de los términos y condiciones establecidos en dicho documento habiendo sido absueltas y aclaradas a mi satisfacción todas las consultas efectuadas y/o dudas, suscribe el presente documento en duplicado y con pleno y exacto conocimiento de los mismos.\n", font);

			parrafoDeclaraciones.add(parrafoDeclaracionesFinalesCab);
			parrafoDeclaraciones.add(parrafoDeclaracionesFinalesValue);

			PdfPCell declaraciones = new PdfPCell(parrafoDeclaraciones);
			declaraciones.setBorder(Rectangle.NO_BORDER);
			declaraciones.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
			table4.addCell(declaraciones);

			document.add(table4);

			// firmas
			Chunk firmaP01 = new Chunk("..........................................");
			Chunk firmaP02 = new Chunk("..........................................\n");
			Chunk firma01 = new Chunk("Cooperativa");
			Chunk firma02 = new Chunk("Cliente    ");

			Paragraph firmas = new Paragraph("\n\n\n\n\n\n");
			firmas.setAlignment(Element.ALIGN_CENTER);

			firmas.add(firmaP01);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(firmaP02);

			firmas.add(firma01);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(Chunk.SPACETABBING);
			firmas.add(firma02);

			document.add(firmas);

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

}
