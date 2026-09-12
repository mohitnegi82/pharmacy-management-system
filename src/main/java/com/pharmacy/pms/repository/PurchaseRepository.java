package com.pharmacy.pms.repository;

import com.pharmacy.pms.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
