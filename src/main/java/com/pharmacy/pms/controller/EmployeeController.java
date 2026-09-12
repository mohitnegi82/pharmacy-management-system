package com.pharmacy.pms.controller;

import com.pharmacy.pms.model.Employee;
import com.pharmacy.pms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("employee", new Employee());
        return "employee/list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        try {
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("success", "Employee '" + employee.getName() + "' added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Could not save employee: " + e.getMessage());
        }
        return "redirect:/employees";
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deactivate(id);
        redirectAttributes.addFlashAttribute("success", "Employee deactivated.");
        return "redirect:/employees";
    }

    @PostMapping("/{id}/activate")
    public String activate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.activate(id);
        redirectAttributes.addFlashAttribute("success", "Employee activated.");
        return "redirect:/employees";
    }
}
