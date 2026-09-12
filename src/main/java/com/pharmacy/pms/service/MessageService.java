package com.pharmacy.pms.service;

import com.pharmacy.pms.model.Employee;
import com.pharmacy.pms.model.Message;
import com.pharmacy.pms.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final EmployeeService employeeService;
    private final SmsService smsService;

    public List<Message> inboxFor(String username) {
        return messageRepository.findByRecipientOrderBySentAtDesc(username);
    }

    public Message send(Message message) {
        return messageRepository.save(message);
    }

    /**
     * Sends (simulated) SMS to every active employee's phone number, and keeps an in-app
     * Message record per employee so it also shows up in the app's own messaging history.
     * Returns the number of employees the message was sent to.
     */
    public int broadcastToAllEmployees(String senderUsername, String subject, String body) {
        List<Employee> activeEmployees = employeeService.findAll().stream()
                .filter(Employee::isActive)
                .filter(e -> e.getPhone() != null && !e.getPhone().isBlank())
                .toList();

        String smsText = "PharmaCare: " + subject + " - " + body;

        for (Employee employee : activeEmployees) {
            smsService.sendSms(employee.getPhone(), smsText, "EMPLOYEE_BROADCAST");

            Message record = Message.builder()
                    .sender(senderUsername)
                    .recipient(employee.getName())
                    .subject(subject)
                    .body(body)
                    .build();
            messageRepository.save(record);
        }

        return activeEmployees.size();
    }
}
