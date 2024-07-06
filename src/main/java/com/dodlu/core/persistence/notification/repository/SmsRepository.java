package com.dodlu.core.persistence.notification.repository;

import com.dodlu.core.persistence.notification.entity.Sms;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long> {
  Optional<Sms> findBySmsId(String smsId);
}
