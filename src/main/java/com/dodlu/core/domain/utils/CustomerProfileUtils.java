package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class CustomerProfileUtils {

  public static CustomerProfile getCustomerByEmail(
      CustomerProfileRepository repository, CoreExceptionService exceptionService, String email) {
    return repository
        .findByEmailEqualsIgnoreCase(email)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Customer does not exist"));
  }

  public static CustomerProfile getCustomerByUsername(
      CustomerProfileRepository repository,
      CoreExceptionService exceptionService,
      String username) {
    return repository
        .findByUsernameEqualsIgnoreCase(username)
        .orElseThrow(() -> exceptionService.handleCreateBadRequest("Customer does not exist"));
  }
}
