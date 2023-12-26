package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import com.dodlu.core.persistence.user.repository.EmployerProfileRepository;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class EmployerProfileUtils {

  public static EmployerProfile getEmployerById(
      EmployerProfileRepository employerProfileRepository,
      CoreExceptionService exceptionService,
      String id) {
    return employerProfileRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Employer does not exist"));
  }

  public static EmployerProfile getEmployerByUsername(
      EmployerProfileRepository employerProfileRepository,
      CoreExceptionService exceptionService,
      String username) {
    return employerProfileRepository
        .findByUsernameIgnoreCase(username)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Employer does not exist"));
  }
}
