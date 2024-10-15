package com.dodlu.core.app.model.request.savings;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.savings.enums.Currency;
import com.dodlu.core.domain.savings.enums.SavingFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class CreateGroupSavingRequest {
  @NotBlank(message = "name cannot be blank")
  private String name;

  @NotBlank(message = "description cannot be blank")
  private String description;

  @URL(message = "invalid group image url format")
  private String imageUrl;

  @NotNull(message = "savingFrequency is required")
  @ValidEnum(message = "invalid saving frequency")
  private SavingFrequency savingFrequency;

  @Positive(message = "saving amount must be greater than zero")
  @NotNull(message = "savingAmount is required")
  private BigDecimal savingAmount;

  @NotBlank(message = "meetingLocation is required")
  @Size(min = 3, message = "meeting location size must be greater than 3")
  private String meetingLocation;

  @NotNull(message = "currency is required")
  @ValidEnum(message = "invalid saving currency")
  private Currency currency;
}
