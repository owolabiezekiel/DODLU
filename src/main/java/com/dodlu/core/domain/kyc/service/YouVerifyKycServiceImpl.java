package com.dodlu.core.domain.kyc.service;

import static java.util.concurrent.CompletableFuture.runAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.kyc.model.YouVerifyConfig;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "verification", name = "active-provider", havingValue = "youverify")
public class YouVerifyKycServiceImpl implements KycService {
  private final CoreExceptionService exceptionService;
  private final RestTemplate restTemplate;
  private final YouVerifyConfig config;

  @Override
  public CompletableFuture<Void> verifyBVN() {
    return runAsync(
        () -> {
          log.info("Verifying with you-verify");
        });
  }
}
