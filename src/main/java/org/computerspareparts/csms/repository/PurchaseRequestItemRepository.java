package org.computerspareparts.csms.repository;

import org.computerspareparts.csms.entity.PurchaseRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRequestItemRepository extends JpaRepository<PurchaseRequestItem, Long> {
}

