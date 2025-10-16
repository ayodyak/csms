package org.computerspareparts.csms.Repository;


import org.computerspareparts.csms.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Example custom query method
    List<Product> findByProductNameContaining(String keyword);
}

