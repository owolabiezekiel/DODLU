package com.dodlu.core.app.model.filters;

import com.dodlu.core.domain.marketplace.enums.MarketItemCategory;
import lombok.Builder;

@Builder
public record MarketPostFilter(String title, MarketItemCategory category) {}
