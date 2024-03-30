package com.dodlu.core.domain.user.service.abstracts;

import com.dodlu.core.app.model.request.user.*;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.user.model.UserModel;
import com.dodlu.core.persistence.user.entity.User;
import java.util.concurrent.CompletableFuture;

public interface AuthService {
  CompletableFuture<UserModel> registerStudentUser(CreateUserRequest request);

  CompletableFuture<UserModel> registerEmployer(RegisterEmployerRequest request);

  CompletableFuture<LoginResponseModel> userLogin(UserLoginRequest request);

  CompletableFuture<Void> changePassword(User user, ChangePasswordRequest request);
  CompletableFuture<UserModel> initiatePasswordReset(EmailModel emailModel);
  CompletableFuture<UserModel> resetPassword(ForgetPasswordRequest request );
}
