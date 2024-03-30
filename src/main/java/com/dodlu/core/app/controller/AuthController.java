package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.user.*;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.user.model.UserModel;
import com.dodlu.core.domain.user.service.abstracts.AuthService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class AuthController {
  private final AuthService authService;

  @PostMapping(value = "/student/register", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for students to register")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = UserModel.class)})
  public CompletableFuture<ResponseModel<UserModel>> studentRegister(
      @Valid @RequestBody CreateUserRequest request) {
    return authService.registerStudentUser(request).thenApply(ResponseModel::new);
  }

  @PostMapping(value = "/employer/register", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for employers to register")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = UserModel.class)})
  public CompletableFuture<ResponseModel<UserModel>> employerRegister(
      @Valid @RequestBody RegisterEmployerRequest request) {
    return authService.registerEmployer(request).thenApply(ResponseModel::new);
  }

  @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint to login")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = LoginResponseModel.class)
  })
  public CompletableFuture<ResponseModel<LoginResponseModel>> login(
      @Valid @RequestBody UserLoginRequest request) {
    return authService.userLogin(request).thenApply(ResponseModel::new);
  }

  @PostMapping(value = "/change-password", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for users to change their password")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public CompletableFuture<ResponseModel<Void>> changePassword(
      @AuthenticationPrincipal User user, @Valid @RequestBody ChangePasswordRequest request) {
    return authService.changePassword(user, request).thenApply(ResponseModel::new);
  }
  @PostMapping(value = "/reset-password/initiate", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Initiate forgot password")
  public CompletableFuture<ResponseModel<UserModel>> initiateForgotPassword(
          @Valid @RequestBody EmailModel emailModel) {
    return authService
            .initiatePasswordReset(emailModel)
            .thenApply(ResponseModel::new);
  }

  @PostMapping(value = "/reset-password/update", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Reset password")
  public CompletableFuture<ResponseModel<UserModel>> resetPassword(
          @Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest) {
    return authService
            .resetPassword(forgetPasswordRequest)
            .thenApply(ResponseModel::new);
  }
}
