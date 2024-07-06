package com.dodlu.core.persistence.notification.repository;

import com.dodlu.core.persistence.notification.entity.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long> {}
