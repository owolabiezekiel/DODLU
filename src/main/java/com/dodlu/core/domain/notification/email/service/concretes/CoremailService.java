package com.dodlu.core.domain.notification.email.service.concretes;

import static com.dodlu.core.domain.utils.UserUtils.mapUserTypeToString;

import com.dodlu.core.app.configuration.EmailTemplatesConfiguration;
import com.dodlu.core.domain.notification.email.enums.EmailBodyType;
import com.dodlu.core.domain.notification.email.model.EmailModel;
import com.dodlu.core.domain.notification.email.service.abstracts.EmailService;
import com.dodlu.core.domain.user.model.UserType;
import com.dodlu.core.persistence.user.entity.EmployerProfile;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoremailService {
  private final EmailService emailService;
  private final EmailTemplatesConfiguration templatesConfiguration;
  private final String senderString = "Industrial Training Management System";

  public void sendNewAccountEmail(
      String email, String username, String password, UserType userType) {
    Map<String, Object> emailData = new HashMap<>();
    emailData.put("username", username);
    emailData.put("password", password);
    emailData.put("userType", mapUserTypeToString(userType));

    EmailModel emailModel =
        EmailModel.builder()
            .sender(senderString)
            .senderName(senderString)
            .bodyType(EmailBodyType.HTML)
            .recipients(Set.of(email))
            .templateData(emailData)
            .templateId(templatesConfiguration.getNewAccountEmailTemplate())
            .isTemplateContent(true)
            .build();

    emailService.send(emailModel);
  }

  public void sendEmployerWelcomeEmail(EmployerProfile employerProfile) {
    Map<String, Object> emailData = new HashMap<>();

    EmailModel emailModel =
        EmailModel.builder()
            .sender(senderString)
            .senderName(senderString)
            .bodyType(EmailBodyType.HTML)
            .recipients(Set.of(employerProfile.getEmail()))
            .templateData(emailData)
            .templateId(templatesConfiguration.getNewAccountEmailTemplate())
            .build();

    emailService.send(emailModel);
  }
}
