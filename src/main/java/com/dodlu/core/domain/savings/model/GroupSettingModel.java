package com.dodlu.core.domain.savings.model;

import com.dodlu.core.domain.savings.enums.SavingFrequency;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record GroupSettingModel(
    BigDecimal minimumSavingAmount,
    SavingFrequency savingFrequency,
    Integer monthsBeforeLoan,
    Float highestLoanPercent,
    Float loanInterestPercent) {}
