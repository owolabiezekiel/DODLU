package com.dodlu.core.domain.savings.model;

import com.dodlu.core.domain.user.model.CustomerProfileModel;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Builder;

@Builder
public record GroupSavingModel(
    String id,
    String name,
    String description,
    CustomerProfileModel admin,
    String url,
    Set<CustomerProfileModel> members,
    @Column(nullable = false) BigDecimal leastSavingAmount,
    String savingFrequency,
    String meetingLocation,
    String currency) {}
