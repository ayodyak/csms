package org.computerspareparts.csms.repository;

import org.computerspareparts.csms.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Integer> {
}

