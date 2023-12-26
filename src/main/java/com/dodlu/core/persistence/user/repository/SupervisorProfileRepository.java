package com.dodlu.core.persistence.user.repository;

import com.dodlu.core.persistence.user.entity.SupervisorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupervisorProfileRepository extends JpaRepository<SupervisorProfile, String> {}
