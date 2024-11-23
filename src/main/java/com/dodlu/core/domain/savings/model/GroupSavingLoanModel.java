package com.dodlu.core.domain.savings.model;

import com.dodlu.core.domain.savings.enums.LoanStatus;
import com.dodlu.core.domain.savings.enums.RepaymentStatus;
import com.dodlu.core.domain.user.model.CustomerProfileModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GroupSavingLoanModel(
    String id,
    @JsonIgnoreProperties({"members"}) GroupSavingModel lendingGroup,
    CustomerProfileModel borrower,
    LoanStatus loanStatus,
    RepaymentStatus repaymentStatus,
    LocalDateTime approvalDate,
    BigDecimal amount,
    BigDecimal interest,
    BigDecimal repaymentAmount,
    BigDecimal amountRepaid,
    Float interestPercent,
    String comment) {}
