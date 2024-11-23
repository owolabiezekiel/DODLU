package com.dodlu.core.app.model.request.savings;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class GroupLoanRequest {
  @NotEmpty(message = "groupId is required")
  private String groupId;

  @NotNull(message = "amount is required")
  @Positive(message = "amount must be greater than zero")
  private BigDecimal amount;
}
