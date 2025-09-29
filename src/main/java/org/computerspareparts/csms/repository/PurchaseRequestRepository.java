package org.computerspareparts.csms.repository;

import org.computerspareparts.csms.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    List<PurchaseRequest> findByManagerId(Integer managerId);

    @Query("SELECT pr FROM PurchaseRequest pr LEFT JOIN FETCH pr.items i LEFT JOIN FETCH i.part WHERE pr.managerId = :managerId")
    List<PurchaseRequest> findByManagerIdWithItemsAndParts(@Param("managerId") Integer managerId);

    long countByManagerIdAndRequestDateBetween(Integer managerId, java.time.LocalDateTime start, java.time.LocalDateTime end);

    @Query("SELECT COUNT(pr) FROM PurchaseRequest pr WHERE pr.managerId = :managerId AND pr.status NOT IN ('COMPLETED', 'REJECTED')")
    long countOpenRequestsByManagerId(@Param("managerId") Integer managerId);
}
