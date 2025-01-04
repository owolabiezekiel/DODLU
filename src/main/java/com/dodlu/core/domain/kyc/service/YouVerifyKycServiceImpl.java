package com.dodlu.core.domain.kyc.service;

import static com.dodlu.core.domain.utils.CustomerProfileUtils.getCustomerByPhoneNumber;
import static java.util.concurrent.CompletableFuture.runAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.kyc.model.YouVerifyBvnRequest;
import com.dodlu.core.domain.kyc.model.YouVerifyBvnResponse;
import com.dodlu.core.domain.kyc.model.YouVerifyConfig;
import com.dodlu.core.persistence.kyc.entity.BvnDetails;
import com.dodlu.core.persistence.kyc.repository.BVNDetailsRepository;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.dodlu.core.persistence.user.entity.CustomerVerification;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.CustomerProfileRepository;
import com.dodlu.core.persistence.user.repository.CustomerVerificationRepository;
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
  private final BVNDetailsRepository bvnDetailsRepository;
  private final CustomerVerificationRepository customerVerificationRepository;

  @Override
  public CompletableFuture<Void> verifyBVN(User user, String bvn) {
    return runAsync(
        () -> {
          CustomerProfile customerProfile =
              getCustomerByPhoneNumber(
                  customerProfileRepository, exceptionService, user.getPhoneNumber());
          YouVerifyBvnRequest request = new YouVerifyBvnRequest(bvn, true, false);
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

          log.info("response body: {}", responseBody);

          String status = responseBody.data().status();
          if (status.equalsIgnoreCase("not_found")
              || status.equalsIgnoreCase("not found")
              || status.startsWith("not")) {
            throw exceptionService.handleCreateBadRequest("BVN details not found");
          }

          log.info(
              "rFirst -> {}---------cFirst -> {}",
              responseBody.data().firstName(),
              customerProfile.getFirstName());
          log.info(
              "rLast -> {}----------cLast -> {}",
              responseBody.data().lastName(),
              customerProfile.getLastName());
          if (!responseBody.data().firstName().equalsIgnoreCase(customerProfile.getFirstName())) {
            throw exceptionService.handleCreateBadRequest("First name does not match");
          }

          if (!responseBody.data().lastName().equalsIgnoreCase(customerProfile.getLastName())) {
            throw exceptionService.handleCreateBadRequest("LastName name does not match");
          }

          BvnDetails bvnDetails =
              bvnDetailsRepository
                  .findByUser(user)
                  .orElse(BvnDetails.fromValidationResponse(responseBody, user));
          bvnDetailsRepository.save(bvnDetails);

          var verifications = customerProfile.getVerification();
          if (verifications == null) {
            verifications = new CustomerVerification();
            verifications
                .setIsNINVerified(false)
                .setIsBVNVerified(true)
                .setIsPassportVerified(false)
                .setIsDriverLicenseVerified(false)
                .setCustomer(customerProfile);
          }
          verifications.setIsBVNVerified(true);
          customerVerificationRepository.save(verifications);
        });
  }

  private HttpHeaders setupRequestHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("token", config.getApiKey());
    return headers;
  }
}
