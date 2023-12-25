package com.dodlu.core.domain.utils;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.UserRepository;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class UserUtils {

  public static User getUser(
      UserRepository userRepository, CoreExceptionService exceptionService, String id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> exceptionService.handleCreateNotFoundException("user does not exist"));
  }

  public static User getUser(
      UserRepository userRepository, String id, Supplier<RuntimeException> errorSupplier) {
    return userRepository.findById(id).orElseThrow(errorSupplier);
  }

  public static User getUserOrNull(UserRepository userRepository, String id) {
    return userRepository.findById(id).orElse(null);
  }
}
