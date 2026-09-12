package com.pharmacy.pms.controller;

import com.pharmacy.pms.model.Role;
import com.pharmacy.pms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Admin-only: manage who is allowed to log into the system at all.
// Deliberately separate from Employee HR records (see EmployeeController) — an employee
// only gets a login if an admin explicitly creates one here.
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final UserService userService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", Role.values());
        return "users/list";
    }

    @PostMapping("/save")
    public String save(@RequestParam String username, @RequestParam String password,
                        @RequestParam String fullName, @RequestParam(required = false) String email,
                        @RequestParam Role role, RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(username, password, fullName, email, role);
            redirectAttributes.addFlashAttribute("success", "Login account created for '" + username + "'.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deactivate(id);
        redirectAttributes.addFlashAttribute("success", "Login account deactivated.");
        return "redirect:/users";
    }

    @PostMapping("/{id}/activate")
    public String activate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.activate(id);
        redirectAttributes.addFlashAttribute("success", "Login account activated.");
        return "redirect:/users";
    }
}
