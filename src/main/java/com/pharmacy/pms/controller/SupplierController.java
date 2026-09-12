package com.pharmacy.pms.controller;

import com.pharmacy.pms.model.Supplier;
import com.pharmacy.pms.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("suppliers", supplierService.findAll());
        model.addAttribute("supplier", new Supplier());
        return "supplier/list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Supplier supplier) {
        supplierService.save(supplier);
        return "redirect:/suppliers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        supplierService.delete(id);
        return "redirect:/suppliers";
    }
}
