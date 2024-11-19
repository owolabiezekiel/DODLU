package com.dodlu.core.domain.marketplace.model;

import com.dodlu.core.domain.marketplace.enums.MarketItemCategory;
import com.dodlu.core.domain.user.model.CustomerProfileModel;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record MarketPostModel(
    long id,
    String title,
    String photoUrl,
    BigDecimal price,
    String description,
    String availableUnit,
    MarketItemCategory category,
    CustomerProfileModel marketer) {}
