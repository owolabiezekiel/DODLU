package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.CustomerProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, String> {
  Optional<CustomerProfile> findByPhoneNumber(String phoneNumber);

  Optional<CustomerProfile> findByEmailEqualsIgnoreCase(String email);

  Optional<CustomerProfile> findByUsernameEqualsIgnoreCase(String username);

  @Query(
      "SELECT c FROM CustomerProfile c WHERE LOWER(c.username) = LOWER(?1) OR LOWER(c.email) = LOWER(?1) OR c.id = ?1")
  Optional<CustomerProfile> findByUsernameOrEmailOrId(String inviteeId);

  boolean existsByPhoneNumberAndCountry_NameEqualsIgnoreCase(
      String phoneNumber, String countryName);
}
