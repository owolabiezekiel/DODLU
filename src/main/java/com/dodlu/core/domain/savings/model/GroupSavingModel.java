package com.dodlu.core.domain.savings.model;

import com.dodlu.core.domain.user.model.CustomerProfileModel;
import java.util.Set;
import lombok.Builder;

@Builder
public record GroupSavingModel(
    String id, String name, CustomerProfileModel admin, Set<CustomerProfileModel> members) {}
