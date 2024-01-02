package com.dodlu.core.app.configuration;

import static java.time.Clock.systemDefaultZone;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SystemConfiguration {
  @Bean
  public Clock clock() {
    return systemDefaultZone();
  }
}
