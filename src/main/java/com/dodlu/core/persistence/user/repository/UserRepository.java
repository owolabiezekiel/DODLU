package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, String> {
  Optional<AppUser> findByUsernameIgnoreCase(String username);

  boolean existsByUsernameEqualsIgnoreCase(String username);

  boolean existsByEmailEqualsIgnoreCase(String email);
}
