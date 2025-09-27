package org.computerspareparts.csms.repository;

import org.computerspareparts.csms.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    List<PurchaseRequest> findByManagerId(Integer managerId);
}
