package com.pharmacy.pms.controller;

import com.pharmacy.pms.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public String reports(Model model) {
        model.addAttribute("stockSummary", reportService.stockSummary());
        model.addAttribute("lowStock", reportService.lowStockReport());
        model.addAttribute("expiring", reportService.expiryReport());
        model.addAttribute("todaysSales", reportService.todaysSalesReport());
        model.addAttribute("todaysEarnings", reportService.todaysEarnings());
        model.addAttribute("purchaseHistory", reportService.purchaseHistory());
        model.addAttribute("totalPurchaseSpend", reportService.totalPurchaseSpend());
        return "reports/reports";
    }
}
