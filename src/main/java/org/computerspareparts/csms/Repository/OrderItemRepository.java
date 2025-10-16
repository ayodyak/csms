package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}

