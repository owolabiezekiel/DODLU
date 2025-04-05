package com.dodlu.core.persistence.systemsettings.repository;

import com.dodlu.core.persistence.systemsettings.entity.SystemSettings;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemSettingsRepository extends JpaRepository<SystemSettings, Long> {
  Optional<SystemSettings> findFirstByOrderByIdDesc();
}
