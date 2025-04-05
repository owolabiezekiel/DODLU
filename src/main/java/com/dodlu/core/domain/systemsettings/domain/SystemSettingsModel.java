package com.dodlu.core.domain.systemsettings.domain;

import lombok.Builder;

@Builder
public record SystemSettingsModel(
    long id,
    int maxWrongPinAttempts,
    long minTransferAmount,
    boolean withdrawalEnabled,
    long withdrawalFeeChargeAmount,
    long dailyWithdrawalLimitWithBvn,
    long dailyWithdrawalLimitWithoutBvn,
    String anchorProvider) {}
