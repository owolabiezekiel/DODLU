package com.dodlu.core.app.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "email.templates")
public class EmailTemplatesConfiguration {
  private String newAccountEmailTemplate;
}
