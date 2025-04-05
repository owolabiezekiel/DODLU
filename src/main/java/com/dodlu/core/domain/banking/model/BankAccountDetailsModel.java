package com.dodlu.core.domain.banking.model;

public record BankAccountDetailsModel(
    String username,
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
