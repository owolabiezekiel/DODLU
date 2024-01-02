package com.dodlu.core.domain.email.service.abstracts;

import com.dodlu.core.domain.email.model.EmailModel;
import java.util.concurrent.CompletableFuture;

public interface EmailService {
  CompletableFuture<EmailModel> send(EmailModel sendNow);
}
