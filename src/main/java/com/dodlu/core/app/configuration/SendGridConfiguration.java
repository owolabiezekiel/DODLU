package com.dodlu.core.app.configuration;

import com.sendgrid.SendGrid;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "email.provider", name = "name", havingValue = "send-grid")
@Configuration
@ConfigurationProperties(prefix = "email.provider.sendgrid")
@Data
public class SendGridConfiguration {
  private String baseUrl;
  private String apiKey;

  @Bean
  public SendGrid sendGrid() {
    return new SendGrid(apiKey);
  }
}
