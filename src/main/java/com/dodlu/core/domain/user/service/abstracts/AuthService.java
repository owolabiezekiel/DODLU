package com.dodlu.core.domain.user.service.abstracts;

import com.dodlu.core.app.model.request.user.CreateEmployerRequest;
import com.dodlu.core.app.model.request.user.CreateUserRequest;
import com.dodlu.core.app.model.request.user.UserLoginRequest;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.user.model.UserModel;
import java.util.concurrent.CompletableFuture;

public interface AuthService {
  CompletableFuture<UserModel> registerStudentUser(CreateUserRequest request);

  CompletableFuture<UserModel> registerEmployer(CreateEmployerRequest request);

  CompletableFuture<LoginResponseModel> userLogin(UserLoginRequest request);
}
