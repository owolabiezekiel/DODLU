package com.dodlu.core.persistence.transaction.entity;

import com.dodlu.core.domain.savings.enums.Currency;
import com.dodlu.core.domain.transaction.enums.EntityType;
import com.dodlu.core.domain.transaction.enums.TransactionType;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transactions")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Transaction extends BaseEntity {
  @Column(nullable = false)
  private String senderId;

  @Column(nullable = false)
  private String senderName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EntityType senderType;

  @Column(nullable = false)
  private String receiverId;

  @Column(nullable = false)
  private String receiverName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EntityType receiverType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Currency currency;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private String reference;

  private String description;

  @Column(nullable = false)
  private String status;

  private String failureReason;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private TransactionType type;

  @Column(nullable = false)
  private LocalDateTime transactionDateTime;
}
