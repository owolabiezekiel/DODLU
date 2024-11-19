package com.dodlu.core.app.model.request.marketplace;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.marketplace.enums.MarketItemCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateMarketPostRequest {
  @NotBlank(message = "title is required")
  private String title;

  @NotBlank(message = "photoUrl is required")
  private String photoUrl;

  @NotNull(message = "price is required")
  @PositiveOrZero(message = "price cannot be negative")
  private BigDecimal price;

  private String description;

  @NotBlank(message = "availableUnit is required")
  private String availableUnit;

  @NotNull(message = "category is required")
  @ValidEnum(message = "invalid category")
  private MarketItemCategory category;
}
