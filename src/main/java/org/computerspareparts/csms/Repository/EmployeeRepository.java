package org.computerspareparts.csms.Repository;

import org.computerspareparts.csms.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}

