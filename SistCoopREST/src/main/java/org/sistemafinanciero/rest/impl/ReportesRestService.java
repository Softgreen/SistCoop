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

            Chunk titulo = new Chunk("MULTIVALORES DEL SUR");
            Font fuenteTitulo = new Font(FontFamily.UNDEFINED, 12, Font.BOLD);
            titulo.setFont(fuenteTitulo);
            parrafoPrincipal.add(titulo);

            Font fuenteSubTitulo = new Font(FontFamily.UNDEFINED, 10, Font.BOLD);
            Chunk subtitulo1 = new Chunk("Reporte debe haber");
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
            parrafoSecundario2.add(subtitulo2);

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

        Font fontTableCabecera = new Font(FontFamily.UNDEFINED, 10, Font.BOLD);
        Font fontCuerpo = new Font(FontFamily.UNDEFINED, 8, Font.NORMAL);

        // row
        float[] columnWidthsROW = { 4f, 4f };
        PdfPTable row = new PdfPTable(columnWidthsROW);
        row.setWidthPercentage(100);
        row.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        float[] columnWidthsCOL6Left = { 1.5f, 8f, 4.5f, 3.5f, 3.5f, 3.5f };
        PdfPTable tableLeft = new PdfPTable(columnWidthsCOL6Left);
        tableLeft.setWidthPercentage(100);

        float[] columnWidthsCOL6Right = { 1.5f, 8f, 4.5f, 3.5f, 3.5f, 3.5f };
        PdfPTable tableRight = new PdfPTable(columnWidthsCOL6Right);
        tableRight.setWidthPercentage(100);

        setRowsTableDebeHaber(listDebe, tableLeft, fontCuerpo);
        setRowsTableDebeHaber(listHaber, tableRight, fontCuerpo);

        row.addCell(tableLeft);
        row.addCell(tableRight);

        try {
            Paragraph parrafoDebeHaber = new Paragraph();
            parrafoDebeHaber.setAlignment(Element.ALIGN_LEFT);

            Chunk chunkDebe = new Chunk(" DEBE", fontTableCabecera);
            parrafoDebeHaber.add(chunkDebe);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            parrafoDebeHaber.add(Chunk.TABBING);
            Chunk chunkHaber = new Chunk("          HABER", fontTableCabecera);
            parrafoDebeHaber.add(chunkHaber);

            document.add(parrafoDebeHaber);
            document.add(row);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();

        return Response.ok(outputStream.toByteArray()).type("application/pdf").build();
    }

    private void setRowsTableDebeHaber(List<DebeHaber> list, PdfPTable table, Font font) {
        /** obteniendo la moneda y dando formato **/
        NumberFormat df1 = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setGroupingSeparator(',');
        dfs.setMonetaryDecimalSeparator('.');
        ((DecimalFormat) df1).setDecimalFormatSymbols(dfs);

        table.setWidthPercentage(100);
        table.addCell(new PdfPCell(new Paragraph("Nro", font)));
        // table.addCell(new PdfPCell(new Paragraph("DOCUMENTO", font)));
        table.addCell(new PdfPCell(new Paragraph("PERSONA", font)));
        // table.addCell(new PdfPCell(new Paragraph("TIPO", font)));
        table.addCell(new PdfPCell(new Paragraph("CUENTA", font)));
        table.addCell(new PdfPCell(new Paragraph("SOLES", font)));
        table.addCell(new PdfPCell(new Paragraph("DOLARES", font)));
        table.addCell(new PdfPCell(new Paragraph("EUROS", font)));

        if (list.isEmpty()) {
            PdfPCell cell = new PdfPCell(new Paragraph("No existente", font));
            cell.setColspan(8);
            table.addCell(cell);
        }

        BigDecimal totalSoles = BigDecimal.ZERO;
        BigDecimal totalDolares = BigDecimal.ZERO;
        BigDecimal totalEuros = BigDecimal.ZERO;
        for (int i = 0; i < list.size(); i++) {
            DebeHaber debeHaber = list.get(i);

            table.addCell(new PdfPCell(new Paragraph(String.valueOf(i + 1), font)));
            /*
             * table.addCell(new Paragraph(debeHaber.getTipoDocumento() + ":" +
             * debeHaber.getNumeroDocumento(), font));
             */
            table.addCell(new Paragraph(debeHaber.getPersona(), font));

            // table.addCell(new Paragraph("NO DEFINIDO", font));
            table.addCell(new Paragraph(debeHaber.getNumeroCuenta(), font));

            if (debeHaber.getSimboloMoneda().equalsIgnoreCase("S/.")) {
                totalSoles = totalSoles.add(debeHaber.getMonto().abs());

                PdfPCell uno = new PdfPCell(new Paragraph("S/." + df1.format(debeHaber.getMonto()), font));
                uno.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell dos = new PdfPCell(new Paragraph("$" + df1.format(BigDecimal.ZERO), font));
                dos.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell tres = new PdfPCell(new Paragraph("€" + df1.format(BigDecimal.ZERO), font));
                tres.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(new PdfPCell(uno));
                table.addCell(new PdfPCell(dos));
                table.addCell(new PdfPCell(tres));
            } else if (debeHaber.getSimboloMoneda().equalsIgnoreCase("$")) {
                totalDolares = totalDolares.add(debeHaber.getMonto().abs());

                PdfPCell uno = new PdfPCell(new Paragraph("S/." + df1.format(BigDecimal.ZERO), font));
                uno.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell dos = new PdfPCell(new Paragraph("$" + df1.format(debeHaber.getMonto()), font));
                dos.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell tres = new PdfPCell(new Paragraph("€" + df1.format(BigDecimal.ZERO), font));
                tres.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(new PdfPCell(uno));
                table.addCell(new PdfPCell(dos));
                table.addCell(new PdfPCell(tres));
            } else if (debeHaber.getSimboloMoneda().equalsIgnoreCase("€")) {
                totalEuros = totalEuros.add(debeHaber.getMonto().abs());

                PdfPCell uno = new PdfPCell(new Paragraph("S/." + df1.format(BigDecimal.ZERO), font));
                uno.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell dos = new PdfPCell(new Paragraph("$" + df1.format(BigDecimal.ZERO), font));
                dos.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PdfPCell tres = new PdfPCell(new Paragraph("€" + df1.format(debeHaber.getMonto()), font));
                tres.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(new PdfPCell(uno));
                table.addCell(new PdfPCell(dos));
                table.addCell(new PdfPCell(tres));
            }
        }

        PdfPCell total = new PdfPCell(new Paragraph("TOTAL", font));
        total.setColspan(3);
        table.addCell(total);

        PdfPCell totalCellSoles = new PdfPCell(new Paragraph("S/." + df1.format(totalSoles), font));
        totalCellSoles.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(new PdfPCell(totalCellSoles));

        PdfPCell totalCellDolares = new PdfPCell(new Paragraph("$" + df1.format(totalDolares), font));
        totalCellDolares.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(new PdfPCell(totalCellDolares));

        PdfPCell totalCellEuros = new PdfPCell(new Paragraph("€" + df1.format(totalEuros), font));
        totalCellEuros.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(new PdfPCell(totalCellEuros));
    }
}
