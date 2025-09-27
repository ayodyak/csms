package org.computerspareparts.csms.repository;

import org.computerspareparts.csms.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartRepository extends JpaRepository<Part, Integer> {
    List<Part> findByStockLevelLessThanEqualAndReorderLevelGreaterThan(Integer stockLevel, Integer reorderLevel);
    // For notification: find parts where stockLevel <= reorderLevel
    List<Part> findByStockLevelLessThanEqual(Integer reorderLevel);

    @Query("SELECT p FROM Part p WHERE p.stockLevel <= p.reorderLevel")
    List<Part> findLowStockParts();
}
