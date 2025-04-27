package com.dodlu.core.domain.savings.model;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record LoanMetricModel(
    long loanDisbursedCount,
    long pendingLoanCount,
    long completedLoanCount,
    long inProgressLoanCount,
    BigDecimal totalLoanDisbursedAmount) {}
