package com.dodlu.core.domain.systemsettings.service;

import com.dodlu.core.app.exceptions.ResourceNotFoundException;
import com.dodlu.core.app.model.request.systemsettings.SystemSettingsRequest;
import com.dodlu.core.domain.systemsettings.domain.SystemSettingsModel;
import com.dodlu.core.persistence.systemsettings.entity.SystemSettings;
import com.dodlu.core.persistence.systemsettings.repository.SystemSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SystemSettingsService {
  private final SystemSettingsRepository systemSettingsRepository;

  public SystemSettingsModel getSystemSettings() {
    return systemSettingsRepository
        .findFirstByOrderByIdDesc()
        .orElseThrow(() -> new ResourceNotFoundException("System settings does not exist"))
        .toModel();
  }

  public SystemSettingsModel createSystemSettings(SystemSettingsRequest request) {
    SystemSettings systemSettings =
        systemSettingsRepository.findFirstByOrderByIdDesc().orElseGet(SystemSettings::new);

    systemSettings.setMinTransferAmount(request.minTransferAmount());
    systemSettings.setMaxWrongPinAttempts(request.maxWrongPinAttempts());
    return systemSettingsRepository.save(systemSettings).toModel();
  }
}
