package org.computerspareparts.csms.Controller;

import org.computerspareparts.csms.Service.FinanceAnalyticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceDashboardController {
    private final FinanceAnalyticsService analyticsService;

    public FinanceDashboardController(FinanceAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/finance/dashboard")
    public String getDashboard(Model model) {
        model.addAttribute("totalSales", analyticsService.getTotalSales());
        model.addAttribute("totalExpenses", analyticsService.getTotalExpenses());
        model.addAttribute("netProfit", analyticsService.getNetProfit());
        return "finance-dashboard";
    }
}
