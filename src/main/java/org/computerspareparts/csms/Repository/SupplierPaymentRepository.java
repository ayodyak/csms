package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {
    List<SupplierPayment> findByStatus(String status);
}
