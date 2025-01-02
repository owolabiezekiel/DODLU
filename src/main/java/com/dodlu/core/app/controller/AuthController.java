package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.user.*;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.user.service.abstracts.AuthService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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

  @PostMapping(value = "/customer/register", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for customers to register")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> customerRegister(@Valid @RequestBody CreateCustomerRequest request) {
    return authService.registerCustomer(request).thenApply(ResponseModel::new).join();
  }

  @PostMapping(value = "/customer/resend-otp", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for customers to request OTP resend")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> resendPhoneVerification(
      @Valid @RequestBody SendPhoneOtpRequest request) {
    return authService.resendPhoneVerification(request).thenApply(ResponseModel::new).join();
  }

  @PostMapping(value = "/customer/verify-phone", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for customers to verify phone number")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> customerVerifyPhone(@Valid @RequestBody VerifyPhoneRequest request) {
    return authService.verifyPhone(request).thenApply(ResponseModel::new).join();
  }

  @PostMapping(value = "/customer/request-password-reset")
  @Operation(description = "Endpoint for customers to request password reset")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> customerRequestPasswordReset(
      @Valid @RequestBody SendPhoneOtpRequest request) {
    return authService.requestPasswordReset(request).thenApply(ResponseModel::new).join();
  }

  @PostMapping(value = "/customer/complete-password-reset", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for customers to complete password reset")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> customerCompletePasswordReset(
      @Valid @RequestBody ResetPasswordRequest request) {
    return authService.completePasswordReset(request).thenApply(ResponseModel::new).join();
  }

  //  @PostMapping(value = "/employer/register", consumes = APPLICATION_JSON_VALUE)
  //  @Operation(description = "Endpoint for employers to register")
  //  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = UserModel.class)})
  //  public ResponseModel<UserModel> employerRegister(
  //      @Valid @RequestBody RegisterEmployerRequest request) {
  //    return authService.registerEmployer(request).thenApply(ResponseModel::new).join();
  //  }

  @PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint to login")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = LoginResponseModel.class)
  })
  public CompletableFuture<ResponseModel<LoginResponseModel>> login(
      @Valid @RequestBody UserLoginRequest request, HttpServletResponse response) {
    return authService.userLogin(request, response).thenApply(ResponseModel::new);
  }

  @PostMapping(value = "/change-password", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for users to change their password")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public CompletableFuture<ResponseModel<Void>> changePassword(
      @AuthenticationPrincipal User user, @Valid @RequestBody ChangePasswordRequest request) {
    return authService.changePassword(user, request).thenApply(ResponseModel::new);
  }
}
