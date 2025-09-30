package org.computerspareparts.csms.Service;

import org.springframework.stereotype.Service;

@Service
public class FinanceAnalyticsService {
    private final InvoiceService invoiceService;
    private final SupplierPaymentService supplierPaymentService;

    public FinanceAnalyticsService(InvoiceService invoiceService, SupplierPaymentService supplierPaymentService) {
        this.invoiceService = invoiceService;
        this.supplierPaymentService = supplierPaymentService;
    }

    public double getTotalSales() {
        return invoiceService.getTotalSales();
    }

    public double getTotalExpenses() {
        return supplierPaymentService.getTotalExpenses();
    }

    public double getNetProfit() {
        return getTotalSales() - getTotalExpenses();
    }
}
