package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.systemsettings.SystemSettingsRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.systemsettings.domain.SystemSettingsModel;
import com.dodlu.core.domain.systemsettings.service.SystemSettingsService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/system-settings", produces = APPLICATION_JSON_VALUE)
public class SystemSettingsController {
  private final SystemSettingsService systemSettingsService;

  @GetMapping
  @Operation(description = "Get system settings")
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = SystemSettingsModel.class)
  })
  public ResponseModel<SystemSettingsModel> getSystemSettings() {
    var systemSettings = systemSettingsService.getSystemSettings();
    return new ResponseModel<>(systemSettings);
  }

  @PutMapping
  @Operation(description = "Create or update system settings")
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = SystemSettingsModel.class)
  })
  public ResponseModel<SystemSettingsModel> createUpdateSystemSettings(
      @Valid @RequestBody SystemSettingsRequest request) {
    var systemSettings = systemSettingsService.createSystemSettings(request);
    return new ResponseModel<>(systemSettings);
  }
}
