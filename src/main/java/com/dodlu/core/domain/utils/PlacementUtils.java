package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.internship.repository.PlacementRepository;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class PlacementUtils {
  public static Boolean assertStudentHasPlacement(
      PlacementRepository placementRepository,
      CoreExceptionService exceptionService,
      String studentId) {
    if (placementRepository.existsByStudentIdAndActive(studentId, true)) {
      return true;
    } else {
      throw exceptionService.handleCreateBadRequest("You are yet to secure a placement");
    }
  }
}
