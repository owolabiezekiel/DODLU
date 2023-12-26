package com.dodlu.core.app.model.request.user;

import com.dodlu.core.app.utils.validators.ValidEnum;
import com.dodlu.core.domain.user.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateSupervisorRequest {
  @Size(min = 3, max = 100, message = "username must be between 3 - 100 characters")
  private String username;

  @NotBlank(message = "email is required")
  @Email(message = "email not valid")
  private String email;

  @NotBlank(message = "phoneNumber is required")
  @Size(min = 11, message = "phoneNumber must be at least 11 characters")
  private String phoneNumber;

  @NotBlank(message = "firstName is required")
  @Size(min = 3, max = 150, message = "firstName must be between 3 - 150 characters")
  private String firstName;

  @NotBlank(message = "lastName is required")
  @Size(min = 3, max = 150, message = "firstName must be between 3 - 150 characters")
  private String lastName;

  @NotBlank(message = "title is required")
  @Size(min = 2, max = 30, message = "title must be between 2 - 30 characters")
  private String title;

  @NotNull(message = "gender is required")
  @ValidEnum(message = "invalid gender")
  private Gender gender;
}
