package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.EmployerProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, String> {
  Optional<EmployerProfile> findByUsernameIgnoreCase(String username);
}
