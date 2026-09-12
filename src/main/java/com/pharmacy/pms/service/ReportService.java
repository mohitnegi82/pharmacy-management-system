package com.pharmacy.pms.service;

import com.pharmacy.pms.model.Medicine;
import com.pharmacy.pms.model.Purchase;
import com.pharmacy.pms.model.Sale;
import com.pharmacy.pms.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MedicineService medicineService;
    private final SaleService saleService;
    private final PurchaseRepository purchaseRepository;

    public List<Medicine> stockSummary() {
        return medicineService.findAll();
    }

    public List<Medicine> lowStockReport() {
        return medicineService.lowStock();
    }

    public List<Medicine> expiryReport() {
        return medicineService.expiringInNext30Days();
    }

    public List<Sale> todaysSalesReport() {
        return saleService.todaysSales();
    }

    public double todaysEarnings() {
        return saleService.todaysSales().stream().mapToDouble(Sale::getTotalAmount).sum();
    }

    public List<Purchase> purchaseHistory() {
        return purchaseRepository.findAll().stream()
                .sorted(Comparator.comparing(Purchase::getPurchaseDate).reversed())
                .collect(Collectors.toList());
    }

    public double totalPurchaseSpend() {
        return purchaseRepository.findAll().stream().mapToDouble(Purchase::getTotalAmount).sum();
    }
}
