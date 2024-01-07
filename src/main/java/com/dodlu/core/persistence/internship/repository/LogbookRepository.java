package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.Logbook;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogbookRepository extends JpaRepository<Logbook, String> {
  Optional<Logbook> findByStudentIdAndActiveIsTrue(String studentId);
}
