package com.dodlu.core.persistence.savings.entity;

import com.dodlu.core.domain.savings.enums.SavingFrequency;
import com.dodlu.core.domain.savings.model.GroupSettingModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_setting")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@DynamicUpdate
public class GroupSetting extends BaseEntity {
  @OneToOne(optional = false)
  @JoinColumn(name = "group_savings_id", nullable = false, unique = true)
  @ToString.Exclude
  private GroupSaving groupSavings;

  private BigDecimal minimumSavingAmount;

  @Enumerated(EnumType.STRING)
  private SavingFrequency savingFrequency;

  private Integer monthsBeforeLoan;
  private Float highestLoanPercent;
  private Float loanInterestPercent;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    GroupSetting that = (GroupSetting) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public GroupSettingModel toModel() {
    return GroupSettingModel.builder()
        .minimumSavingAmount(minimumSavingAmount)
        .savingFrequency(savingFrequency)
        .monthsBeforeLoan(monthsBeforeLoan)
        .highestLoanPercent(highestLoanPercent)
        .loanInterestPercent(loanInterestPercent)
        .build();
  }
}
