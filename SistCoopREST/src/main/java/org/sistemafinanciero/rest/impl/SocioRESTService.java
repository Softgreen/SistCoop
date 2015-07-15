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
import java.util.Set;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.SocioView;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.NonexistentEntityException;
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
	//private final static String cartillaURL = "D:\\cartilla";
	private final static String cartillaURL = "//usr//share//jboss//archivos//cartillaInformacion//";
	
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
    public Response listAll(TipoPersona tipoPersona, BigInteger idTipoDocumento, String numeroDocumento) {        
        SocioView socio = socioServiceNT.find(tipoPersona, idTipoDocumento, numeroDocumento);
        Response response = Response.status(Response.Status.OK).entity(socio).build();
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
			
			parrafoPrincipal.setSpacingAfter(40);
			parrafoPrincipal.setSpacingBefore(50);
			parrafoPrincipal.setAlignment(Element.ALIGN_CENTER);
			parrafoPrincipal.setIndentationLeft(100);
			parrafoPrincipal.setIndentationRight(50);
			
			Chunk titulo = new Chunk("CARTILLA DE INFORMACIÓN\n");
			Font fuenteTitulo = new Font();
			fuenteTitulo.setSize(18);
			fuenteTitulo.setFamily("Arial");
			fuenteTitulo.setStyle(Font.BOLD | Font.UNDERLINE);
			
			titulo.setFont(fuenteTitulo);
			parrafoPrincipal.add(titulo);

			Chunk subTitulo = new Chunk("APERTURA CUENTA DE APORTE\n");
			Font fuenteSubtitulo = new Font();
			fuenteSubtitulo.setSize(13);
			fuenteSubtitulo.setFamily("Arial");
			fuenteSubtitulo.setStyle(Font.BOLD | Font.UNDERLINE);
			
			subTitulo.setFont(fuenteSubtitulo);
			parrafoPrincipal.add(subTitulo);
			document.add(parrafoPrincipal);

			/******************* DATOS BASICOS DEL SOCIO **********************/
			PdfPTable table1 = new PdfPTable(4);
			table1.setWidthPercentage(100);

			PdfPCell cabecera1 = new PdfPCell(new Paragraph("DATOS BASICOS DEL SOCIO", fontBold));
			cabecera1.setColspan(4);
			cabecera1.setBackgroundColor(baseColor);

			PdfPCell cellCodigoSocio = new PdfPCell(new Paragraph("Codigo Socio:", fontBold));
			cellCodigoSocio.setColspan(1);
			cellCodigoSocio.setBorder(Rectangle.NO_BORDER);
			
			PdfPCell cellCodigoSocioValue = new PdfPCell(new Paragraph(socio.getIdsocio().toString(), font));
			cellCodigoSocioValue.setColspan(3);
			cellCodigoSocioValue.setBorder(Rectangle.NO_BORDER);
			
			PdfPCell cellApellidosNombres = new PdfPCell(new Paragraph(socio.getTipoPersona().equals(TipoPersona.NATURAL) ? "Apellidos y Nombres:" : "Razón Social:", fontBold));
			cellApellidosNombres.setColspan(1);
			cellApellidosNombres.setBorder(Rectangle.NO_BORDER);
			
			PdfPCell cellApellidosNombresValue = new PdfPCell(new Paragraph(socio.getSocio(), font));
			cellApellidosNombresValue.setColspan(3);
			cellApellidosNombresValue.setBorder(Rectangle.NO_BORDER);

			table1.addCell(cabecera1);
			table1.addCell(cellCodigoSocio);
			table1.addCell(cellCodigoSocioValue);
			table1.addCell(cellApellidosNombres);
			table1.addCell(cellApellidosNombresValue);

			PdfPCell cellDNI = new PdfPCell(new Paragraph(socio.getTipoDocumento()+":", fontBold));
			cellDNI.setColspan(1);
			cellDNI.setBorder(Rectangle.NO_BORDER);
			
			PdfPCell cellDNIValue = new PdfPCell(new Paragraph(socio.getNumeroDocumento(), font));
			cellDNIValue.setColspan(1);
			cellDNIValue.setBorder(Rectangle.NO_BORDER);
			
			PdfPCell cellFechaNaciemiento = new PdfPCell(new Paragraph(socio.getTipoPersona().equals(TipoPersona.NATURAL) ? "Fecha de Nacimiento:" : "Fecha de Constitución", fontBold));
			cellFechaNaciemiento.setColspan(1);
			cellFechaNaciemiento.setBorder(Rectangle.NO_BORDER);
			
			PdfPCell cellFechaNacimientoValue = new PdfPCell(new Paragraph(DATE_FORMAT.format(socio.getFechaNacimiento()), font));
			cellFechaNacimientoValue.setColspan(1);
			cellFechaNacimientoValue.setBorder(Rectangle.NO_BORDER);

			table1.addCell(cellDNI);
			table1.addCell(cellDNIValue);
			table1.addCell(cellFechaNaciemiento);
			table1.addCell(cellFechaNacimientoValue);

			document.add(table1);
			document.add(new Paragraph("\n"));


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
			Chunk parrafo1 = new Chunk("Los aportes individuales serán pagados por los Asociados en forma periódica de conformidad con lo establecido en el Estatuto y el Reglamento de Aportes Sociales de la Cooperativa. El aporte social ordinario de cada Asociado será mínimo de S/. 10.00 Nuevos Soles si es mayor de edad y S/. 5.00 Nuevos Soles si es menor de edad.\n\n", font);
			parrafo1.setLineHeight(13);
			parrafoDeclaraciones.add(parrafo1);

			Chunk parrafoDeclaracionesFinalesCab = new Chunk("DECLARACIÓN FINAL DEL CLIENTE: ", fontBold);
			
			Paragraph parrafoDeclaracionesFinalesValue = new Paragraph();
			Chunk parrafo2 = new Chunk("Declaro haber leido previamente las condiciones establecidas en el Contrato de Depósito y la Cartilla de Información, asi como haber sido instruido acerca de los alcances y significados de los términos y condiciones establecidos en dicho documento habiendo sido absueltas y aclaradas a mi satisfacción todas las consultas efectuadas y/o dudas, suscribe el presente documento en duplicado y con pleno y exacto conocimiento de los mismos.\n", font);
			parrafo2.setLineHeight(13);
			parrafoDeclaracionesFinalesValue.add(parrafo2);
			
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
			Chunk firma01 = new Chunk("Caja Ventura");
			Chunk firma02 = new Chunk("El Socio     ");

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

	@Override
	public Response getBeneficiarios(BigInteger id) {
		Set<Beneficiario> list = socioServiceNT.getBeneficiarios(id);
		Response response = Response.status(Response.Status.CREATED).entity(list).build();
		return response;
	}
	
	@Override
	public Response getBeneficiario(BigInteger id, BigInteger idBeneficiario) {
		Beneficiario beneficiario = socioServiceNT.findBeneficiarioById(idBeneficiario);
		Response response = Response.status(Response.Status.OK).entity(beneficiario).build();
		return response;
	}
	
	@Override
	public Response createBeneficiario(BigInteger id, Beneficiario beneficiario) {
		Response response;
		try {
			BigInteger idBeneficiario = socioServiceTS.addBeneficiario(id, beneficiario);
			response = Response.status(Response.Status.CREATED).entity(Jsend.getSuccessJSend(idBeneficiario)).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response deleteBeneficiario(BigInteger id, BigInteger idBeneficiario) {
		try {
			socioServiceTS.deleteBeneficiario(idBeneficiario);
			return Response.status(Status.OK).build();
		} catch (NonexistentEntityException e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (RollbackFailureException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@Override
	public Response updateBeneficiario(BigInteger id, BigInteger idBeneficiario, Beneficiario beneficiario) {
		Response response;
		try {
			socioServiceTS.updateBeneficiario(idBeneficiario, beneficiario);
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

}
