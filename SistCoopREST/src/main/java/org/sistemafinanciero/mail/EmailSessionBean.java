package org.sistemafinanciero.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.EstadocuentaBancariaView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfCell;

@Stateless
@LocalBean
public class EmailSessionBean {

	private int port = 587;
	private String host = "smtp.gmail.com";
	private String from = "carlosthe19916@gmail.com";
	private boolean auth = true;
	private String username = "carlosthe19916@gmail.com";
	private String password = "zarate19916";
	private Protocol protocol = Protocol.SMTP;
	private boolean debug = true;

	private String mailMessage = "";
	private String fileName = "estado_cuenta.pdf";
	private String subject = "Estado de cuenta";
	
	@Asynchronous
	public void sendMail() {

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
			textBodyPart.setText("este es un pdf de prueba");

			// now write the PDF content to the output stream
			outputStream = new ByteArrayOutputStream();
			writePdf(outputStream);
			byte[] bytes = outputStream.toByteArray();

			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName("test.pdf");

			// construct the mime multi part
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);

			// create the sender/recipient addresses
			InternetAddress iaSender = new InternetAddress(from);
			InternetAddress iaRecipient = new InternetAddress("carlosthe19916@hotmail.com");

			// construct the mime message
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setSender(iaSender);
			mimeMessage.setSubject("Esta es una prueba");
			mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
			mimeMessage.setContent(mimeMultipart);

			// send off the email
			Transport.send(mimeMessage);

			//System.out.println("sent from " + from + ", to " + "carlosthe19916@hotmail.com" + "; server = " + smtpHost + ", port = " + smtpPort);
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

		
		
		
		/*MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] address = { new InternetAddress("carlosthe19916@hotmail.com") };// to
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject("Esta es una prueba"); // tema
			message.setSentDate(new Date());
			message.setText("hola carlos esta es una prueba."); // contenido del
																// mensaje
			Transport.send(message);
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}*/
		

	}

	/**
	 * Writes the content of a PDF file (using iText API) to the
	 * {@link OutputStream}.
	 * 
	 * @param outputStream
	 *            {@link OutputStream}.
	 * @throws Exception
	 */
	public void writePdf(OutputStream outputStream) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);

		document.open();

		document.addTitle("Test PDF");
		document.addSubject("Testing email PDF");
		document.addKeywords("iText, email");
		document.addAuthor("Jee Vang");
		document.addCreator("Jee Vang");

		Paragraph paragraph = new Paragraph();
		paragraph.add(new Chunk("hello!"));
		document.add(paragraph);

		document.close();
	}	
	
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
		try{
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
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm"); // Set your date format
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

	@Asynchronous
	public void sendMail(CuentaBancariaView cuentaBancariaView, List<EstadocuentaBancariaView> list, List<String> emails, Date desde, Date hasta) {
		mailMessage = "Buen dia, el siguiente estado de cuenta corresponde a";
		if(desde == null || hasta == null)
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
			writePdf(outputStream, list, cuentaBancariaView);
			byte[] bytes = outputStream.toByteArray();

			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName(fileName);

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
