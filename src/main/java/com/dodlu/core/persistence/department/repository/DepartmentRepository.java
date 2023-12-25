package com.dodlu.core.persistence.department.repository;

import com.dodlu.core.persistence.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {
  boolean existsByHodId(String id);

  boolean existsByNameEqualsIgnoreCaseAndFacultyId(String name, String faculty);
}
