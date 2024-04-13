package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.Placement;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacementRepository extends JpaRepository<Placement, String> {
  Optional<Placement> findPlacementByStudentIdAndActiveIsTrue(String studentId);

  boolean existsByCompanyIdAndStudentId(String companyId, String studentId);

  Boolean existsByStudentIdAndActive(String studentId, boolean active);

  Page<Placement> findAllByCompanyId(String employerId, Pageable pageable);
}
