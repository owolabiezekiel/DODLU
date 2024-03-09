package com.dodlu.core.domain.notification.email.model;

import com.dodlu.core.domain.notification.email.enums.EmailBodyType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailModel {
  private Long id;
  private UUID referenceId;
  private String sender;
  private String senderName;
  private String subject;
  private String body;
  private EmailBodyType bodyType;
  private Set<String> attachments;
  private Set<String> recipients;
  private Set<String> cc;
  private Set<String> bcc;
  private LocalDateTime scheduledDate;
  private LocalDateTime sentTime;
  private boolean sent;
  private boolean isTemplateContent;
  private String templateId;
  private Map<String, Object> templateData;
  @JsonIgnore private boolean deleted;
}
