package com.dodlu.core.persistence.transaction.entity;

import com.dodlu.core.domain.savings.enums.Currency;
import com.dodlu.core.domain.transaction.enums.EntityType;
import com.dodlu.core.domain.transaction.enums.TransactionType;
import com.dodlu.core.domain.transaction.model.TransactionModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Transaction that = (Transaction) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public TransactionModel toModel() {
    return TransactionModel.builder()
        .id(id)
        .senderId(senderId)
        .senderName(senderName)
        .senderType(senderType)
        .receiverId(receiverId)
        .receiverName(receiverName)
        .receiverType(receiverType)
        .currency(currency)
        .amount(amount)
        .reference(reference)
        .description(description)
        .status(status)
        .failureReason(failureReason)
        .type(type)
        .transactionDateTime(transactionDateTime)
        .build();
  }
}
