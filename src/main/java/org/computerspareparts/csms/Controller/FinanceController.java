package org.computerspareparts.csms.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.computerspareparts.csms.Entity.Invoice;
import org.computerspareparts.csms.Service.InvoiceService;

@Controller
@RequestMapping("/finance/invoices")
public class FinanceController {
    private final InvoiceService invoiceService;

    public FinanceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // READ all invoices
    @GetMapping
    public String listInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        model.addAttribute("totalSales", invoiceService.getTotalSales());
        return "finance-report"; // Thymeleaf page
    }

    // CREATE form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("invoice", new Invoice());
        return "invoice-form";
    }

    @PostMapping
    public String createInvoice(@ModelAttribute Invoice invoice) {
        invoiceService.saveInvoice(invoice);
        return "redirect:/finance/invoices";
    }

    // UPDATE form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("invoice", invoiceService.getInvoiceById(id));
        return "invoice-form";
    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable Long id, @ModelAttribute Invoice invoice) {
        invoiceService.updateInvoice(id, invoice);
        return "redirect:/finance/invoices";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return "redirect:/finance/invoices";
    }
}
