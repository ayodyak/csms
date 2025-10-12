package Repository;



import Entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    // Login by email and password
    Optional<Supplier> findByEmailAndPasswordHash(String email, String passwordHash);

    // Find by email (for validations)
    Optional<Supplier> findByEmail(String email);
}
