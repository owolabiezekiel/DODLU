package com.dodlu.core.app.model.request.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailModel {
    @Email(message = "Email is invalid")
    @NotBlank(message = "Email is required")
    private String email;
}
