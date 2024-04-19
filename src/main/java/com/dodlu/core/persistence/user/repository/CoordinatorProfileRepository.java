package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.CoordinatorProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatorProfileRepository extends JpaRepository<CoordinatorProfile, String> {
  Optional<CoordinatorProfile> findByUsernameIgnoreCase(String username);

  long countByIsDeletedIsFalse();
}
