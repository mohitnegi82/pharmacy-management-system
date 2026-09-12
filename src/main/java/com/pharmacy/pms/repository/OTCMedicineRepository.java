package com.pharmacy.pms.repository;

import com.pharmacy.pms.model.OTCMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OTCMedicineRepository extends JpaRepository<OTCMedicine, Long> {

    List<OTCMedicine> findByCategoryIgnoreCaseAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqualOrderByPriorityAsc(
            String category, Integer age1, Integer age2);

    List<OTCMedicine> findAllByOrderByCategoryAsc();
}
