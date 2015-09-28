package org.sistemafinanciero.mail;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
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
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.service.nt.MonedaServiceNT;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Stateless
@LocalBean
public class EmailSessionBean {

    @EJB
    private MonedaServiceNT monedaServiceNT;

    private int port = 587;
    private String host = "smtp.gmail.com";
    // private String from = "coop.caja.ventura@gmail.com";
    private String from = "multivaloresdelsursac@gmail.com";
    private boolean auth = true;
    // private String username = "coop.caja.ventura@gmail.com";
    private String username = "multivaloresdelsursac@gmail.com";
    // private String password = "caja.ventura";
    private String password = "jjjbryan777";
    private Protocol protocol = Protocol.SMTP;
    private boolean debug = true;

    private String mailMessage = "";
    private String fileName = "estado_cuenta";
    private String subject = "EE.CC. ";

    public void writePdf(OutputStream outputStream, List<EstadocuentaBancariaView> list,
            CuentaBancariaView cuentaBancaria, Date desde, Date hasta) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        // Caso de que no existan transacciones
        if (list.isEmpty()) {
            document.open();
            document.addTitle("Estado de Cuenta");
            document.addSubject("Estado de Cuenta");
            document.addKeywords("email");
            document.addAuthor("Multivalores del Sur");
            document.addCreator("Multivalores del Sur");

            Paragraph saltoDeLinea = new Paragraph();
            document.add(saltoDeLinea);

            // ******************* TITULO ******************           
            
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

                if (cuentaBancaria.getTipoPersona() == TipoPersona.NATURAL) {
                    Chunk clientePNNombres = new Chunk("CLIENTE       : " + cuentaBancaria.getSocio() + "\n");
                    Chunk clientePNDni = new Chunk(cuentaBancaria.getTipoDocumento() + "                : "
                            + cuentaBancaria.getNumeroDocumento() + "\n");
                    Chunk clientePNFecha = new Chunk("FECHA          : " + fechaActual + "\n\n");
                    Chunk tipoCuentaPN = new Chunk("CUENTA " + cuentaBancaria.getTipoCuenta() + " Nº "
                            + cuentaBancaria.getNumeroCuenta() + "\n");
                    Chunk tipoMonedaPN;

                    if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ZERO) == 0) {
                        tipoMonedaPN = new Chunk("MONEDA: " + "DOLARES AMERICANOS" + "\n");
                    } else if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ONE) == 0) {
                        tipoMonedaPN = new Chunk("MONEDA: " + "NUEVOS SOLES" + "\n");
                    } else {
                        tipoMonedaPN = new Chunk("MONEDA: " + "EUROS" + "\n");
                    }

                    clientePNNombres.setFont(fuenteDatosCliente);
                    clientePNDni.setFont(fuenteDatosCliente);
                    clientePNFecha.setFont(fuenteDatosCliente);
                    tipoCuentaPN.setFont(fuenteDatosCliente);
                    tipoMonedaPN.setFont(fuenteDatosCliente);

                    parrafoSecundario.add(clientePNNombres);
                    parrafoSecundario.add(clientePNDni);
                    parrafoSecundario.add(clientePNFecha);
                    parrafoSecundario.add(tipoCuentaPN);
                    parrafoSecundario.add(tipoMonedaPN);

                } else {
                    Chunk clientePJNombre = new Chunk("CLIENTE       : " + cuentaBancaria.getSocio() + "\n");
                    Chunk clientePJRuc = new Chunk(cuentaBancaria.getTipoDocumento() + "               : "
                            + cuentaBancaria.getNumeroDocumento() + "\n");
                    Chunk clientePJFecha = new Chunk("FECHA          : " + fechaActual + "\n\n");
                    Chunk tipoCuentaPJ = new Chunk("CUENTA " + cuentaBancaria.getTipoCuenta() + " Nº "
                            + cuentaBancaria.getNumeroCuenta() + "\n");
                    Chunk tipoMonedaPJ;

                    if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ZERO) == 0) {
                        tipoMonedaPJ = new Chunk("MONEDA: " + "DOLARES AMERICANOS" + "\n");
                    } else if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ONE) == 0) {
                        tipoMonedaPJ = new Chunk("MONEDA: " + "NUEVOS SOLES" + "\n");
                    } else {
                        tipoMonedaPJ = new Chunk("MONEDA: " + "EUROS" + "\n");
                    }

                    clientePJNombre.setFont(fuenteDatosCliente);
                    clientePJRuc.setFont(fuenteDatosCliente);
                    clientePJFecha.setFont(fuenteDatosCliente);
                    tipoCuentaPJ.setFont(fuenteDatosCliente);
                    tipoMonedaPJ.setFont(fuenteDatosCliente);

                    parrafoSecundario.add(clientePJNombre);
                    parrafoSecundario.add(clientePJRuc);
                    parrafoSecundario.add(clientePJFecha);
                    parrafoSecundario.add(tipoCuentaPJ);
                    parrafoSecundario.add(tipoMonedaPJ);
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

            Paragraph message = new Paragraph();
            message.setAlignment(Element.ALIGN_JUSTIFIED);
            Chunk textoMessaje = new Chunk("No existen transacciones en este periodo de tiempo...");
            message.add(textoMessaje);

            try {
                document.add(message);
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            document.close();
            return;

            // Caso de que existan transacciones
        } else {

            // Dando formato a las fechas desde hasta
            if(desde == null && hasta == null){
                desde = list.get(0).getFecha();
                hasta = list.get(list.size() - 1).getFecha();
            }           
            SimpleDateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
            String fechaDesde = fechaFormato.format(desde);
            String fechaHasta = fechaFormato.format(hasta);

            // obteniendo la moneda y dando formato
            Moneda moneda = monedaServiceNT.findById(cuentaBancaria.getIdMoneda());
            NumberFormat df1 = NumberFormat.getCurrencyInstance();
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setCurrencySymbol("");
            dfs.setGroupingSeparator(',');
            dfs.setMonetaryDecimalSeparator('.');
            ((DecimalFormat) df1).setDecimalFormatSymbols(dfs);

            document.open();

            document.addTitle("Estado de Cuenta");
            document.addSubject("Estado de Cuenta");
            document.addKeywords("email");
            document.addAuthor("Multivalores del Sur");
            document.addCreator("Multivalores del Sur");

            Paragraph saltoDeLinea = new Paragraph();
            document.add(saltoDeLinea);

            // ******************* TITULO ******************
            try {
                // Image img =
                // Image.getInstance("/images/logo_coop_contrato.png");
                Image img = Image
                        .getInstance("//usr//share//jboss//archivos//logoCartilla//logo_coop_contrato.png");
                img.setAlignment(Image.LEFT | Image.UNDERLYING);
                document.add(img);

                Paragraph parrafoPrincipal = new Paragraph();
                parrafoPrincipal.setSpacingAfter(30);
                // parrafoPrincipal.setSpacingBefore(50);
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

                if (cuentaBancaria.getTipoPersona() == TipoPersona.NATURAL) {
                    Chunk clientePNNombres = new Chunk("CLIENTE       : " + cuentaBancaria.getSocio() + "\n");
                    Chunk clientePNDni = new Chunk(cuentaBancaria.getTipoDocumento() + "                : "
                            + cuentaBancaria.getNumeroDocumento() + "\n");
                    // Chunk clientePNTitulares = new Chunk("TITULAR(ES): " +
                    // cuentaBancariaView.getTitulares() + "\n");
                    Chunk clientePNFecha = new Chunk("FECHA          : " + fechaActual + "\n\n");

                    Chunk tipoCuentaPN = new Chunk("CUENTA " + cuentaBancaria.getTipoCuenta() + " Nº "
                            + cuentaBancaria.getNumeroCuenta() + "\n");
                    Chunk tipoMonedaPN;

                    if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ZERO) == 0) {
                        tipoMonedaPN = new Chunk("MONEDA: " + "DOLARES AMERICANOS" + "\n");
                    } else if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ONE) == 0) {
                        tipoMonedaPN = new Chunk("MONEDA: " + "NUEVOS SOLES" + "\n");
                    } else {
                        tipoMonedaPN = new Chunk("MONEDA: " + "EUROS" + "\n");
                    }

                    Chunk fechaEstadoCuenta = new Chunk("ESTADO DE CUENTA DEL " + fechaDesde + " AL "
                            + fechaHasta);
                    // obteniedo titulares
                    /*
                     * String tPN = cuentaBancariaView.getTitulares(); String[]
                     * arrayTitulares = tPN.split(","); Chunk clientePNTitulares
                     * = new Chunk("Titular(es):"); for (int i = 0; i <
                     * arrayTitulares.length; i++) { String string =
                     * arrayTitulares[i]; }
                     */

                    clientePNNombres.setFont(fuenteDatosCliente);
                    clientePNDni.setFont(fuenteDatosCliente);
                    // clientePNTitulares.setFont(fuenteDatosCliente);
                    clientePNFecha.setFont(fuenteDatosCliente);
                    tipoCuentaPN.setFont(fuenteDatosCliente);
                    tipoMonedaPN.setFont(fuenteDatosCliente);
                    fechaEstadoCuenta.setFont(fuenteDatosCliente);

                    parrafoSecundario.add(clientePNNombres);
                    parrafoSecundario.add(clientePNDni);
                    // parrafoSecundario.add(clientePNTitulares);
                    parrafoSecundario.add(clientePNFecha);
                    parrafoSecundario.add(tipoCuentaPN);
                    parrafoSecundario.add(tipoMonedaPN);
                    parrafoSecundario.add(fechaEstadoCuenta);

                } else {
                    Chunk clientePJNombre = new Chunk("CLIENTE       : " + cuentaBancaria.getSocio() + "\n");
                    Chunk clientePJRuc = new Chunk(cuentaBancaria.getTipoDocumento() + "               : "
                            + cuentaBancaria.getNumeroDocumento() + "\n");
                    // Chunk clientePJTitulares = new Chunk("TITULAR(ES): " +
                    // cuentaBancariaView.getTitulares() + "\n");
                    Chunk clientePJFecha = new Chunk("FECHA          : " + fechaActual + "\n\n");

                    Chunk tipoCuentaPJ = new Chunk("CUENTA " + cuentaBancaria.getTipoCuenta() + " Nº "
                            + cuentaBancaria.getNumeroCuenta() + "\n");
                    Chunk tipoMonedaPJ;

                    if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ZERO) == 0) {
                        tipoMonedaPJ = new Chunk("MONEDA: " + "DOLARES AMERICANOS" + "\n");
                    } else if (cuentaBancaria.getIdMoneda().compareTo(BigInteger.ONE) == 0) {
                        tipoMonedaPJ = new Chunk("MONEDA: " + "NUEVOS SOLES" + "\n");
                    } else {
                        tipoMonedaPJ = new Chunk("MONEDA: " + "EUROS" + "\n");
                    }

                    Chunk fechaEstadoCuenta = new Chunk("ESTADO DE CUENTA DEL " + fechaDesde + " AL "
                            + fechaHasta);
                    // obteniedo titulares
                    /*
                     * String tPN = cuentaBancariaView.getTitulares(); String[]
                     * arrayTitulares = tPN.split(","); Chunk clientePNTitulares
                     * = new Chunk("Titular(es):"); for (int i = 0; i <
                     * arrayTitulares.length; i++) { String string =
                     * arrayTitulares[i]; }
                     */

                    clientePJNombre.setFont(fuenteDatosCliente);
                    clientePJRuc.setFont(fuenteDatosCliente);
                    // clientePJTitulares.setFont(fuenteDatosCliente);
                    clientePJFecha.setFont(fuenteDatosCliente);
                    tipoCuentaPJ.setFont(fuenteDatosCliente);
                    tipoMonedaPJ.setFont(fuenteDatosCliente);
                    fechaEstadoCuenta.setFont(fuenteDatosCliente);

                    parrafoSecundario.add(clientePJNombre);
                    parrafoSecundario.add(clientePJRuc);
                    // parrafoSecundario.add(clientePJTitulares);
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

            // float[] columnWidths = { 5f, 4f, 2.8f, 10f, 3.5f, 4f, 2.8f};
            float[] columnWidths = { 5f, 4f, 2.8f, 10f, 3.5f, 4f };
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            PdfPCell cellFechaHoraCabecera = new PdfPCell(new Paragraph("FECHA Y HORA", fontTableCabecera));
            PdfPCell cellTransaccionCabecera = new PdfPCell(new Paragraph("TIPO TRANS.", fontTableCabecera));
            PdfPCell cellOperacionCabecera = new PdfPCell(new Paragraph("NUM. OP.", fontTableCabecera));
            PdfPCell cellReferenciaCabecera = new PdfPCell(new Paragraph("REFERENCIA", fontTableCabecera));
            PdfPCell cellMontoCabecera = new PdfPCell(new Paragraph("MONTO", fontTableCabecera));
            PdfPCell cellSaldoDisponibleCabecera = new PdfPCell(
                    new Paragraph("DISPONIBLE", fontTableCabecera));
            // PdfPCell cellEstado = new PdfPCell(new Paragraph("ESTADO",
            // fontTableCabecera));

            table.addCell(cellFechaHoraCabecera);
            table.addCell(cellTransaccionCabecera);
            table.addCell(cellOperacionCabecera);
            table.addCell(cellReferenciaCabecera);
            table.addCell(cellMontoCabecera);
            table.addCell(cellSaldoDisponibleCabecera);
            // table.addCell(cellEstado);

            for (EstadocuentaBancariaView estadocuentaBancariaView : list) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String fecHoraFormat = sdf.format(estadocuentaBancariaView.getHora());

                if (estadocuentaBancariaView.getEstado()) {
                    PdfPCell cellFechaHora = new PdfPCell(new Paragraph(fecHoraFormat, fontTableCuerpo));
                    table.addCell(cellFechaHora);
                    PdfPCell cellTipoTrasaccion = new PdfPCell(new Paragraph(
                            estadocuentaBancariaView.getTipoTransaccionTransferencia(), fontTableCuerpo));
                    table.addCell(cellTipoTrasaccion);
                    PdfPCell cellNumOperacion = new PdfPCell(new Paragraph(estadocuentaBancariaView
                            .getNumeroOperacion().toString(), fontTableCuerpo));
                    table.addCell(cellNumOperacion);
                    PdfPCell cellReferencia = new PdfPCell(new Paragraph(
                            estadocuentaBancariaView.getReferencia(), fontTableCuerpo));
                    table.addCell(cellReferencia);
                    PdfPCell cellMonto = new PdfPCell(new Paragraph(df1.format(estadocuentaBancariaView
                            .getMonto()), fontTableCuerpo));
                    cellMonto.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cellMonto);
                    PdfPCell cellSaldoDisponible = new PdfPCell(new Paragraph(
                            df1.format(estadocuentaBancariaView.getSaldoDisponible()), fontTableCuerpo));
                    cellSaldoDisponible.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cellSaldoDisponible);
                    /*
                     * if (estadocuentaBancariaView.getEstado()) { PdfPCell
                     * cellEstadoActivo = new PdfPCell(new Paragraph("Activo",
                     * fontTableCuerpo)); table.addCell(cellEstadoActivo); }
                     * else { PdfPCell cellEstadoExtornado = new PdfPCell(new
                     * Paragraph("Extornado", fontTableCuerpo));
                     * table.addCell(cellEstadoExtornado); }
                     */
                }
            }

            Paragraph saldoDisponible = new Paragraph();
            saldoDisponible.setAlignment(Element.ALIGN_CENTER);
            Chunk textoSaldoDisponible = new Chunk("SALDO DISPONIBLE: " + moneda.getSimbolo()
                    + df1.format(cuentaBancaria.getSaldo()), fontTableCabecera);
            textoSaldoDisponible.setFont(fontTableCabecera);
            saldoDisponible.add(textoSaldoDisponible);

            try {
                document.add(table);
                document.add(saldoDisponible);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.close();
        }
    }

    public void writeExcel(OutputStream outputStream, List<EstadocuentaBancariaView> list,
            CuentaBancariaView cuentaBancaria) throws Exception {

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
        Cell cellEstadoCabecera = row3.createCell(6);
        cellEstadoCabecera.setCellValue("ESTADO");
        cellEstadoCabecera.setCellStyle(styleBold);

        for (int i = 0; i < list.size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String fechaString = sdf.format(list.get(i).getFecha());

            sdf = new SimpleDateFormat("HH:mm:ss");
            String horaString = sdf.format(list.get(i).getHora());

            /***** EXCEL *****/
            Row row = sheet.createRow(i + 3);

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

            Cell cellEstado = row.createCell(6);
            cellEstado.setCellValue(list.get(i).getEstado() ? "" : "EXTORNADO");
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
    public void sendMailPdf(CuentaBancariaView cuentaBancariaView, List<EstadocuentaBancariaView> list,
            List<String> emails, Date desde, Date hasta) {
        mailMessage = "Buen día, el siguiente estado de cuenta corresponde";

        // dando formato a las fechas
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String fechaDesde = df.format(desde);
        String fechaHasta = df.format(hasta);

        if (desde == null || hasta == null)
            mailMessage = mailMessage + " los ultimos 30 dias.";
        else
            mailMessage = mailMessage + " al perido desde: " + fechaDesde + " hasta: " + fechaHasta;

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

            // enviando para crear el archivo a enviar
            writePdf(outputStream, list, cuentaBancariaView, desde, hasta);

            byte[] bytes = outputStream.toByteArray();

            // construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(fileName + ".pdf");

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

                SimpleDateFormat format = new SimpleDateFormat("MMMM 'del' yyyy", new Locale("es", "ES"));
                Date fecha = Calendar.getInstance().getTime();
                mimeMessage.setSubject(subject + format.format(fecha));

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
    public void sendMailExcel(CuentaBancariaView cuentaBancariaView, List<EstadocuentaBancariaView> list,
            List<String> emails, Date desde, Date hasta) {
        mailMessage = "Buen dia, el siguiente estado de cuenta corresponde";

        // dando formato a las fechas
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String fechaDesde = df.format(desde);
        String fechaHasta = df.format(hasta);

        if (desde == null || hasta == null)
            mailMessage = mailMessage + " los ultimos 30 dias.";
        else
            mailMessage = mailMessage + " al perido desde: " + fechaDesde + " hasta: " + fechaHasta;

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

            // enviando para crear el archivo a enviar
            writeExcel(outputStream, list, cuentaBancariaView);

            byte[] bytes = outputStream.toByteArray();

            // construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(bytes, "application/vnd.ms-excel");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName(fileName + ".xls");

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
