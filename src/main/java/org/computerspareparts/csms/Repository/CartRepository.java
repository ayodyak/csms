package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCustomerCustomerId(Long customerId);
}
