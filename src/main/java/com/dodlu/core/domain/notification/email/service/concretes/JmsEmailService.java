package com.dodlu.core.domain.notification.email.service.concretes;

import static com.dodlu.core.domain.utils.UserUtils.mapUserTypeToString;
import static java.util.concurrent.CompletableFuture.runAsync;

import com.dodlu.core.domain.notification.email.enums.EmailBodyType;
import com.dodlu.core.domain.notification.email.model.EmailModel;
import com.dodlu.core.domain.user.model.UserType;
import com.dodlu.core.persistence.notification.entity.Email;
import com.dodlu.core.persistence.notification.repository.EmailRepository;
import com.dodlu.core.persistence.user.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@RequiredArgsConstructor
@Service
@Slf4j
public class JmsEmailService {
  private final Configuration freemarkerConfig;
  private final JavaMailSender mailSender;
  private final EmailRepository emailRepository;
  private final String senderEmail = "welcome@itrams.com";

  public CompletableFuture<Void> send(EmailModel emailModel) {
    log.info("using java message sender to send email");
    Map<String, Object> emailData = emailModel.getTemplateData();
    return runAsync(
        () -> {
          MimeMessage mimeMessage = mailSender.createMimeMessage();
          try {
            Template t = freemarkerConfig.getTemplate(emailModel.getTemplateId());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, emailData);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(emailModel.getRecipients().toArray(new String[0]));
            mimeMessageHelper.setSubject(emailModel.getSubject());
            mimeMessageHelper.setText(html, true);

            refineCheck(
                mimeMessageHelper,
                emailModel.getCc(),
                emailModel.getBcc(),
                emailModel.getAttachments());

            Email email = Email.toEntity(emailModel);
            email.setSender(senderEmail);
            email.setSentTime(LocalDateTime.now());
            email.setBodyType(EmailBodyType.HTML);
            email = emailRepository.save(email);

            mailSender.send(mimeMessage);
            log.info("Finished sending email, now setting saved to true");
            email.setSent(true);
            emailRepository.save(email);
          } catch (Exception e) {
            log.error(e.getMessage());
          }
        });
  }

  public void sendNewAccountEmail(
      String email, String password, String username, UserType userType) {
    log.info("Sending new account for type: {}", mapUserTypeToString(userType));
    Map<String, Object> emailData = new HashMap<>();
    emailData.put("username", username);
    emailData.put("password", password);
    emailData.put("userType", mapUserTypeToString(userType));

    EmailModel emailModel =
        EmailModel.builder()
            .templateData(emailData)
            .templateId("new-account.ftl")
            .recipients(Set.of(email))
            .subject("Welcome on board")
            .build();

    send(emailModel);
  }
  public void sendResetPasswordTokenEmail(String email, String username,String token){
    String resetLink = "";
    log.info("Sending reset password token for {}", email);
    Map<String, Object> emailData = new HashMap<>();
    emailData.put("username", username);
    emailData.put("resetToken", token);
    emailData.put("resetLink", resetLink);

    EmailModel emailModel =
            EmailModel.builder()
                    .templateData(emailData)
                    .templateId("reset-password.ftl")
                    .recipients(Set.of(email))
                    .subject("Reset Password")
                    .build();

    send(emailModel);

  }

  public void refineCheck(
      MimeMessageHelper mimeMessageHelper,
      Set<String> cc,
      Set<String> bcc,
      Set<String> attachments) {
    if (cc != null) {
      try {
        mimeMessageHelper.setCc(cc.toArray(new String[0]));
      } catch (MessagingException e) {
        log.error(e.getMessage());
      }
    }

    if (bcc != null) {
      try {
        mimeMessageHelper.setBcc(bcc.toArray(new String[0]));
      } catch (MessagingException e) {
        log.error(e.getMessage());
      }
    }

    if (attachments != null) {
      FileSystemResource fileSystemResource;
      for (String filePath : attachments) {
        fileSystemResource = new FileSystemResource(new File(filePath));
        if (fileSystemResource.exists()) {
          try {
            mimeMessageHelper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            log.info("Added file " + fileSystemResource.getFilename() + " successfully");
          } catch (MessagingException e) {
            log.error(fileSystemResource.getFilename() + ". Aborting!!!\n" + e.getMessage());
          }
        } else {
          log.error(fileSystemResource.getFilename() + "!!!");
        }
      }
    }
  }
}
