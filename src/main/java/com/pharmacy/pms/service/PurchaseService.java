package com.pharmacy.pms.service;

import com.pharmacy.pms.model.Medicine;
import com.pharmacy.pms.model.Purchase;
import com.pharmacy.pms.model.PurchaseItem;
import com.pharmacy.pms.model.Supplier;
import com.pharmacy.pms.repository.MedicineRepository;
import com.pharmacy.pms.repository.PurchaseRepository;
import com.pharmacy.pms.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final MedicineRepository medicineRepository;

    /**
     * Records a stock-in purchase and increases medicine stock quantities.
     * itemsMap: medicineId -> [quantity, unitPrice]
     */
    @Transactional
    public Purchase recordPurchase(Long supplierId, Map<Long, int[]> quantities, Map<Long, Double> unitPrices) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        Purchase purchase = Purchase.builder().supplier(supplier).build();
        double total = 0.0;

        for (Map.Entry<Long, int[]> entry : quantities.entrySet()) {
            Long medicineId = entry.getKey();
            int qty = entry.getValue()[0];
            Medicine medicine = medicineRepository.findById(medicineId)
                    .orElseThrow(() -> new IllegalArgumentException("Medicine not found: " + medicineId));

            double unitPrice = unitPrices.getOrDefault(medicineId, medicine.getPrice());

            PurchaseItem item = PurchaseItem.builder()
                    .purchase(purchase)
                    .medicine(medicine)
                    .quantity(qty)
                    .unitPrice(unitPrice)
                    .build();
            purchase.getItems().add(item);

            medicine.setQuantity(medicine.getQuantity() + qty);
            medicineRepository.save(medicine);

            total += qty * unitPrice;
        }

        purchase.setTotalAmount(total);
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }
}
