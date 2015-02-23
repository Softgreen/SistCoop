package org.sistemafinanciero.mail;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

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

		document.addTitle("Test PDF");
		document.addSubject("Testing email PDF");
		document.addKeywords("iText, email");
		document.addAuthor("Jee Vang");
		document.addCreator("Jee Vang");

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
		
		
		Paragraph paragraph4 = new Paragraph();
		paragraph4.setFont(font);
		Chunk socio1 = new Chunk("SOCIO:");
		Chunk socio2 = new Chunk(cuentaBancaria.getSocio());
		Chunk codigoSocio1 = new Chunk("CODIGO SOCIO:");
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
		
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Chunk("hello!"));
		document.add(paragraph);

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
