package com.dodlu.core.persistence.systemsettings.entity;

import com.dodlu.core.domain.systemsettings.domain.SystemSettingsModel;
import com.dodlu.core.persistence.base.LongBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import java.util.Objects;
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
@Table(name = "system_settings")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class SystemSettings extends LongBaseEntity {
  @Column(nullable = false)
  private Integer maxWrongPinAttempts;

  @Column(nullable = false)
  private Long minTransferAmount;

  @Column(nullable = false)
  private Boolean withdrawalEnabled;

  @Column(nullable = false)
  private Long withdrawalFeeChargeAmount;

  @Column(nullable = false)
  private Long dailyWithdrawalLimitWithBvn;

  @Column(nullable = false)
  private Long dailyWithdrawalLimitWithoutBvn;

  @Column(nullable = false)
  private String anchorProvider;

  @Column(nullable = false)
  private Boolean bankAccountActivationEnabled;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    SystemSettings that = (SystemSettings) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public SystemSettingsModel toModel() {
    return SystemSettingsModel.builder()
        .id(id)
        .maxWrongPinAttempts(maxWrongPinAttempts)
        .minTransferAmount(minTransferAmount)
        .withdrawalEnabled(withdrawalEnabled)
        .withdrawalFeeChargeAmount(withdrawalFeeChargeAmount)
        .dailyWithdrawalLimitWithBvn(dailyWithdrawalLimitWithBvn)
        .dailyWithdrawalLimitWithoutBvn(dailyWithdrawalLimitWithoutBvn)
        .bankAccountActivationEnabled(bankAccountActivationEnabled)
        .anchorProvider(anchorProvider)
        .build();
  }
}
