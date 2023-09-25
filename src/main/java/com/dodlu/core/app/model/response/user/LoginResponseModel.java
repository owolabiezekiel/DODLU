package com.dodlu.core.app.model.response.user;

import com.dodlu.core.domain.user.model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginResponseModel {
  private UserModel user;
  private String accessToken;
}
