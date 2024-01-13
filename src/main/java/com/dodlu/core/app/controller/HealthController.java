package com.dodlu.core.app.controller;

import com.dodlu.core.app.model.response.ResponseModel;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/health")
@RequiredArgsConstructor
public class HealthController {
  @Value("${dodlu.jwt-secret}")
  private String jwtSecret;

  @Operation(description = "Health Check")
  @GetMapping
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = String.class)})
  public CompletableFuture<ResponseModel<String>> getStatus() {
    ResponseModel<String> status = new ResponseModel<>("UP - " + jwtSecret);
    return CompletableFuture.completedFuture(status);
  }
}
