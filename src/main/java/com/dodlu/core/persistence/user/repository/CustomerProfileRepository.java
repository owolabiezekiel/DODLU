package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.CustomerProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, String> {
  Optional<CustomerProfile> findByPhoneNumber(String phoneNumber);

  Optional<CustomerProfile> findByEmailEqualsIgnoreCase(String email);

  Optional<CustomerProfile> findByUsernameEqualsIgnoreCase(String username);

  boolean existsByPhoneNumberAndCountry_NameEqualsIgnoreCase(
      String phoneNumber, String countryName);
}
