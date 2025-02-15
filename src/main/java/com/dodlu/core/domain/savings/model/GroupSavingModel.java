package com.dodlu.core.domain.savings.model;

import com.dodlu.core.domain.transaction.model.GroupBalanceModel;
import com.dodlu.core.domain.user.model.CustomerProfileModel;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Builder;

@Builder
public record GroupSavingModel(
    String id,
    String name,
    String goals,
    String vision,
    String mission,
    String rulesAndRegulations,
    String membershipRequirements,
    boolean loanSupport,
    CustomerProfileModel admin,
    String url,
    BigDecimal leastSavingAmount,
    String savingFrequency,
    String currency,
    GroupBalanceModel balance,
    GroupSettingModel settings,
    Set<CustomerProfileModel> members) {}
