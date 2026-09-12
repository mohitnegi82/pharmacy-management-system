package com.pharmacy.pms.controller;

import com.pharmacy.pms.model.Medicine;
import com.pharmacy.pms.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicines")
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("medicines",
                (q == null || q.isBlank()) ? medicineService.findAll() : medicineService.search(q));
        model.addAttribute("q", q);
        return "medicine/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        return "medicine/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("medicine", medicineService.findById(id));
        return "medicine/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Medicine medicine) {
        medicineService.save(medicine);
        return "redirect:/medicines";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        medicineService.delete(id);
        return "redirect:/medicines";
    }
}
