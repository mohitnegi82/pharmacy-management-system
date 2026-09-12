package com.pharmacy.pms.repository;

import com.pharmacy.pms.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findByNameContainingIgnoreCase(String name);
    List<Medicine> findByQuantityLessThanEqual(Integer level);
    List<Medicine> findByExpiryDateBetween(LocalDate start, LocalDate end);
}
