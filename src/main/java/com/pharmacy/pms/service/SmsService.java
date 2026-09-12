package com.pharmacy.pms.service;

import com.pharmacy.pms.model.SmsLog;
import com.pharmacy.pms.repository.SmsLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * SIMULATED SMS SERVICE — for demo/viva purposes.
 * No real SMS provider (e.g. Twilio) is connected, so no account/API keys are needed to run this project.
 * Every "sent" SMS is logged to the database (SmsLog) and printed to the console, so you can show
 * the examiner it fired correctly without needing a real phone/carrier account.
 *
 * TO GO LIVE LATER: replace the body of sendSms() with a real provider call, e.g. Twilio's
 * MessagingClient, using the same (toPhone, message) inputs — nothing else in the app needs to change.
 */
@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsLogRepository smsLogRepository;

    public void sendSms(String toPhone, String message, String purpose) {
        if (toPhone == null || toPhone.isBlank()) {
            return; // no phone number provided, nothing to send
        }

        System.out.println(">>> [SIMULATED SMS] To: " + toPhone + " | Purpose: " + purpose);
        System.out.println(">>> Message: " + message);

        SmsLog log = SmsLog.builder()
                .toPhone(toPhone)
                .message(message)
                .purpose(purpose)
                .simulated(true)
                .build();
        smsLogRepository.save(log);
    }
}
