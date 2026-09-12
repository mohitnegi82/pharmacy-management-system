package com.pharmacy.pms.controller;

import com.pharmacy.pms.model.Message;
import com.pharmacy.pms.repository.SmsLogRepository;
import com.pharmacy.pms.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SmsLogRepository smsLogRepository;

    @GetMapping
    public String inbox(Model model, Authentication authentication) {
        model.addAttribute("messages", messageService.inboxFor(authentication.getName()));
        return "messages/inbox";
    }

    @GetMapping("/send")
    public String sendForm(Model model) {
        model.addAttribute("message", new Message());
        return "messages/send";
    }

    @PostMapping("/send")
    public String send(@ModelAttribute Message message, Authentication authentication) {
        message.setSender(authentication.getName());
        messageService.send(message);
        return "redirect:/messages";
    }

    @GetMapping("/broadcast")
    public String broadcastForm() {
        return "messages/broadcast";
    }

    @PostMapping("/broadcast")
    public String broadcast(@RequestParam String subject, @RequestParam String body,
                             Authentication authentication, RedirectAttributes redirectAttributes) {
        int count = messageService.broadcastToAllEmployees(authentication.getName(), subject, body);
        redirectAttributes.addFlashAttribute("success",
                "Message broadcast (simulated SMS) sent to " + count + " active employee(s).");
        return "redirect:/messages";
    }

    // Shows the simulated SMS log so you can demonstrate to an examiner that messages actually fired.
    @GetMapping("/sms-log")
    public String smsLog(Model model) {
        model.addAttribute("smsLogs", smsLogRepository.findAllByOrderBySentAtDesc());
        return "messages/sms-log";
    }
}
