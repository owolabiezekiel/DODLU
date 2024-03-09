package com.dodlu.core.persistence.notification.repository;

import com.dodlu.core.persistence.notification.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, String> {}
