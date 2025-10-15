package org.computerspareparts.csms.Service;

import org.springframework.stereotype.Service;
import java.util.List;
import org.computerspareparts.csms.Entity.Invoice;
import org.computerspareparts.csms.Repository.InvoiceRepository;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    // CREATE
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    // READ
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    // UPDATE
    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        return invoiceRepository.findById(id).map(invoice -> {
            invoice.setCustomerName(updatedInvoice.getCustomerName());
            invoice.setAmount(updatedInvoice.getAmount());
            invoice.setDate(updatedInvoice.getDate());
            invoice.setStatus(updatedInvoice.getStatus());
            return invoiceRepository.save(invoice);
        }).orElse(null);
    }

    // DELETE
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    // EXTRA: Analytics
    public double getTotalSales() {
        return invoiceRepository.findAll()
                .stream()
                .mapToDouble(Invoice::getAmount)
                .sum();
    }
}
