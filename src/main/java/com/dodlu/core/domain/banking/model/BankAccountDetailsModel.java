package com.dodlu.core.domain.banking.model;

import com.dodlu.core.domain.banking.enums.BankUserType;
import lombok.Builder;

@Builder
public record BankAccountDetailsModel(
    String ownerId,
    BankUserType ownerType,
    boolean isActive,
    String virtualBankName,
    String virtualBankAccountName,
    String virtualBankAccountNumber,
    String currency,
    String status,
    boolean blocked,
    boolean accountUpdated,
    String bvn,
    long dailyLimit) {}
