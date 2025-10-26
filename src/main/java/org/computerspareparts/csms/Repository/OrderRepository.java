package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}

