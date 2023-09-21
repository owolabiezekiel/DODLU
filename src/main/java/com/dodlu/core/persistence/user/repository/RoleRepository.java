package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
  boolean existsByName(String name);

  boolean existsByDescription(String description);
}
