package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.location.entity.Country;
import com.dodlu.core.persistence.location.entity.State;
import com.dodlu.core.persistence.location.repository.CountryRepository;
import com.dodlu.core.persistence.location.repository.StateRepository;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class LocationUtils {
  public static Country getCountryById(
      CountryRepository countryRepository, CoreExceptionService exceptionService, String id) {
    return countryRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Country does not exist"));
  }

  public static Country getCountryById(
      CountryRepository countryRepository, Supplier<RuntimeException> errorSupplier, String id) {
    return countryRepository.findById(id).orElseThrow(errorSupplier);
  }

  public static State getStateById(
      StateRepository stateRepository, CoreExceptionService exceptionService, String id) {
    return stateRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("State does not exist"));
  }

  public static State getStateById(
      StateRepository stateRepository, Supplier<RuntimeException> errorSupplier, String id) {
    return stateRepository.findById(id).orElseThrow(errorSupplier);
  }
}
