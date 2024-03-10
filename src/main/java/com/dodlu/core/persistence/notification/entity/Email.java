package com.dodlu.core.persistence.notification.entity;

import static com.dodlu.core.app.utils.StringUtilities.toStringFromSet;

import com.dodlu.core.domain.notification.email.enums.EmailBodyType;
import com.dodlu.core.domain.notification.email.model.EmailModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "emails")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Email extends BaseEntity {
  private String sender;
  private String subject;
  private String body;

  @Enumerated(EnumType.STRING)
  private EmailBodyType bodyType;

  private String attachments;
  private String recipients;
  private String cc;
  private String bcc;
  private LocalDateTime scheduledDate;
  private LocalDateTime sentTime;
  private boolean sent;

  public static Email toEntity(EmailModel model) {
    return Email.builder()
        .sender(model.getSender())
        .body(model.getTemplateId())
        .subject(model.getSubject())
        .bodyType(model.getBodyType())
        .attachments(toStringFromSet(model.getAttachments()))
        .recipients(toStringFromSet(model.getRecipients()))
        .cc(toStringFromSet(model.getCc()))
        .bcc(toStringFromSet(model.getBcc()))
        .scheduledDate(model.getScheduledDate())
        .sentTime(model.getSentTime())
        .sent(model.isSent())
        .build();
  }
}
