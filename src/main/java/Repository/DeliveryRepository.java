package Repository;

import Entity.Delivery;
import Entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // Find deliveries by Supplier
    List<Delivery> findBySupplier(Supplier supplier);

    // Find deliveries by Status (Scheduled, Delivered, Cancelled)
    List<Delivery> findByStatus(String status);

    // Find deliveries by Supplier and Status
    List<Delivery> findBySupplierAndStatus(Supplier supplier, String status);
}
