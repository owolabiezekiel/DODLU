package com.dodlu.core.app.configuration;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms.provider.twilio")
@Data
public class TwilioConfiguration {
  private String accountSid;
  private String phoneNumber;
  private String authToken;
  private String callbackUrl;

  @PostConstruct
  public void twilio() {
    Twilio.init(accountSid, authToken);
  }
}
