package com.dodlu.core.persistence.internship.repository;

import com.dodlu.core.persistence.internship.entity.StudentSupervisor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSupervisorRepository extends JpaRepository<StudentSupervisor, String> {
  boolean existsByCompanyIdAndStudentIdAndActive(
      String companyId, String studentId, boolean active);

  Optional<StudentSupervisor> findByStudentIdAndActiveIsTrue(String studentId);

  Page<StudentSupervisor> findStudentSupervisorBySupervisorIdAndActiveIsTrue(
      Pageable pageable, String supervisorId);
}
