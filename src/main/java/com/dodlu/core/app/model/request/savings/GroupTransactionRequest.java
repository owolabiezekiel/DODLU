package com.dodlu.core.app.model.request.savings;

import jakarta.validation.constraints.*;
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
public class GroupTransactionRequest {
  @NotNull(message = "amount is required")
  @Positive(message = "amount must be greater than zero")
  private BigDecimal amount;

  @NotEmpty(message = "description is required")
  private String description;

  @NotEmpty(message = "memberId is required")
  private String memberId;

  //  @NotNull(message = "category is required")
  //  @ValidEnum(message = "invalid category")
  //  private TransactionCategory category;
}
