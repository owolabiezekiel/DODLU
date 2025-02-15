package com.dodlu.core.app.model.request.savings;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.savings.enums.Currency;
import com.dodlu.core.domain.savings.enums.SavingFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

  @NotBlank(message = "goals cannot be blank")
  private String goals;

  @NotBlank(message = "vision cannot be blank")
  private String vision;

  @NotBlank(message = "mission cannot be blank")
  private String mission;

  @NotBlank(message = "rules and regulations cannot be blank")
  private String rulesAndRegulations;

  @NotBlank(message = "membership requirements cannot be blank")
  private String membershipRequirements;

  private boolean loanSupport;

  @URL(message = "invalid group image url format")
  private String imageUrl;

  @NotNull(message = "savingFrequency is required")
  @ValidEnum(message = "invalid saving frequency")
  private SavingFrequency savingFrequency;

  @Positive(message = "saving amount must be greater than zero")
  @NotNull(message = "savingAmount is required")
  private BigDecimal savingAmount;

  @NotNull(message = "currency is required")
  @ValidEnum(message = "invalid saving currency")
  private Currency currency;
}
