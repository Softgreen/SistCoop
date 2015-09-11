package org.sistemafinanciero.rest.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.DebeHaber;
import org.sistemafinanciero.entity.type.TipoDebeHaber;
import org.sistemafinanciero.rest.ReportesRest;
import org.sistemafinanciero.service.nt.ReportesServiceNT;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportesRestService implements ReportesRest {

    @EJB
    private ReportesServiceNT reportesServiceNT;

    @Override
    public Response reporteDebeHaber(Long fecha, TipoDebeHaber tipoDebeHaber, BigInteger idMoneda) {
        Date fechaReporte;
        if (fecha == null) {
            fechaReporte = Calendar.getInstance().getTime();
        } else {
            fechaReporte = new Date(fecha);
        }
        List<DebeHaber> list = reportesServiceNT.getDebeHaber(fechaReporte, idMoneda, tipoDebeHaber);
        Response response = Response.status(Response.Status.OK).entity(list).build();
        return response;
    }

    @Override
    public Response reporteDebeHaberPdf(Long fecha) {
        Date fechaReporte;
        if (fecha == null) {
            fechaReporte = Calendar.getInstance().getTime();
        } else {
            fechaReporte = new Date(fecha);
        }
        List<DebeHaber> listDebe = reportesServiceNT.getDebeHaber(fechaReporte, TipoDebeHaber.DEBE);
        List<DebeHaber> listHaber = reportesServiceNT.getDebeHaber(fechaReporte, TipoDebeHaber.HABER);

        /** obteniendo la moneda y dando formato **/
        NumberFormat df1 = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setGroupingSeparator(',');
        dfs.setMonetaryDecimalSeparator('.');
        ((DecimalFormat) df1).setDecimalFormatSymbols(dfs);

        /** PDF **/
        ByteArrayOutputStream outputStream = null;
        outputStream = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate());

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            document.addTitle("MULTISERVICIOS DEL SUR");
            document.addSubject("Estado contable DEBE HABER");
            document.addKeywords("pdf");
            document.addAuthor("MULTISERVICIOS DEL SUR");
            document.addCreator("MULTISERVICIOS DEL SUR");

            // Saldo de linea
            document.add(new Paragraph());
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        /******************* TITULO ******************/
        try {
            Image img = Image
                    .getInstance("//usr//share//jboss//archivos//logoCartilla//logo_coop_contrato.png");
            img.setAlignment(Image.LEFT | Image.UNDERLYING);
            document.add(img);

            Paragraph parrafoPrincipal = new Paragraph();
            parrafoPrincipal.setSpacingAfter(30);
            parrafoPrincipal.setAlignment(Element.ALIGN_CENTER);
            parrafoPrincipal.setIndentationLeft(100);
            parrafoPrincipal.setIndentationRight(50);

            Paragraph parrafoSecundario1 = new Paragraph();
            parrafoSecundario1.setSpacingAfter(20);
            parrafoSecundario1.setSpacingBefore(-20);
            parrafoSecundario1.setAlignment(Element.ALIGN_LEFT);
            parrafoSecundario1.setIndentationLeft(160);
            parrafoSecundario1.setIndentationRight(10);

            Chunk titulo = new Chunk("MULTISERVICIOS DEL SUR");
            Font fuenteTitulo = new Font(FontFamily.UNDEFINED, 12, Font.BOLD);
            titulo.setFont(fuenteTitulo);
            parrafoPrincipal.add(titulo);

            Font fuenteSubTitulo = new Font(FontFamily.UNDEFINED, 10, Font.BOLD);
            Chunk subtitulo1 = new Chunk("Reporte DEBE HABER");
            subtitulo1.setFont(fuenteSubTitulo);
            parrafoSecundario1.add(subtitulo1);

            Paragraph parrafoSecundario2 = new Paragraph();
            parrafoSecundario2.setSpacingAfter(20);
            parrafoSecundario2.setSpacingBefore(-20);
            parrafoSecundario2.setAlignment(Element.ALIGN_LEFT);
            parrafoSecundario2.setIndentationLeft(160);
            parrafoSecundario2.setIndentationRight(10);

            Chunk subtitulo2 = new Chunk("Fecha:" + df.format(fechaReporte));
            subtitulo2.setFont(fuenteSubTitulo);
            parrafoSecundario1.add(subtitulo2);

            document.add(parrafoPrincipal);
            document.add(parrafoSecundario1);
            document.add(parrafoSecundario2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Font fontTableCabecera = new Font(FontFamily.UNDEFINED, 8, Font.BOLD);
        Font fontCuerpo = new Font(FontFamily.UNDEFINED, 8, Font.NORMAL);

        // row
        float[] columnWidthsROW = { 4f, 4f };
        PdfPTable row = new PdfPTable(columnWidthsROW);
        row.setWidthPercentage(100);
        row.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        float[] columnWidthsCOL6Left = { 2f, 5f, 5f, 10f, 3.5f, 4f, 2.8f, 2.8f, 2.8f };
        PdfPTable tableLeft = new PdfPTable(columnWidthsCOL6Left);
        tableLeft.setWidthPercentage(100);
        tableLeft.addCell(new PdfPCell(new Paragraph("Nro", fontTableCabecera)));
        tableLeft.addCell(new PdfPCell(new Paragraph("DOCUMENTO", fontTableCabecera)));
        tableLeft.addCell(new PdfPCell(new Paragraph("NUMERO", fontTableCabecera)));
        tableLeft.addCell(new PdfPCell(new Paragraph("PERSONA", fontTableCabecera)));
        tableLeft.addCell(new PdfPCell(new Paragraph("TIPO", fontTableCabecera)));
        tableLeft.addCell(new PdfPCell(new Paragraph("CUENTA", fontTableCabecera)));
        tableLeft.addCell(new PdfPCell(new Paragraph("SOLES", fontTableCabecera)));
        tableLeft.addCell(new PdfPCell(new Paragraph("DOLARES", fontTableCabecera)));
        tableLeft.addCell(new PdfPCell(new Paragraph("EUROS", fontTableCabecera)));

        float[] columnWidthsCOL6Right = { 2f, 5f, 5f, 10f, 3.5f, 4f, 2.8f, 2.8f, 2.8f };
        PdfPTable tableRight = new PdfPTable(columnWidthsCOL6Right);
        tableRight.setWidthPercentage(100);
        tableRight.addCell(new PdfPCell(new Paragraph("Nro", fontTableCabecera)));
        tableRight.addCell(new PdfPCell(new Paragraph("DOCUMENTO", fontTableCabecera)));
        tableRight.addCell(new PdfPCell(new Paragraph("NUMERO", fontTableCabecera)));
        tableRight.addCell(new PdfPCell(new Paragraph("PERSONA", fontTableCabecera)));
        tableRight.addCell(new PdfPCell(new Paragraph("TIPO", fontTableCabecera)));
        tableRight.addCell(new PdfPCell(new Paragraph("CUENTA", fontTableCabecera)));
        tableRight.addCell(new PdfPCell(new Paragraph("SOLES", fontTableCabecera)));
        tableRight.addCell(new PdfPCell(new Paragraph("DOLARES", fontTableCabecera)));
        tableRight.addCell(new PdfPCell(new Paragraph("EUROS", fontTableCabecera)));

        for (int i = 0; i < listDebe.size(); i++) {
            DebeHaber debeHaber = listDebe.get(i);
            tableLeft.addCell(new PdfPCell(new Paragraph(String.valueOf(i + 1), fontCuerpo)));
            tableLeft.addCell(new Paragraph(debeHaber.getTipoDocumento(), fontCuerpo));
            tableLeft.addCell(new Paragraph(debeHaber.getNumeroDocumento(), fontCuerpo));
            tableLeft.addCell(new Paragraph(debeHaber.getPersona(), fontCuerpo));

            tableLeft.addCell(new Paragraph("NO DEFINIDO", fontCuerpo));
            tableLeft.addCell(new Paragraph(debeHaber.getNumeroCuenta(), fontCuerpo));

            if (debeHaber.getSimboloMoneda().equalsIgnoreCase("S/.")) {
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(debeHaber.getMonto()), fontCuerpo)));
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
            } else if (debeHaber.getSimboloMoneda().equalsIgnoreCase("$")) {
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(debeHaber.getMonto()), fontCuerpo)));
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
            } else if (debeHaber.getSimboloMoneda().equalsIgnoreCase("€")) {
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
                tableLeft.addCell(new PdfPCell(new Paragraph(df1.format(debeHaber.getMonto()), fontCuerpo)));
            }
        }

        for (int i = 0; i < listHaber.size(); i++) {
            DebeHaber debeHaber = listDebe.get(i);
            tableRight.addCell(new PdfPCell(new Paragraph(String.valueOf(i + 1), fontCuerpo)));
            tableRight.addCell(new Paragraph(debeHaber.getTipoDocumento(), fontCuerpo));
            tableRight.addCell(new Paragraph(debeHaber.getNumeroDocumento(), fontCuerpo));
            tableRight.addCell(new Paragraph(debeHaber.getPersona(), fontCuerpo));

            tableRight.addCell(new Paragraph("NO DEFINIDO", fontCuerpo));
            tableRight.addCell(new Paragraph(debeHaber.getNumeroCuenta(), fontCuerpo));

            if (debeHaber.getSimboloMoneda().equalsIgnoreCase("S/.")) {
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(debeHaber.getMonto()), fontCuerpo)));
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
            } else if (debeHaber.getSimboloMoneda().equalsIgnoreCase("$")) {
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(debeHaber.getMonto()), fontCuerpo)));
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
            } else if (debeHaber.getSimboloMoneda().equalsIgnoreCase("€")) {
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(BigDecimal.ZERO), fontCuerpo)));
                tableRight.addCell(new PdfPCell(new Paragraph(df1.format(debeHaber.getMonto()), fontCuerpo)));
            }
        }

        PdfPCell debe = new PdfPCell(new Paragraph("DEBE", new Font(FontFamily.UNDEFINED, 9, Font.BOLD)));
        debe.setPaddingRight(0.5f);
        PdfPCell haber = new PdfPCell(new Paragraph("HABER", new Font(FontFamily.UNDEFINED, 9, Font.BOLD)));
        haber.setPaddingLeft(0.5f);

        row.addCell(debe);
        row.addCell(debe);
        row.addCell(tableLeft);
        row.addCell(tableRight);

        Paragraph saldoDisponible = new Paragraph();
        saldoDisponible.setAlignment(Element.ALIGN_CENTER);
        Chunk textoSaldoDisponible = new Chunk("TOTAL: S/." + df1.format(BigDecimal.ZERO), fontTableCabecera);
        textoSaldoDisponible.setFont(fontTableCabecera);
        saldoDisponible.add(textoSaldoDisponible);

        try {
            document.add(row);
            document.add(saldoDisponible);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();

        return Response.ok(outputStream.toByteArray()).type("application/pdf").build();
    }
}
