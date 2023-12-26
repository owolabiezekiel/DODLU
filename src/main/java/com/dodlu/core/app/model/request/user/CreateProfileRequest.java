package com.dodlu.core.app.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@SuperBuilder
public class CreateProfileRequest {
  @Size(min = 3, max = 100, message = "username must be between 3 - 100 characters")
  @Pattern(
      regexp = "^[a-zA-Z][a-zA-Z0-9_-]*$",
      message =
          "Username must start with a letter and can only contain letters, digits, hyphens, and underscores.")
  protected String username;

  @NotBlank(message = "email is required")
  @Email(message = "email not valid")
  protected String email;

  @NotBlank(message = "phoneNumber is required")
  @Size(min = 11, message = "phoneNumber must be at least 11 characters")
  protected String phoneNumber;

  @NotBlank(message = "firstName is required")
  @Size(min = 3, max = 150, message = "firstName must be between 3 - 150 characters")
  protected String firstName;

  @NotBlank(message = "lastName is required")
  @Size(min = 3, max = 150, message = "firstName must be between 3 - 150 characters")
  protected String lastName;
}
