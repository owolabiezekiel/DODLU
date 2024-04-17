package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.StudentProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, String> {
  Optional<StudentProfile> findByUsernameIgnoreCase(String username);

  long countByIsDeletedIsFalse();
}
