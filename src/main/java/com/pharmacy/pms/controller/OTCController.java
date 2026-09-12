package com.pharmacy.pms.controller;

import com.pharmacy.pms.model.OTCMedicine;
import com.pharmacy.pms.service.OTCService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/otc")
@RequiredArgsConstructor
public class OTCController {

    private final OTCService otcService;

    @GetMapping
    public String searchPage(Model model) {
        model.addAttribute("categories", otcService.allCategories());
        return "otc/search";
    }

    @GetMapping("/results")
    public String results(@RequestParam Integer age, @RequestParam String category, Model model) {
        List<OTCMedicine> suggestions = otcService.topSuggestions(age, category);
        model.addAttribute("categories", otcService.allCategories());
        model.addAttribute("suggestions", suggestions);
        model.addAttribute("age", age);
        model.addAttribute("category", category);
        model.addAttribute("searched", true);
        return "otc/search";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute OTCMedicine medicine) {
        otcService.save(medicine);
        return "redirect:/otc";
    }
}
