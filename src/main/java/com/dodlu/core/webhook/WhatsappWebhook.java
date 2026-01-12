package com.dodlu.core.webhook;

import com.dodlu.core.app.configuration.WhatsappConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value = "/v1/webhook/whatsapp")
public class WhatsappWebhook {
  private final WhatsappConfiguration whatsappConfiguration;

  @GetMapping
  public ResponseEntity<String> handleSmsUpdate(
      @RequestParam(name = "hub.mode") String hubMode,
      @RequestParam(name = "hub.challenge") String hubChallenge,
      @RequestParam(name = "hub.verify_token") String hubVerifyToken
  ) {
    log.info("Received webhook verification request. Mode: '{}'", hubMode);
    if ("subscribe".equalsIgnoreCase(hubMode) && whatsappConfiguration.getVerifyToken().equals(hubVerifyToken)) {
      log.info("Webhook verification successful! Responding with challenge.");
      return ResponseEntity.ok(hubChallenge);
    } else {
      log.warn("Webhook verification failed. Mode or token did not match.");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }

  @GetMapping("/message-status-change")
  public ResponseEntity<String> handleMessageStatusChange(
      @RequestParam(name = "hub.mode") String hubMode,
      @RequestParam(name = "hub.challenge") String hubChallenge,
      @RequestParam(name = "hub.verify_token") String hubVerifyToken
  ) {
    log.info("Received webhook verification request. Mode: '{}'", hubMode);
    if ("subscribe".equalsIgnoreCase(hubMode) && whatsappConfiguration.getVerifyToken().equals(hubVerifyToken)) {
      log.info("Webhook verification successful! Responding with challenge.");
      return ResponseEntity.ok(hubChallenge);
    } else {
      log.warn("Webhook verification failed. Mode or token did not match.");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
  }
}