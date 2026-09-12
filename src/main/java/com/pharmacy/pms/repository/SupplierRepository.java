package com.pharmacy.pms.repository;

import com.pharmacy.pms.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
