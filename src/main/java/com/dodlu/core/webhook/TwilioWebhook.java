package com.dodlu.core.webhook;

import static java.util.concurrent.CompletableFuture.runAsync;
import static org.springframework.http.MediaType.*;

import com.dodlu.core.persistence.notification.repository.SmsRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/v1/webhook/twilio", produces = APPLICATION_JSON_VALUE)
public class TwilioWebhook {
  private final SmsRepository smsRepository;

  @PostMapping(value = "/sms-update", consumes = APPLICATION_FORM_URLENCODED_VALUE)
  public CompletableFuture<Void> handleSmsUpdate(
      @RequestBody MultiValueMap<String, String> payload) {
    return runAsync(
        () -> {
          log.info("Processing sms update from twilio");
          String smsSid = payload.getFirst("SmsSid");
          String status = payload.getFirst("MessageStatus");

          smsRepository
              .findBySmsId(smsSid)
              .ifPresent(
                  data -> {
                    log.info(
                        "Updating the status of sms with smsId: {} from {} to {}",
                        smsSid,
                        data.getStatus(),
                        status);
                    data.setStatus(status);
                    smsRepository.save(data);
                  });
        });
  }
}
