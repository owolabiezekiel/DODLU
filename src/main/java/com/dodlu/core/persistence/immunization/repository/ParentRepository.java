package com.dodlu.core.persistence.immunization.repository;

import com.dodlu.core.persistence.immunization.entity.Parent;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, String> {
  boolean existsByEmailEqualsIgnoreCase(String email);

  boolean existsByPhoneNumberEqualsIgnoreCase(String phoneNumber);

  Optional<Parent> findByEmailEqualsIgnoreCase(String email);

  Optional<Parent> findByPhoneNumberEquals(String phoneNumber);
}
