package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.vaccines.CreateVaccineRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.app.model.response.vaccine.VaccineResult;
import com.dodlu.core.domain.immunization.model.VaccineModel;
import com.dodlu.core.domain.immunization.service.VaccineService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/vaccine", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class VaccineController {
  private final VaccineService vaccineService;

  @GetMapping
  @Operation(description = "get all vaccines")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = VaccineResult.class)})
  public ResponseModel<VaccineResult> getParent(@PageableDefault Pageable pageable) {
    return vaccineService
        .getAllVaccines(pageable)
        .thenApply(VaccineResult::new)
        .thenApply(ResponseModel::new)
        .join();
  }

  @GetMapping(value = "/{vaccineId}")
  @Operation(description = "get single vaccine by id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = VaccineModel.class)})
  public ResponseModel<VaccineModel> getVaccineById(@PathVariable String vaccineId) {
    return vaccineService.getVaccineById(vaccineId).thenApply(ResponseModel::new).join();
  }

  @PostMapping
  @Operation(description = "Create new vaccine")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = VaccineModel.class)})
  public ResponseModel<VaccineModel> getVaccineById(
      @Valid @RequestBody CreateVaccineRequest request) {
    return vaccineService.createVaccine(request).thenApply(ResponseModel::new).join();
  }

  @PutMapping(value = "/{vaccineId}")
  @Operation(description = "Update vaccine by id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = VaccineModel.class)})
  public ResponseModel<VaccineModel> updateVaccine(
      @PathVariable String vaccineId, @Valid @RequestBody CreateVaccineRequest request) {
    return vaccineService.updateVaccine(vaccineId, request).thenApply(ResponseModel::new).join();
  }
}
