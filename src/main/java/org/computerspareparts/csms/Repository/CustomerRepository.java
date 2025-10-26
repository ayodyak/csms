package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}

