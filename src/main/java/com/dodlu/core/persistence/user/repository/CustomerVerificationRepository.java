package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.CustomerVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerVerificationRepository extends JpaRepository<CustomerVerification, Long> {}
