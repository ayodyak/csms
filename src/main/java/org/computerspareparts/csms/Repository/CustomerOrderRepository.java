package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByStatus(String status);
    List<CustomerOrder> findByCustomerCustomerId(Long customerId);
}
