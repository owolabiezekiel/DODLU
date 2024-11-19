package com.dodlu.core.app.model.request.marketplace;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.marketplace.enums.MarketItemCategory;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class UpdateMarketPostRequest {
  private String title;
  private String photoUrl;

  @PositiveOrZero(message = "price cannot be negative")
  private BigDecimal price;

  private String description;
  private String availableUnit;

  @ValidEnum(message = "invalid category")
  private MarketItemCategory category;
}
