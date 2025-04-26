package com.dodlu.core.persistence.transaction.entity;

import com.dodlu.core.domain.transaction.model.GroupBalanceModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.savings.entity.GroupSaving;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_balance")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class GroupBalance extends BaseEntity {
  @Column(nullable = false)
  private BigDecimal availableBalance;

  @Column(nullable = false)
  private BigDecimal totalCreditAmount;

  @Column(nullable = false)
  private BigDecimal totalDebitAmount;

  @Column(nullable = false)
  private Long totalDebitTransactionCount;

  @Column(nullable = false)
  private Long totalCreditTransactionCount;

  @OneToOne(optional = false)
  @JoinColumn(name = "group_savings_id", nullable = false, unique = true)
  @ToString.Exclude
  private GroupSaving groupSavings;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    GroupBalance that = (GroupBalance) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public GroupBalanceModel toModel() {
    return GroupBalanceModel.builder()
        .availableBalance(this.availableBalance)
        .totalCreditAmount(this.totalCreditAmount)
        .totalDebitAmount(this.totalDebitAmount)
        .totalCreditTransactionCount(this.totalCreditTransactionCount)
        .totalDebitTransactionCount(this.totalDebitTransactionCount)
        .totalTransactionCount(this.totalCreditTransactionCount + this.totalDebitTransactionCount)
        .build();
  }
}
