package com.dodlu.core.app.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "whatsapp")
public class WhatsappConfiguration {
  private String businessAccountId;
  private String phoneNumberId;
  private String accessToken;
  private String verifyToken;
}
