package com.dodlu.core.app.model.request.internship;

import com.dodlu.core.app.utils.validators.FutureDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;
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
public class CreateJobPostingRequest {
  @NotBlank(message = "title is required")
  @Size(min = 3, max = 200, message = "title must be between 3 - 200 characters")
  private String title;

  @NotBlank(message = "description is required")
  @Size(min = 10, message = "description must be greater than 10 characters")
  private String description;

  @NotNull(message = "durationInMonths is required")
  @Min(
      value = 2,
      message = "durationInMonths cannot be less than 2 months or greater than 6 months")
  @Max(
      value = 6,
      message = "durationInMonths cannot be less than 2 months or greater than 6 months")
  private Integer durationInMonths;

  @NotNull(message = "durationInMonths is required")
  @Min(value = 1, message = "numberOfOpenPositions cannot be less than 1")
  private Integer numberOfOpenPositions;

  @NotNull(message = "expirationDate is required")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @FutureDateTime(
      value = 2,
      message = "expiration days must not be less than 2 days from current date")
  private LocalDateTime expirationDate;

  @NotNull(message = "isResumeRequired is required")
  private Boolean isCgpaRequired;

  @NotNull(message = "isResumeRequired is required")
  private Boolean isResumeRequired;

  @NotNull(message = "isCoverLetterRequired is required")
  private Boolean isCoverLetterRequired;

  @Size(min = 1, message = "at least one eligible department is required")
  private Set<String> eligibleDepartments;
}
