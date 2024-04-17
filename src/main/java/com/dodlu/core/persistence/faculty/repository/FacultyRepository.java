package com.dodlu.core.persistence.faculty.repository;

import com.dodlu.core.persistence.faculty.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, String> {
  boolean existsByName(String name);

  boolean existsByDeanId(String id);

  long count();
}
