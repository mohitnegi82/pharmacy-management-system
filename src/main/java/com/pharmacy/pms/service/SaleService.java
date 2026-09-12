package com.pharmacy.pms.service;

import com.pharmacy.pms.model.*;
import com.pharmacy.pms.repository.MedicineRepository;
import com.pharmacy.pms.repository.SaleRepository;
import com.pharmacy.pms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final MedicineRepository medicineRepository;
    private final UserRepository userRepository;

    /**
     * Records a checkout/sale, deducts stock, and returns the saved Sale (used for invoice generation).
     * cart: medicineId -> quantity
     */
    @Transactional
    public Sale checkout(String customerName, String customerPhone, String soldByUsername, Map<Long, Integer> cart) {
        User cashier = userRepository.findByUsername(soldByUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Sale sale = Sale.builder()
                .customerName(customerName)
                .customerPhone(customerPhone)
                .soldBy(cashier)
                .build();

        double total = 0.0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Medicine medicine = medicineRepository.findById(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Medicine not found: " + entry.getKey()));
            int qty = entry.getValue();

            if (medicine.getQuantity() < qty) {
                throw new IllegalStateException("Insufficient stock for " + medicine.getName());
            }

            medicine.setQuantity(medicine.getQuantity() - qty);
            medicineRepository.save(medicine);

            SaleItem item = SaleItem.builder()
                    .sale(sale)
                    .medicine(medicine)
                    .quantity(qty)
                    .unitPrice(medicine.getPrice())
                    .build();
            sale.getItems().add(item);

            total += qty * medicine.getPrice();
        }

        sale.setTotalAmount(total);
        return saleRepository.save(sale);
    }

    public Sale findById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found: " + id));
    }

    public List<Sale> todaysSales() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return saleRepository.findBySaleDateBetween(start, end);
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }
}
