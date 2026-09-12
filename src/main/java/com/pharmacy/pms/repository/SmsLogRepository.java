package com.pharmacy.pms.repository;

import com.pharmacy.pms.model.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SmsLogRepository extends JpaRepository<SmsLog, Long> {
    List<SmsLog> findAllByOrderBySentAtDesc();
}
