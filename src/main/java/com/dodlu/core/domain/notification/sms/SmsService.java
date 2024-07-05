package com.dodlu.core.domain.notification.sms;

import com.dodlu.core.app.model.request.notification.SmsRequest;
import java.util.concurrent.CompletableFuture;

public interface SmsService {
  CompletableFuture<Void> send(SmsRequest request);
}
