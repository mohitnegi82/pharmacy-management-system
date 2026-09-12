package com.pharmacy.pms.service;

import com.pharmacy.pms.model.Medicine;
import com.pharmacy.pms.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }

    public List<Medicine> search(String name) {
        return medicineRepository.findByNameContainingIgnoreCase(name);
    }

    public Medicine findById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicine not found: " + id));
    }

    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public void delete(Long id) {
        medicineRepository.deleteById(id);
    }

    public List<Medicine> lowStock() {
        return medicineRepository.findByQuantityLessThanEqual(10);
    }

    public List<Medicine> expiringInNext30Days() {
        return medicineRepository.findByExpiryDateBetween(LocalDate.now(), LocalDate.now().plusDays(30));
    }
}
