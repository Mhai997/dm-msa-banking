package com.pichincha.dm.msa.banking.util;

import com.pichincha.dm.msa.banking.domain.Client;
import com.pichincha.dm.msa.banking.domain.enums.MovementType;
import com.pichincha.dm.msa.banking.service.dto.ReportAccountDto;
import com.pichincha.dm.msa.banking.service.dto.ReportMovementLineDto;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

@Component
public class PdfReportGenerator {

    private static final PDType1Font FONT = PDType1Font.HELVETICA;
    private static final PDType1Font FONT_BOLD = PDType1Font.HELVETICA_BOLD;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat MONEY_FMT = new DecimalFormat("#,##0.00");

    private static final float MARGIN = 30f;
    private static final float HEADER_H = 70f;

    // Tabla (ajustada a Letter con márgenes)
    private static final float ROW_H = 18f;

    private static final float COL_DATE = 60f;
    private static final float COL_OFFICE = 60f;
    private static final float COL_DOC = 85f;
    private static final float COL_DESC = 175f;
    private static final float COL_DEBIT = 55f;
    private static final float COL_CREDIT = 55f;
    private static final float COL_BAL = 55f;

    public String generateAccountStatementBase64(
            Client client,
            LocalDate from,
            LocalDate to,
            List<ReportAccountDto> accounts
    ) {
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            for (int i = 0; i < accounts.size(); i++) {
                ReportAccountDto acc = accounts.get(i);
                writeAccountSection(doc, client, from, to, acc);
            }

            doc.save(baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            // Si falla el PDF, no rompas el endpoint
            return Base64.getEncoder().encodeToString(("PDF generation error: " + e.getMessage()).getBytes());
        }
    }

    private void writeAccountSection(
            PDDocument doc,
            Client client,
            LocalDate from,
            LocalDate to,
            ReportAccountDto account
    ) throws Exception {

        PageCtx ctx = newPage(doc);
        float y = ctx.y;

        // Header + meta
        drawHeader(ctx, "DM BANKING"); // <-- pon tu “marca” genérica
        y = drawMeta(ctx, y, from, to, account.accountNumber());

        // Título
        y -= 18;
        y = centerText(ctx, "Detalle de movimientos", y, 12, true);
        y -= 10;

        // Tabla header
        y = drawTableHeader(ctx, y);

        // Rows
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (int idx = 0; idx < account.movements().size(); idx++) {

            ReportMovementLineDto m = account.movements().get(idx);

            // Paginación
            if (y < 80) {
                ctx.close();
                ctx = newPage(doc);
                y = ctx.y;

                drawHeader(ctx, "DM BANKING");
                y = drawMeta(ctx, y, from, to, account.accountNumber());
                y -= 18;
                y = centerText(ctx, "Detalle de movimientos", y, 12, true);
                y -= 10;
                y = drawTableHeader(ctx, y);
            }

            // Mapeo “tipo banco”
            String date = m.movementDate().toLocalDate().format(DATE_FMT);
            String office = "ONLINE"; // placeholder
            String docNo = String.valueOf(idx + 1); // correlativo (o movementId si lo incluyes)
            String desc = m.movementType() == MovementType.RETIRO ? "TRANSFERENCIA / PAGO" : "ABONO / DEPOSITO";

            BigDecimal debit = BigDecimal.ZERO;
            BigDecimal credit = BigDecimal.ZERO;

            if (m.movementType() == MovementType.RETIRO) {
                debit = m.amount().abs();
                totalDebit = totalDebit.add(debit);
            } else {
                credit = m.amount().abs();
                totalCredit = totalCredit.add(credit);
            }

            y = drawRow(ctx, y, date, office, docNo, desc, debit, credit, m.balance());
        }

        // Totales tipo “resumen”
        y -= 14;
        y = drawTotals(ctx, y, totalDebit, totalCredit);

        ctx.close();
    }

    // --------- DRAW HELPERS ----------

    private PageCtx newPage(PDDocument doc) throws Exception {
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);
        PDPageContentStream cs = new PDPageContentStream(doc, page);
        float pageH = page.getMediaBox().getHeight();

        PageCtx ctx = new PageCtx(page, cs);
        ctx.y = pageH - MARGIN;
        return ctx;
    }

    private void drawHeader(PageCtx ctx, String brand) throws Exception {
        float pageW = ctx.page.getMediaBox().getWidth();
        float pageH = ctx.page.getMediaBox().getHeight();

        // Banda superior (amarillo)
        ctx.cs.setNonStrokingColor(new Color(240, 240, 240));
        ctx.cs.addRect(0, pageH - HEADER_H, pageW, HEADER_H);
        ctx.cs.fill();

        // Texto “marca” (no uses logos reales si no tienes permiso)
        ctx.cs.setNonStrokingColor(Color.BLACK);
        writeText(ctx, MARGIN, pageH - 40, brand, 18, true);

        // Línea separadora
        ctx.cs.setStrokingColor(new Color(210, 210, 210));
        ctx.cs.moveTo(0, pageH - HEADER_H);
        ctx.cs.lineTo(pageW, pageH - HEADER_H);
        ctx.cs.stroke();
    }

    private float drawMeta(PageCtx ctx, float y, LocalDate from, LocalDate to, String accountNumber) throws Exception {
        float pageH = ctx.page.getMediaBox().getHeight();
        float baseY = pageH - HEADER_H - 22;

        writeText(ctx, MARGIN, baseY, "Fecha de generación: " + LocalDate.now().format(DATE_FMT), 10, false);
        writeText(ctx, MARGIN, baseY - 14, "Cuenta: " + accountNumber, 10, false);
        writeText(ctx, MARGIN, baseY - 28, "Rango de fechas: " + from.format(DATE_FMT) + " - " + to.format(DATE_FMT), 10, false);

        return baseY - 44;
    }

    private float drawTableHeader(PageCtx ctx, float y) throws Exception {
        float x = MARGIN;
        float tableW = COL_DATE + COL_OFFICE + COL_DOC + COL_DESC + COL_DEBIT + COL_CREDIT + COL_BAL;

        // fondo header
        ctx.cs.setNonStrokingColor(new Color(240, 240, 240));
        ctx.cs.addRect(x, y - ROW_H, tableW, ROW_H);
        ctx.cs.fill();

        // borde
        ctx.cs.setStrokingColor(new Color(180, 180, 180));
        ctx.cs.addRect(x, y - ROW_H, tableW, ROW_H);
        ctx.cs.stroke();

        float ty = y - 13;

        writeText(ctx, x + 3, ty, "Fecha", 9, true);
        x += COL_DATE;

        writeText(ctx, x + 3, ty, "Oficina", 9, true);
        x += COL_OFFICE;

        writeText(ctx, x + 3, ty, "Nro. doc", 9, true);
        x += COL_DOC;

        writeText(ctx, x + 3, ty, "Descripción", 9, true);
        x += COL_DESC;

        writeText(ctx, x + 3, ty, "Retiro", 9, true);
        x += COL_DEBIT;

        writeText(ctx, x + 3, ty, "Deposito", 9, true);
        x += COL_CREDIT;

        writeText(ctx, x + 3, ty, "Saldo Disponible", 9, true);

        return y - ROW_H;
    }

    private float drawRow(
            PageCtx ctx,
            float y,
            String date,
            String office,
            String docNo,
            String desc,
            BigDecimal debit,
            BigDecimal credit,
            BigDecimal balance
    ) throws Exception {

        float x0 = MARGIN;
        float tableW = COL_DATE + COL_OFFICE + COL_DOC + COL_DESC + COL_DEBIT + COL_CREDIT + COL_BAL;

        // borde fila
        ctx.cs.setStrokingColor(new Color(220, 220, 220));
        ctx.cs.addRect(x0, y - ROW_H, tableW, ROW_H);
        ctx.cs.stroke();

        float x = x0;
        float ty = y - 13;

        writeText(ctx, x + 3, ty, date, 9, false);
        x += COL_DATE;

        writeText(ctx, x + 3, ty, office, 9, false);
        x += COL_OFFICE;

        writeText(ctx, x + 3, ty, docNo, 9, false);
        x += COL_DOC;

        writeText(ctx, x + 3, ty, truncate(desc, 38), 9, false);
        x += COL_DESC;

        writeTextRight(ctx, x + COL_DEBIT - 3, ty, money(debit), 9, false);
        x += COL_DEBIT;

        writeTextRight(ctx, x + COL_CREDIT - 3, ty, money(credit), 9, false);
        x += COL_CREDIT;

        writeTextRight(ctx, x + COL_BAL - 3, ty, money(balance), 9, false);

        return y - ROW_H;
    }

    private float drawTotals(PageCtx ctx, float y, BigDecimal totalDebit, BigDecimal totalCredit) throws Exception {
        float x = MARGIN;
        float right = ctx.page.getMediaBox().getWidth() - MARGIN;

        writeText(ctx, x, y, "Resumen", 11, true);
        y -= 14;

        writeText(ctx, x, y, "Total Retiros:", 10, false);
        writeTextRight(ctx, right, y, money(totalDebit), 10, true);
        y -= 14;

        writeText(ctx, x, y, "Total Depositos:", 10, false);
        writeTextRight(ctx, right, y, money(totalCredit), 10, true);
        y -= 14;

        return y;
    }

    private float centerText(PageCtx ctx, String text, float y, int size, boolean bold) throws Exception {
        float pageW = ctx.page.getMediaBox().getWidth();
        float textW = (bold ? FONT_BOLD : FONT).getStringWidth(text) / 1000f * size;
        float x = (pageW - textW) / 2f;

        writeText(ctx, x, y, text, size, bold);
        return y - 14;
    }

    private void writeText(PageCtx ctx, float x, float y, String text, int size, boolean bold) throws Exception {
        ctx.cs.setNonStrokingColor(Color.BLACK);   // <-- FIX
        ctx.cs.beginText();
        ctx.cs.setFont(bold ? FONT_BOLD : FONT, size);
        ctx.cs.newLineAtOffset(x, y);
        ctx.cs.showText(text);
        ctx.cs.endText();
    }

    private void writeTextRight(PageCtx ctx, float xRight, float y, String text, int size, boolean bold) throws Exception {
        ctx.cs.setNonStrokingColor(Color.BLACK);   // <-- FIX
        float textW = (bold ? FONT_BOLD : FONT).getStringWidth(text) / 1000f * size;
        float x = xRight - textW;
        writeText(ctx, x, y, text, size, bold);
    }


    private String money(BigDecimal v) {
        if (v == null) return "0.00";
        return MONEY_FMT.format(v);
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }

    private static class PageCtx {
        final PDPage page;
        final PDPageContentStream cs;
        float y;

        PageCtx(PDPage page, PDPageContentStream cs) {
            this.page = page;
            this.cs = cs;
        }

        void close() throws Exception {
            cs.close();
        }
    }
}
