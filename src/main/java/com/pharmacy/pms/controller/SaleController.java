package com.pharmacy.pms.controller;

import com.pharmacy.pms.model.Sale;
import com.pharmacy.pms.service.MedicineService;
import com.pharmacy.pms.service.PdfInvoiceService;
import com.pharmacy.pms.service.SaleService;
import com.pharmacy.pms.service.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final MedicineService medicineService;
    private final PdfInvoiceService pdfInvoiceService;
    private final SmsService smsService;

    @GetMapping("/checkout")
    public String checkoutForm(Model model) {
        model.addAttribute("medicines", medicineService.findAll());
        return "sale/checkout";
    }

    // Expects: customerName, customerPhone, sendSms (checkbox), medicineId[], quantity[]
    @PostMapping("/checkout")
    public String checkout(HttpServletRequest request, Authentication authentication) {
        String customerName = request.getParameter("customerName");
        String customerPhone = request.getParameter("customerPhone");
        boolean sendSms = "on".equals(request.getParameter("sendSms")) || "true".equals(request.getParameter("sendSms"));
        String[] medicineIds = request.getParameterValues("medicineId");
        String[] quantities = request.getParameterValues("quantity");

        Map<Long, Integer> cart = new HashMap<>();
        if (medicineIds != null) {
            for (int i = 0; i < medicineIds.length; i++) {
                if (medicineIds[i] == null || medicineIds[i].isBlank()) continue;
                cart.put(Long.valueOf(medicineIds[i]), Integer.parseInt(quantities[i]));
            }
        }

        Sale sale = saleService.checkout(customerName, customerPhone, authentication.getName(), cart);

        if (sendSms && customerPhone != null && !customerPhone.isBlank()) {
            String message = "PharmaCare: Your bill INV-" + sale.getId() + " total is Rs. "
                    + sale.getTotalAmount() + ". Thank you for shopping with us!";
            smsService.sendSms(customerPhone, message, "INVOICE");
        }

        return "redirect:/sales/" + sale.getId() + "/invoice";
    }

    @GetMapping("/{id}/invoice")
    public String invoice(@PathVariable Long id, Model model) {
        model.addAttribute("sale", saleService.findById(id));
        return "sale/invoice";
    }

    @GetMapping("/{id}/invoice/pdf")
    public ResponseEntity<byte[]> invoicePdf(@PathVariable Long id) {
        Sale sale = saleService.findById(id);
        byte[] pdfBytes = pdfInvoiceService.generateInvoicePdf(sale);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice-INV-" + id + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    // Re-send the bill via (simulated) SMS from the invoice page, in case the customer wants it after the fact.
    @PostMapping("/{id}/invoice/send-sms")
    public String resendSms(@PathVariable Long id, @RequestParam String phone) {
        Sale sale = saleService.findById(id);
        String message = "PharmaCare: Your bill INV-" + sale.getId() + " total is Rs. "
                + sale.getTotalAmount() + ". Thank you for shopping with us!";
        smsService.sendSms(phone, message, "INVOICE");
        return "redirect:/sales/" + id + "/invoice";
    }
}
