package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Integer> {
}

