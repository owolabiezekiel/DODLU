package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.faculty.entity.Faculty;
import com.dodlu.core.persistence.faculty.repository.FacultyRepository;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class FacultyUtils {
  public static Faculty getFacultyById(
      FacultyRepository facultyRepository, CoreExceptionService exceptionService, String id) {
    return facultyRepository
        .findById(id)
        .orElseThrow(
            () -> exceptionService.handleCreateNotFoundException("Faculty does not exist"));
  }

  public static Faculty getFacultyById(
      FacultyRepository facultyRepository, Supplier<RuntimeException> errorSupplier, String id) {
    return facultyRepository.findById(id).orElseThrow(errorSupplier);
  }
}
