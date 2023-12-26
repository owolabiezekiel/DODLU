package com.dodlu.core.domain.utils;

import static java.lang.String.format;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.user.entity.Role;
import com.dodlu.core.persistence.user.repository.RoleRepository;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class RoleUtils {
  public static Role getRoleByName(
      RoleRepository roleRepository, CoreExceptionService exceptionService, String name) {
    return roleRepository
        .findByNameEqualsIgnoreCase(name)
        .orElseThrow(
            () -> exceptionService.handleCreateBadRequest(format("Could not get role %s", name)));
  }

  public static Role getRoleById(
      RoleRepository roleRepository, Supplier<RuntimeException> errorSupplier, String id) {
    return roleRepository.findById(id).orElseThrow(errorSupplier);
  }
}
