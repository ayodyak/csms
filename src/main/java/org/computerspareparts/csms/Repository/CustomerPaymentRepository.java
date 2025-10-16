package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.CustomerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {
    List<CustomerPayment> findByCustomerCustomerId(Long customerId);
    List<CustomerPayment> findByPaymentMethod(String paymentMethod);
}
