package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.user.entity.CoordinatorProfile;
import com.dodlu.core.persistence.user.repository.CoordinatorProfileRepository;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class CoordinatorProfileUtils {

  public static CoordinatorProfile getCoordinatorById(
      CoordinatorProfileRepository coordinatorProfileRepository,
      CoreExceptionService exceptionService,
      String id) {
    return coordinatorProfileRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Coordinator does not exist"));
  }

  public static CoordinatorProfile getCoordinatorByUsername(
      CoordinatorProfileRepository coordinatorProfileRepository,
      CoreExceptionService exceptionService,
      String username) {
    return coordinatorProfileRepository
        .findByUsernameIgnoreCase(username)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Coordinator does not exist"));
  }
}
