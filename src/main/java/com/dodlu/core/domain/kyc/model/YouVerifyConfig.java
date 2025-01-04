package com.dodlu.core.domain.kyc.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "verification.provider.you-verify")
@Data
public class YouVerifyConfig {
  private String apiKey;
  private String baseUrl;
  private String bvnUrl;
  private String ninUrl;
}
