package com.dodlu.core.domain.user.service.abstracts;

import com.dodlu.core.app.model.request.user.*;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.user.model.UserModel;
import com.dodlu.core.persistence.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

public interface AuthService {
  CompletableFuture<UserModel> registerStudentUser(CreateUserRequest request);

  CompletableFuture<Void> registerCustomer(CreateCustomerRequest request);

  CompletableFuture<UserModel> registerEmployer(RegisterEmployerRequest request);

  CompletableFuture<LoginResponseModel> userLogin(
      UserLoginRequest request, HttpServletResponse response);

  CompletableFuture<Void> changePassword(User user, ChangePasswordRequest request);

  CompletableFuture<Void> verifyPhone(VerifyPhoneRequest request);

  CompletableFuture<Void> requestPasswordReset(String email);

  CompletableFuture<Void> completePasswordReset(ResetPasswordRequest request);

  CompletableFuture<Void> resendPhoneVerification(ResendPhoneVerificationRequest request);
}
