package com.dodlu.core.app.configuration;

import com.dodlu.core.domain.kyc.service.KycService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class KycConfig {
  @Bean
  @ConditionalOnMissingBean(KycService.class)
  public KycService defaultProvider() {
    return new KycService() {};
  }
}
