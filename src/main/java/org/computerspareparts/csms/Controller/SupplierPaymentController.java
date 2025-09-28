package org.computerspareparts.csms.Controller;

import org.computerspareparts.csms.Entity.SupplierPayment;
import org.computerspareparts.csms.Service.SupplierPaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/finance/payments")
public class SupplierPaymentController {
    private final SupplierPaymentService service;

    public SupplierPaymentController(SupplierPaymentService service) {
        this.service = service;
    }

    // READ
    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", service.getAllPayments());
        model.addAttribute("totalExpenses", service.getTotalExpenses());
        return "supplier-report";
    }

    // CREATE form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("payment", new SupplierPayment());
        return "supplier-form";
    }

    @PostMapping
    public String createPayment(@ModelAttribute SupplierPayment payment) {
        service.savePayment(payment);
        return "redirect:/finance/payments";
    }

    // UPDATE form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("payment", service.getPaymentById(id));
        return "supplier-form";
    }

    @PostMapping("/update/{id}")
    public String updatePayment(@PathVariable Long id, @ModelAttribute SupplierPayment payment) {
        service.updatePayment(id, payment);
        return "redirect:/finance/payments";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id) {
        service.deletePayment(id);
        return "redirect:/finance/payments";
    }
}

