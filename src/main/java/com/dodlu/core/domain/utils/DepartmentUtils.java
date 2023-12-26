package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.department.entity.Department;
import com.dodlu.core.persistence.department.repository.DepartmentRepository;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class DepartmentUtils {
  public static Department getDepartmentById(
      DepartmentRepository departmentRepository, CoreExceptionService exceptionService, String id) {
    return departmentRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Department does not exist"));
  }

  public static Department getDepartmentById(
      DepartmentRepository departmentRepository,
      Supplier<RuntimeException> errorSupplier,
      String id) {
    return departmentRepository.findById(id).orElseThrow(errorSupplier);
  }
}
