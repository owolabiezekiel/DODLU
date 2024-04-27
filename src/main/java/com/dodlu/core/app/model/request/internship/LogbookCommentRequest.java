package com.dodlu.core.app.model.request.internship;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class LogbookCommentRequest {
  @NotNull(message = "weekNumber is required")
  @PositiveOrZero(message = "weekNumber must be a positive number")
  private Integer weekNumber;

  @NotBlank(message = "comment is required")
  private String comment;
}
