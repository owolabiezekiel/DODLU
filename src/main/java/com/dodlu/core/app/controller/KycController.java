package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.kyc.service.KycService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/kyc", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class KycController {
  private final KycService kycService;

  @PostMapping(value = "/verify-bvn", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Endpoint for users to Verify their BVN")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> verifyBvn(@AuthenticationPrincipal User user) {
    kycService.verifyBVN().join();
    return new ResponseModel<>(null);
  }
}
