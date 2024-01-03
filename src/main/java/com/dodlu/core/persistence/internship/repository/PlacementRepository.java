package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.Placement;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacementRepository extends JpaRepository<Placement, String> {
  Optional<Placement> findPlacementByStudentId(String studentId);
}
