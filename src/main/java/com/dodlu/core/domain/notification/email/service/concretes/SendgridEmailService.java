package com.dodlu.core.domain.notification.email.service.concretes;

import static com.dodlu.core.app.utils.Constants.DEFAULT_EMAIL_SENDER_NAME;
import static com.dodlu.core.app.utils.StringUtilities.emptyIfNullStream;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.notification.email.model.EmailModel;
import com.dodlu.core.domain.notification.email.service.abstracts.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@ConditionalOnProperty(prefix = "email.provider", name = "name", havingValue = "send-grid")
@RequiredArgsConstructor
@Service
@Slf4j
public class SendgridEmailService implements EmailService {
  private final SendGrid sendGrid;
  private final Clock clock;
  private final CoreExceptionService exceptionService;

  @Override
  public CompletableFuture<EmailModel> send(EmailModel emailModel) {
    log.info("using send grid to send email");
    return supplyAsync(() -> sendEmail(emailModel));
  }

  private EmailModel sendEmail(EmailModel emailModel) {
    Mail mail = buildEmail(emailModel);
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sendGrid.api(request);
      if (HttpStatus.valueOf(response.getStatusCode()).is2xxSuccessful()) {
        emailModel.setSent(true);
        emailModel.setSentTime(LocalDateTime.now(clock));
      } else {
        emailModel.setSent(false);
        log.error("Error making call to sendgrid -  {}", response.getBody());
      }
      return emailModel;
    } catch (IOException ex) {
      throw exceptionService.handleCreateException(ex.getMessage());
    }
  }

  private Mail buildEmail(EmailModel emailModel) {
    String sender =
        Strings.isNotBlank(emailModel.getSenderName())
            ? emailModel.getSenderName()
            : DEFAULT_EMAIL_SENDER_NAME;
    Email from = new Email(emailModel.getSender(), sender);
    String subject = emailModel.getSubject();
    Mail mail = new Mail();
    Personalization personalization = new Personalization();
    if (emailModel.isTemplateContent()) {
      if (Strings.isBlank(emailModel.getTemplateId())
          || emailModel.getTemplateData() == null
          || emailModel.getTemplateData().isEmpty()) {
        throw exceptionService.handleCreateException(
            "Template Content of Email Model is set to true but Template ID or Template Data isn't complete");
      }
      mail.setTemplateId(emailModel.getTemplateId().strip());
      emailModel.getTemplateData().forEach(personalization::addDynamicTemplateData);
      personalization.addDynamicTemplateData("subject", subject);
    } else {
      Content content =
          new Content(emailModel.getBodyType().getContentType(), emailModel.getBody());
      mail.addContent(content);
    }
    mail.setFrom(from);
    mail.setSubject(subject);
    TrackingSettings trackingSettings = new TrackingSettings();
    ClickTrackingSetting clickTrackingSetting = new ClickTrackingSetting();
    clickTrackingSetting.setEnable(true);
    clickTrackingSetting.setEnableText(false);
    OpenTrackingSetting openTrackingSetting = new OpenTrackingSetting();
    openTrackingSetting.setEnable(true);
    openTrackingSetting.setSubstitutionTag("%open-track%");
    SubscriptionTrackingSetting subscriptionTrackingSetting = new SubscriptionTrackingSetting();
    subscriptionTrackingSetting.setEnable(true);
    trackingSettings.setSubscriptionTrackingSetting(subscriptionTrackingSetting);
    trackingSettings.setOpenTrackingSetting(openTrackingSetting);
    trackingSettings.setClickTrackingSetting(clickTrackingSetting);
    mail.setTrackingSettings(trackingSettings);
    emailModel.getRecipients().forEach(recipient -> personalization.addTo(new Email(recipient)));
    emptyIfNullStream(emailModel.getCc())
        .forEach(recipient -> personalization.addCc(new Email(recipient)));
    emptyIfNullStream(emailModel.getBcc())
        .forEach(recipient -> personalization.addBcc(new Email(recipient)));
    mail.addPersonalization(personalization);
    return mail;
  }
}
