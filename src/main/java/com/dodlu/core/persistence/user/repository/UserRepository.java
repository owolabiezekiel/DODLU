package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsernameEqualsIgnoreCase(String username);

  Optional<User> findByEmailEqualsIgnoreCase(String email);

  Optional<User> findByPhoneNumberEqualsIgnoreCase(String phoneNumber);

  boolean existsByUsernameEqualsIgnoreCase(String username);

  boolean existsByEmailEqualsIgnoreCase(String email);
}
