package org.computerspareparts.csms.Service;

import org.computerspareparts.csms.Entity.SupplierPayment;
import org.computerspareparts.csms.Repository.SupplierPaymentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplierPaymentService {
    private final SupplierPaymentRepository repository;

    public SupplierPaymentService(SupplierPaymentRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public SupplierPayment savePayment(SupplierPayment payment) {
        return repository.save(payment);
    }

    // READ
    public List<SupplierPayment> getAllPayments() {
        return repository.findAll();
    }

    public SupplierPayment getPaymentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public SupplierPayment updatePayment(Long id, SupplierPayment updated) {
        return repository.findById(id).map(payment -> {
            payment.setSupplierName(updated.getSupplierName());
            payment.setPaymentAmount(updated.getPaymentAmount());
            payment.setPaymentDate(updated.getPaymentDate());
            payment.setStatus(updated.getStatus());
            return repository.save(payment);
        }).orElse(null);
    }

    // DELETE
    public void deletePayment(Long id) {
        repository.deleteById(id);
    }

    // ANALYTICS
    public double getTotalExpenses() {
        return repository.findAll()
                .stream()
                .mapToDouble(SupplierPayment::getPaymentAmount)
                .sum();
    }
}
