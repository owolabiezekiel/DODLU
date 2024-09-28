package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, String> {}
