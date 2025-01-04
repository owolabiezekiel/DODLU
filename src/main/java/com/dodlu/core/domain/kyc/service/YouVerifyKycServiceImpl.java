package com.dodlu.core.domain.kyc.service;

import static com.dodlu.core.domain.utils.CustomerProfileUtils.getCustomerByPhoneNumber;
import static java.util.concurrent.CompletableFuture.runAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.kyc.model.YouVerifyBvnRequest;
import com.dodlu.core.domain.kyc.model.YouVerifyBvnResponse;
import com.dodlu.core.domain.kyc.model.YouVerifyConfig;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "verification", name = "active-provider", havingValue = "youverify")
public class YouVerifyKycServiceImpl implements KycService {
  private final CoreExceptionService exceptionService;
  private final RestTemplate restTemplate;
  private final YouVerifyConfig config;
  private final CustomerProfileRepository customerProfileRepository;

  @Override
  public CompletableFuture<Void> verifyBVN(User user, String bvn) {
    return runAsync(
        () -> {
          CustomerProfile customerProfile =
              getCustomerByPhoneNumber(
                  customerProfileRepository, exceptionService, user.getPhoneNumber());
          YouVerifyBvnRequest request = new YouVerifyBvnRequest(bvn, true, true);
          HttpHeaders headers = setupRequestHeaders();
          YouVerifyBvnResponse responseBody;

          HttpEntity<YouVerifyBvnRequest> entity = new HttpEntity<>(request, headers);
          try {
            ResponseEntity<YouVerifyBvnResponse> response =
                restTemplate.exchange(
                    config.getBvnUrl(), HttpMethod.POST, entity, YouVerifyBvnResponse.class);
            responseBody = response.getBody();
          } catch (HttpClientErrorException e) {
            log.error("Error: ", e);
            throw exceptionService.handleCreateBadRequest(e.getMessage());
          } catch (HttpServerErrorException e) {
            log.error("Error: ", e);
            throw exceptionService.handleCreateException("Service unavailable", e.getStatusCode());
          }

          if (!responseBody.data().firstName().equalsIgnoreCase(customerProfile.getFirstName())) {
            throw exceptionService.handleCreateBadRequest("First name does not match");
          }

          if (!responseBody.data().lastName().equalsIgnoreCase(customerProfile.getLastName())) {
            throw exceptionService.handleCreateBadRequest("LastName name does not match");
          }
        });
  }

  private HttpHeaders setupRequestHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("token", config.getApiKey());
    return headers;
  }
}
