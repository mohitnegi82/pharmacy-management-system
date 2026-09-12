package com.pharmacy.pms.service;

import com.pharmacy.pms.model.OTCMedicine;
import com.pharmacy.pms.repository.OTCMedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OTCService {

    private final OTCMedicineRepository otcMedicineRepository;

    /**
     * Returns the top 3 OTC medicines for a given age + disease/symptom category,
     * ranked by priority (lowest number = highest priority).
     */
    public List<OTCMedicine> topSuggestions(Integer age, String category) {
        return otcMedicineRepository
                .findByCategoryIgnoreCaseAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqualOrderByPriorityAsc(
                        category, age, age)
                .stream()
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<String> allCategories() {
        return otcMedicineRepository.findAllByOrderByCategoryAsc()
                .stream()
                .map(OTCMedicine::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public OTCMedicine save(OTCMedicine medicine) {
        return otcMedicineRepository.save(medicine);
    }

    public List<OTCMedicine> findAll() {
        return otcMedicineRepository.findAll();
    }
}
