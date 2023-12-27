package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import com.dodlu.core.persistence.user.repository.StudentProfileRepository;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class StudentProfileUtils {
  public static StudentProfile getStudentProfileByUsername(
      StudentProfileRepository studentProfileRepository,
      CoreExceptionService exceptionService,
      String username) {
    return studentProfileRepository
        .findByUsernameIgnoreCase(username)
        .orElseThrow(
            () -> exceptionService.handleCreateBadRequest("Student profile does not exist"));
  }
}
