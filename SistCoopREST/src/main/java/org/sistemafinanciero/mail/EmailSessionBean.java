package org.sistemafinanciero.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.EstadocuentaBancariaView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Stateless
@LocalBean
public class EmailSessionBean {

	private int port = 587;
	private String host = "smtp.gmail.com";
	private String from = "coop.caja.ventura@gmail.com";
	private boolean auth = true;
	private String username = "coop.caja.ventura@gmail.com";
	private String password = "caja.ventura";
	private Protocol protocol = Protocol.SMTP;
	private boolean debug = true;

	private String mailMessage = "";
	private String fileName = "estado_cuenta";	
	private String subject = "Estado de cuenta";

	public void writePdf(OutputStream outputStream, List<EstadocuentaBancariaView> list, CuentaBancariaView cuentaBancaria) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);

		document.open();

		document.addTitle("Estado de cuenta bancaria.");
		document.addSubject("Estado de cuenta");
		document.addKeywords("email");
		document.addAuthor("Cooperativa Ventura");
		document.addCreator("Cooperativa Ventura");

		Paragraph saltoDeLinea = new Paragraph();
		document.add(saltoDeLinea);

		/******************* TITULO ******************/
		try {
			// Image img = Image.getInstance("/images/logo.png");
			Image img = Image.getInstance("//usr//share//jboss//archivos//logoCartilla//logo.png");
			img.setAlignment(Image.LEFT | Image.UNDERLYING);
			document.add(img);

			Paragraph parrafoPrincipal = new Paragraph();
			parrafoPrincipal.setSpacingAfter(30);
			parrafoPrincipal.setSpacingBefore(50);
			parrafoPrincipal.setAlignment(Element.ALIGN_CENTER);
			parrafoPrincipal.setIndentationLeft(100);
			parrafoPrincipal.setIndentationRight(50);

			Chunk titulo = new Chunk("ESTADO DE CUENTA\n");
			Font fuenteTitulo = new Font();
			fuenteTitulo.setSize(18);
			fuenteTitulo.setFamily("Arial");
			fuenteTitulo.setStyle(Font.BOLD | Font.UNDERLINE);
			titulo.setFont(fuenteTitulo);
			parrafoPrincipal.add(titulo);

			Chunk subTituloAhorro = new Chunk("APERTURA CUENTA DE AHORRO\n");
			Font fuenteSubtituloAhorro = new Font();
			fuenteSubtituloAhorro.setSize(13);
			fuenteSubtituloAhorro.setFamily("Arial");
			fuenteSubtituloAhorro.setStyle(Font.BOLD | Font.UNDERLINE);
			subTituloAhorro.setFont(fuenteSubtituloAhorro);
			parrafoPrincipal.add(subTituloAhorro);

			document.add(parrafoPrincipal);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		PdfPTable table = new PdfPTable(3);
		PdfPCell cellCabecera1 = new PdfPCell(new Paragraph("SOCIO: " + cuentaBancaria.getSocio()));
		cellCabecera1.setColspan(3);
		table.addCell(cellCabecera1);

		PdfPCell cellCabecera2 = new PdfPCell(new Paragraph("CUENTA Nº: " + cuentaBancaria.getNumeroCuenta()));
		cellCabecera2.setColspan(3);
		table.addCell(cellCabecera2);

		PdfPCell cellFecha = new PdfPCell(new Paragraph("FECHA"));
		cellFecha.setBackgroundColor(new BaseColor(51, 144, 66));
		PdfPCell cellDescripcion = new PdfPCell(new Paragraph("DESCRIPCION"));
		cellDescripcion.setBackgroundColor(new BaseColor(51, 144, 66));
		PdfPCell cellMonto = new PdfPCell(new Paragraph("MONTO"));
		cellMonto.setBackgroundColor(new BaseColor(51, 144, 66));

		table.addCell(cellFecha);
		table.addCell(cellDescripcion);
		table.addCell(cellMonto);

		for (EstadocuentaBancariaView estadocuentaBancariaView : list) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String fechaString = sdf.format(estadocuentaBancariaView.getHora());
			table.addCell(fechaString);
			table.addCell(estadocuentaBancariaView.getTipoTransaccionTransferencia());
			table.addCell(estadocuentaBancariaView.getMonto().toString());
		}

		table.addCell("");
		table.addCell("Saldo:");
		table.addCell(cuentaBancaria.getSaldo().toString());

		document.add(table);

		document.close();
	}

	public void writeExcel(OutputStream outputStream, List<EstadocuentaBancariaView> list, CuentaBancariaView cuentaBancaria) throws Exception {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");
				
		Row row1 = sheet.createRow(0);		
		Cell cellSocioLabel = row1.createCell(0);			
		cellSocioLabel.setCellValue("SOCIO:");
		Cell cellSocioValue = row1.createCell(1);			
		cellSocioValue.setCellValue(cuentaBancaria.getSocio());
		
		Row row2 = sheet.createRow(1);		
		Cell cellCuentaLabel = row2.createCell(0);			
		cellCuentaLabel.setCellValue("Nº CUENTA:");
		Cell cellCuentaValue = row2.createCell(1);			
		cellCuentaValue.setCellValue(cuentaBancaria.getNumeroCuenta());
		
		
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle styleBold = workbook.createCellStyle();
		styleBold.setFont(font);
		styleBold.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		
		Row row3 = sheet.createRow(2);		
		Cell cellFechaCabecera = row3.createCell(0);			
		cellFechaCabecera.setCellValue("FECHA");	
		cellFechaCabecera.setCellStyle(styleBold);
		Cell cellHoraCabecera = row3.createCell(1);			
		cellHoraCabecera.setCellValue("HORA");	
		cellHoraCabecera.setCellStyle(styleBold);
		Cell cellTipoTransaccionCabecera = row3.createCell(2);			
		cellTipoTransaccionCabecera.setCellValue("DESCRIPCION");	
		cellTipoTransaccionCabecera.setCellStyle(styleBold);
		Cell cellReferenciaCabecera = row3.createCell(3);			
		cellReferenciaCabecera.setCellValue("REFERENCIA");	
		cellReferenciaCabecera.setCellStyle(styleBold);
		Cell cellMontoCabecera = row3.createCell(4);			
		cellMontoCabecera.setCellValue("MONTO");
		cellMontoCabecera.setCellStyle(styleBold);
		Cell cellSaldoDisponibleCabecera = row3.createCell(5);			
		cellSaldoDisponibleCabecera.setCellValue("S.DISPONIBLE");	
		cellSaldoDisponibleCabecera.setCellStyle(styleBold);
		
		for (int i = 0; i < list.size(); i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String fechaString = sdf.format(list.get(i).getFecha());
			
			sdf = new SimpleDateFormat("HH:mm:ss");
			String horaString = sdf.format(list.get(i).getHora());
			
			/*****EXCEL*****/
			Row row = sheet.createRow(i+3);
			
			Cell cellFecha = row.createCell(0);			
			cellFecha.setCellValue(fechaString);
			
			Cell cellHora = row.createCell(1);			
			cellHora.setCellValue(horaString);
			
			Cell cellTipoTransaccion = row.createCell(2);			
			cellTipoTransaccion.setCellValue(list.get(i).getTipoTransaccionTransferencia());
			
			Cell cellReferencia = row.createCell(3);			
			cellReferencia.setCellValue(list.get(i).getReferencia());
			
			Cell cellMonto = row.createCell(4, Cell.CELL_TYPE_NUMERIC);			
			cellMonto.setCellValue(list.get(i).getMonto().doubleValue());
			
			Cell cellSaldoDisponible = row.createCell(5, Cell.CELL_TYPE_NUMERIC);			
			cellSaldoDisponible.setCellValue(list.get(i).getSaldoDisponible().doubleValue());
		}
		
		Row rowSaldo = sheet.createRow(list.size() + 3);	
		Cell cellSaldoLabel = rowSaldo.createCell(0);
		cellSaldoLabel.setCellValue("SALDO");				
		Cell cellSaldoValue = rowSaldo.createCell(1, Cell.CELL_TYPE_NUMERIC);
		cellSaldoValue.setCellValue(cuentaBancaria.getSaldo().doubleValue());	
		
		workbook.write(outputStream);		
		workbook.close();

	}

	@Asynchronous
	public void sendMailPdf(CuentaBancariaView cuentaBancariaView, List<EstadocuentaBancariaView> list, List<String> emails, Date desde, Date hasta) {
		mailMessage = "Buen dia, el siguiente estado de cuenta corresponde a";
		if (desde == null || hasta == null)
			mailMessage = mailMessage + " los ultimos 30 dias.";
		else
			mailMessage = mailMessage + " el perido desde:" + desde.toString() + " hasta:" + hasta.toString();

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

		switch (protocol) {
		case SMTPS:
			props.put("mail.smtp.ssl.enable", true);
			break;
		case TLS:
			props.put("mail.smtp.starttls.enable", true);
			break;
		case SMTP:
			props.put("mail.smtp.starttls.enable", true);
		default:
			break;
		}

		Authenticator authenticator = null;
		if (auth) {
			props.put("mail.smtp.auth", true);
			authenticator = new Authenticator() {
				private PasswordAuthentication pa = new PasswordAuthentication(username, password);

				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return pa;
				}
			};
		}

		Session session = Session.getInstance(props, authenticator);
		session.setDebug(debug);

		// creando el pdf
		ByteArrayOutputStream outputStream = null;
		try {
			// construct the text body part
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(mailMessage);

			// now write the PDF content to the output stream
			outputStream = new ByteArrayOutputStream();
			
			
			//enviando para crear el archivo a enviar
			writePdf(outputStream, list, cuentaBancariaView);
			
			
			byte[] bytes = outputStream.toByteArray();

			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName(fileName+".pdf");

			// construct the mime multi part
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);

			// create the sender/recipient addresses
			for (String mail : emails) {
				InternetAddress iaSender = new InternetAddress(from);
				InternetAddress iaRecipient = new InternetAddress(mail);

				// construct the mime message
				MimeMessage mimeMessage = new MimeMessage(session);
				mimeMessage.setSender(iaSender);
				mimeMessage.setSubject(subject);
				mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
				mimeMessage.setContent(mimeMultipart);

				// send off the email
				Transport.send(mimeMessage);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// clean off
			if (null != outputStream) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (Exception ex) {
				}
			}
		}
	}
	
	@Asynchronous
	public void sendMailExcel(CuentaBancariaView cuentaBancariaView, List<EstadocuentaBancariaView> list, List<String> emails, Date desde, Date hasta) {
		mailMessage = "Buen dia, el siguiente estado de cuenta corresponde a";
		if (desde == null || hasta == null)
			mailMessage = mailMessage + " los ultimos 30 dias.";
		else
			mailMessage = mailMessage + " el perido desde:" + desde.toString() + " hasta:" + hasta.toString();

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

		switch (protocol) {
		case SMTPS:
			props.put("mail.smtp.ssl.enable", true);
			break;
		case TLS:
			props.put("mail.smtp.starttls.enable", true);
			break;
		case SMTP:
			props.put("mail.smtp.starttls.enable", true);
		default:
			break;
		}

		Authenticator authenticator = null;
		if (auth) {
			props.put("mail.smtp.auth", true);
			authenticator = new Authenticator() {
				private PasswordAuthentication pa = new PasswordAuthentication(username, password);

				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return pa;
				}
			};
		}

		Session session = Session.getInstance(props, authenticator);
		session.setDebug(debug);

		// creando el pdf
		ByteArrayOutputStream outputStream = null;
		try {
			// construct the text body part
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(mailMessage);

			// now write the PDF content to the output stream
			outputStream = new ByteArrayOutputStream();
			
			
			//enviando para crear el archivo a enviar
			writeExcel(outputStream, list, cuentaBancariaView);
			
			
			byte[] bytes = outputStream.toByteArray();

			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName(fileName+".xls");

			// construct the mime multi part
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);

			// create the sender/recipient addresses
			for (String mail : emails) {
				InternetAddress iaSender = new InternetAddress(from);
				InternetAddress iaRecipient = new InternetAddress(mail);

				// construct the mime message
				MimeMessage mimeMessage = new MimeMessage(session);
				mimeMessage.setSender(iaSender);
				mimeMessage.setSubject(subject);
				mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
				mimeMessage.setContent(mimeMultipart);

				// send off the email
				Transport.send(mimeMessage);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// clean off
			if (null != outputStream) {
				try {
					outputStream.close();
					outputStream = null;
				} catch (Exception ex) {
				}
			}
		}
	}

}
