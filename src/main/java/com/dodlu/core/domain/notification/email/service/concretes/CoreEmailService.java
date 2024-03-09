package com.dodlu.core.domain.notification.email.service.concretes;

import static com.dodlu.core.domain.utils.UserUtils.mapUserTypeToString;

import com.dodlu.core.app.configuration.EmailTemplatesConfiguration;
import com.dodlu.core.domain.notification.email.enums.EmailBodyType;
import com.dodlu.core.domain.notification.email.model.EmailModel;
import com.dodlu.core.domain.notification.email.service.abstracts.EmailService;
import com.dodlu.core.domain.user.model.UserType;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreEmailService {
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

  public void sendEmployerWelcomeEmail() {
    Email from = new Email("owo.ezekiel@gmail.com");
    String subject = "Sending with SendGrid is Fun";
    Email to = new Email("business.tobiloba@gmail.com");
    Content content = new Content("text/html", "and easy to do anywhere, even with Java");
    Mail mail = new Mail(from, subject, to, content);
    mail.setTemplateId(templatesConfiguration.getNewAccountEmailTemplate());
    Map<String, Object> emailData = new HashMap<>();
    emailData.put("username", "Tobijava");
    emailData.put("password", "Sample-very random password");
    emailData.put("userType", mapUserTypeToString(UserType.STUDENT));
    Personalization personalization = new Personalization();
    emailData.forEach(personalization::addDynamicTemplateData);
    personalization.addDynamicTemplateData("subject", subject);

    SendGrid sg =
        new SendGrid("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");
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
    } catch (IOException ex) {
      log.info("{}", ex.getCause().toString());
      throw new RuntimeException("Something went wrong");
    }
  }
}
