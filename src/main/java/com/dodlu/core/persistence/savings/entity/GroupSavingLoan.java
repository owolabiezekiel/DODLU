package com.dodlu.core.persistence.savings.entity;

import com.dodlu.core.domain.savings.enums.LoanStatus;
import com.dodlu.core.domain.savings.enums.RepaymentStatus;
import com.dodlu.core.domain.savings.model.GroupSavingLoanModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_saving_loans")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@DynamicUpdate
public class GroupSavingLoan extends BaseEntity {
  @ManyToOne(optional = false)
  @JoinColumn(name = "lending_group_id", referencedColumnName = "id")
  @ToString.Exclude
  private GroupSaving lendingGroup;

  @ManyToOne(optional = false)
  @JoinColumn(name = "borrower_id", referencedColumnName = "id")
  @ToString.Exclude
  private CustomerProfile borrower;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private LoanStatus loanStatus;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private RepaymentStatus repaymentStatus;

  private LocalDateTime approvalDate;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private BigDecimal interest;

  @Column(nullable = false)
  private BigDecimal repaymentAmount;

  @Column(nullable = false)
  private BigDecimal amountRepaid;

  @Column(nullable = false)
  private Float interestPercent;

  private String comment;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    GroupSavingLoan that = (GroupSavingLoan) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public GroupSavingLoanModel toModel() {
    return GroupSavingLoanModel.builder()
        .id(id)
        .lendingGroup(lendingGroup.toModel())
        .borrower(borrower.toModel())
        .loanStatus(loanStatus)
        .repaymentStatus(repaymentStatus)
        .approvalDate(approvalDate)
        .amount(amount)
        .interest(interest)
        .repaymentAmount(repaymentAmount)
        .amountRepaid(amountRepaid)
        .interestPercent(interestPercent)
        .comment(comment)
        .build();
  }
}
