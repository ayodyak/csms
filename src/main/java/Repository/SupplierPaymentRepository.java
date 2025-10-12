package Repository;



import Entity.SupplierPayment;
import Entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {

    // Find payments for a supplier
    List<SupplierPayment> findBySupplier(Supplier supplier);

    // Find payments by status (Paid, Pending, Failed)
    List<SupplierPayment> findBySupplierAndStatus(Supplier supplier, String status);
}
