package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.internship.model.PlacementModel;
import com.dodlu.core.domain.internship.service.DefaultPlacementService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/placement", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class PlacementController {
  private final DefaultPlacementService placementService;

  @GetMapping("/student")
  @Operation(description = "Get studentplacement")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = PlacementModel.class)})
  public ResponseModel<PlacementModel> getPlacement(@AuthenticationPrincipal User user) {
    PlacementModel placementModel = placementService.getPlacement(user).join();
    return new ResponseModel<>(placementModel);
  }
}
