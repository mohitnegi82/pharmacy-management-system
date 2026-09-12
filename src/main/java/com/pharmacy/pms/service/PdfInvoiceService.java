package com.pharmacy.pms.service;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pharmacy.pms.model.Sale;
import com.pharmacy.pms.model.SaleItem;
import org.springframework.stereotype.Service;

import com.itextpdf.text.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfInvoiceService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private static final Font SUB_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
    private static final Font BODY_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font TOTAL_FONT = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);

    /**
     * Generates a printable PDF invoice for a completed sale and returns it as a byte array,
     * ready to be streamed as a file download or (in future) attached to a real SMS/email.
     */
    public byte[] generateInvoicePdf(Sale sale) {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Paragraph title = new Paragraph("PharmaCare Pharmacy", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Store ID: PHC-001  |  Official Invoice", SUB_FONT);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(16);
            document.add(subtitle);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            document.add(new Paragraph("Invoice No: INV-" + sale.getId(), BODY_FONT));
            document.add(new Paragraph("Date: " + sale.getSaleDate().format(fmt), BODY_FONT));
            document.add(new Paragraph("Customer: " + safe(sale.getCustomerName())
                    + (sale.getCustomerPhone() != null && !sale.getCustomerPhone().isBlank()
                        ? " (" + sale.getCustomerPhone() + ")" : ""), BODY_FONT));
            document.add(new Paragraph("Served By: " + safe(sale.getSoldBy() != null ? sale.getSoldBy().getFullName() : "N/A"), BODY_FONT));

            Paragraph spacer = new Paragraph(" ");
            spacer.setSpacingAfter(10);
            document.add(spacer);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1, 1.5f, 1.5f});

            addHeaderCell(table, "Medicine");
            addHeaderCell(table, "Qty");
            addHeaderCell(table, "Unit Price");
            addHeaderCell(table, "Subtotal");

            for (SaleItem item : sale.getItems()) {
                table.addCell(new PdfPCell(new Phrase(item.getMedicine().getName(), BODY_FONT)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), BODY_FONT)));
                table.addCell(new PdfPCell(new Phrase("Rs. " + item.getUnitPrice(), BODY_FONT)));
                double subtotal = item.getQuantity() * item.getUnitPrice();
                table.addCell(new PdfPCell(new Phrase("Rs. " + subtotal, BODY_FONT)));
            }

            document.add(table);

            Paragraph totalPara = new Paragraph("Total: Rs. " + sale.getTotalAmount(), TOTAL_FONT);
            totalPara.setAlignment(Element.ALIGN_RIGHT);
            totalPara.setSpacingBefore(16);
            document.add(totalPara);

            Paragraph footer = new Paragraph("Thank you for choosing PharmaCare!", SUB_FONT);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(24);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate invoice PDF", e);
        }

        return out.toByteArray();
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, HEADER_FONT));
        cell.setBackgroundColor(new BaseColor(15, 118, 110)); // matches site's teal primary color
        cell.setPadding(6);
        table.addCell(cell);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
