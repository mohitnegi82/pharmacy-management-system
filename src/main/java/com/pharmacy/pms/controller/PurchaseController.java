package com.pharmacy.pms.controller;

import com.pharmacy.pms.service.MedicineService;
import com.pharmacy.pms.service.PurchaseService;
import com.pharmacy.pms.service.SupplierService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final SupplierService supplierService;
    private final MedicineService medicineService;

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("medicines", medicineService.findAll());
        return "purchase/form";
    }

    // Expects form fields: supplierId, medicineId[], quantity[], unitPrice[]
    @PostMapping("/save")
    public String save(HttpServletRequest request) {
        Long supplierId = Long.valueOf(request.getParameter("supplierId"));
        String[] medicineIds = request.getParameterValues("medicineId");
        String[] quantities = request.getParameterValues("quantity");
        String[] unitPrices = request.getParameterValues("unitPrice");

        Map<Long, int[]> qtyMap = new HashMap<>();
        Map<Long, Double> priceMap = new HashMap<>();

        if (medicineIds != null) {
            for (int i = 0; i < medicineIds.length; i++) {
                if (medicineIds[i] == null || medicineIds[i].isBlank()) continue;
                Long medId = Long.valueOf(medicineIds[i]);
                int qty = Integer.parseInt(quantities[i]);
                double price = Double.parseDouble(unitPrices[i]);
                qtyMap.put(medId, new int[]{qty});
                priceMap.put(medId, price);
            }
        }

        purchaseService.recordPurchase(supplierId, qtyMap, priceMap);
        return "redirect:/medicines";
    }
}
