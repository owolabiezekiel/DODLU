package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.user.entity.SupervisorProfile;
import com.dodlu.core.persistence.user.repository.SupervisorProfileRepository;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class SupervisorProfileUtils {
  public static SupervisorProfile getSupervisorProfileById(
      SupervisorProfileRepository supervisorProfileRepository,
      CoreExceptionService exceptionService,
      String id) {
    return supervisorProfileRepository
        .findById(id)
        .orElseThrow(
            () -> exceptionService.handleCreateBadRequest("Supervisor profile does not exist"));
  }

  public static SupervisorProfile getSupervisorProfileByUsername(
      SupervisorProfileRepository supervisorProfileRepository,
      CoreExceptionService exceptionService,
      String username) {
    return supervisorProfileRepository
        .findByUsernameIgnoreCase(username)
        .orElseThrow(
            () -> exceptionService.handleCreateBadRequest("Supervisor profile does not exist"));
  }
}
