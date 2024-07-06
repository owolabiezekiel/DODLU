package com.dodlu.core.domain.notification.sms;

import static java.util.concurrent.CompletableFuture.runAsync;

import com.dodlu.core.app.configuration.TwilioConfiguration;
import com.dodlu.core.app.model.request.notification.SmsRequest;
import com.dodlu.core.persistence.notification.entity.Sms;
import com.dodlu.core.persistence.notification.repository.SmsRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwilioSmsService implements SmsService {
  private final TwilioConfiguration twilioConfiguration;
  private final SmsRepository smsRepository;

  @Override
  public CompletableFuture<Void> send(SmsRequest request) {
    return runAsync(
        () -> {
          log.info("Sending sms to {} from twilio", request.getNumber());
          PhoneNumber from = new PhoneNumber(twilioConfiguration.getPhoneNumber());
          PhoneNumber to = new PhoneNumber(request.getNumber());

          Message message =
              Message.creator(to, from, request.getMessage())
                  .setStatusCallback(URI.create(twilioConfiguration.getCallbackUrl()))
                  .create();
          log.info("{}", message);
          smsRepository.save(buildSms(message));
        });
  }

  private Sms buildSms(Message message) {
    return Sms.builder()
        .smsId(message.getSid())
        .sender(message.getFrom().toString())
        .recipient(message.getTo())
        .text(message.getBody())
        .status(message.getStatus().toString())
        .build();
  }
}
