package com.dodlu.core.domain.user.service.abstracts;

import com.dodlu.core.app.model.request.user.ChangePasswordRequest;
import com.dodlu.core.app.model.request.user.CreateUserRequest;
import com.dodlu.core.app.model.request.user.RegisterEmployerRequest;
import com.dodlu.core.app.model.request.user.UserLoginRequest;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.user.model.UserModel;
import com.dodlu.core.persistence.user.entity.User;
import java.util.concurrent.CompletableFuture;

public interface AuthService {
  CompletableFuture<UserModel> registerStudentUser(CreateUserRequest request);

  CompletableFuture<UserModel> registerEmployer(RegisterEmployerRequest request);

  CompletableFuture<LoginResponseModel> userLogin(UserLoginRequest request);

  CompletableFuture<Void> changePassword(User user, ChangePasswordRequest request);
}
