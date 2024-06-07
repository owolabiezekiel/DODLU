package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.immunization.entity.Vaccine;
import com.dodlu.core.persistence.immunization.repository.VaccineRepository;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class VaccineUtils {
  public static Vaccine getVaccineById(
      VaccineRepository vaccineRepository, CoreExceptionService exceptionService, String id) {
    return vaccineRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Department does not exist"));
  }

  public static Vaccine getVaccineById(
      VaccineRepository vaccineRepository, Supplier<RuntimeException> errorSupplier, String id) {
    return vaccineRepository.findById(id).orElseThrow(errorSupplier);
  }
}
