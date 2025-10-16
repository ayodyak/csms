package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {
    List<CustomerInvoice> findByCustomerCustomerId(Long customerId);
}
