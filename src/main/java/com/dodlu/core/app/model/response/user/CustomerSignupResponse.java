package com.dodlu.core.app.model.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@RequiredArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class CustomerSignupResponse {
  private String passcode;
}
