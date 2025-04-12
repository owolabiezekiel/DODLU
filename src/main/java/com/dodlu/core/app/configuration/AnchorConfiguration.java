package com.dodlu.core.app.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "dodlu.anchor")
public class AnchorConfiguration {
  private String apiKey;
  private String fboParentAccountId;
  private String revenueAccountId;
  private String settlementAccountId;
  private String masterAccountId;
  private AnchorUrls urls;

  @Data
  public static class AnchorUrls {
    private String baseUrl;
    private String customerUrl;
    private String accountUrl;
    private String subAccountUrl;
    private String virtualNubanUrl;
    private String bookTransferUrl;
    private String bankVerificationUrl;
    private String feeUrl;
    private String bvnValidationUrl;
    private String billsUrl;
    private String billerUrl;
  }
}
