package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.location.CreateCountryRequest;
import com.dodlu.core.app.model.request.location.CreateStateRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.location.model.CountryModel;
import com.dodlu.core.domain.location.model.StateModel;
import com.dodlu.core.domain.location.service.DefaultLocationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/location", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class LocationController {
  private final DefaultLocationService locationService;

  @PostMapping(value = "/country", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Admin create country")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = CountryModel.class)})
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  public ResponseModel<CountryModel> createCountry(
      @Valid @RequestBody CreateCountryRequest request) {
    CountryModel country = locationService.createCountry(request).join();
    return new ResponseModel<>(country);
  }

  @PostMapping(value = "/state", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Admin create state")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = StateModel.class)})
  @PreAuthorize("hasAnyAuthority('ADMIN')")
  public ResponseModel<StateModel> createState(@Valid @RequestBody CreateStateRequest request) {
    StateModel state = locationService.createState(request).join();
    return new ResponseModel<>(state);
  }

  @GetMapping(value = "/country")
  @Operation(description = "Get all countries")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = CountryModel.class,
        responseContainer = "set")
  })
  public ResponseModel<Set<CountryModel>> getAllCountries() {
    Set<CountryModel> countries = locationService.getAllCountries().join();
    return new ResponseModel<>(countries);
  }

  @GetMapping(value = "/states/{countryId}")
  @Operation(description = "Get all states by country id")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = StateModel.class,
        responseContainer = "set")
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT', 'EMPLOYER', 'SUPERVISOR', 'COORDINATOR')")
  public ResponseModel<Set<StateModel>> getAllStatesForCountry(
      @PathVariable("countryId") String countryId) {
    Set<StateModel> states = locationService.getAllStatesForCountry(countryId).join();
    return new ResponseModel<>(states);
  }

  @GetMapping(value = "/states")
  @Operation(description = "Get all states")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = StateModel.class,
        responseContainer = "set")
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT', 'EMPLOYER', 'SUPERVISOR', 'COORDINATOR')")
  public ResponseModel<Set<StateModel>> getAllStates() {
    Set<StateModel> states = locationService.getAllStates().join();
    return new ResponseModel<>(states);
  }
  //
  //  @GetMapping(value = "/{id}")
  //  @Operation(description = "get single department")
  //  @ApiResponses({@ApiResponse(code = 200, message = "Success", response =
  // DepartmentModel.class)})
  //  public ResponseModel<DepartmentModel> getDepartment(@PathVariable("id") String id) {
  //    DepartmentModel department = departmentService.getDepartmentById(id).join();
  //    return new ResponseModel<>(department);
  //  }
}
