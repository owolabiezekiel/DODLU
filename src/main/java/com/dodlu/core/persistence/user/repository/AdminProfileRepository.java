package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.AdminProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProfileRepository extends JpaRepository<AdminProfile, String> {
  Optional<AdminProfile> findByUsernameIgnoreCase(String username);
}
