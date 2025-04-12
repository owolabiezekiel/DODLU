package com.dodlu.core.persistence.savings.entity;

import com.dodlu.core.persistence.base.LongBaseEntity;
import jakarta.persistence.*;
import java.io.Serializable;
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
@Table(name = "group_invite_code")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class GroupInviteCode extends LongBaseEntity implements Serializable {
  @Column(nullable = false)
  private String customerId;

  @Column(nullable = false)
  private String groupId;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private LocalDateTime nextReminder;
}
