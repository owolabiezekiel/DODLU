package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.user.CreateUserRequest;
import com.dodlu.core.app.model.request.user.UserLoginRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.app.model.response.user.LoginResponseModel;
import com.dodlu.core.domain.user.model.AppUserModel;
import com.dodlu.core.domain.user.service.abstracts.AuthService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class AuthController {
  private final AuthService authService;

  @PostMapping(value = "/student/register", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for students to register")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = AppUserModel.class)})
  public CompletableFuture<ResponseModel<AppUserModel>> studentRegister(
      @Valid @RequestBody CreateUserRequest request) {
    return authService.registerStudentUser(request).thenApply(ResponseModel::new);
  }

  @PostMapping(value = "/student/login", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for students to login")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = LoginResponseModel.class)
  })
  public CompletableFuture<ResponseModel<LoginResponseModel>> studentLogin(
      @Valid @RequestBody UserLoginRequest request) {
    return authService.userLogin(request).thenApply(ResponseModel::new);
  }
}
