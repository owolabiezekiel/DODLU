package com.dodlu.core.domain.notification.email.service.concretes;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.domain.notification.email.model.EmailModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
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

  public CompletableFuture<EmailModel> sendNewAccountEmail(Map<String, Object> emailData) {
    log.info("using java message sender to send email");
    return supplyAsync(
        () -> {
          MimeMessage mimeMessage = mailSender.createMimeMessage();
          try {
            Template t = freemarkerConfig.getTemplate("new-account.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, emailData);

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("business.tobiloba@gmail.com");
            mimeMessageHelper.setTo("owo.ezekiel@gmail.com");
            mimeMessageHelper.setSubject("Welcome on board");
            mimeMessageHelper.setText(html, true);

            refineCheck(mimeMessageHelper, null, null, null);

            mailSender.send(mimeMessage);
          } catch (Exception e) {
            log.error(e.getMessage());
          }
          return null;
        });
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
