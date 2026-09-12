package com.pharmacy.pms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// Simulated SMS log — records every "SMS" the system would have sent.
// No real SMS provider is wired up (no Twilio account required for this project's demo).
// To go live: implement SmsService.sendSms() to call a real provider using these same records.
@Entity
@Table(name = "sms_log")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SmsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String toPhone;

    @Column(length = 1000, nullable = false)
    private String message;

    private String purpose; // e.g. "INVOICE", "EMPLOYEE_BROADCAST"

    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();

    @Builder.Default
    private boolean simulated = true;
}
