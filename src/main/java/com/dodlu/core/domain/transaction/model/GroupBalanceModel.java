package com.dodlu.core.domain.transaction.model;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record GroupBalanceModel(BigDecimal availableBalance) {}
