package com.dodlu.core.app.configuration;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component(value = "databaseAuditor")
@RequiredArgsConstructor
@Slf4j
public class DatabaseAuditor implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = "System";
    if (authentication != null) {
      username = authentication.getName();
    }
    return Optional.of(username);
  }
}
