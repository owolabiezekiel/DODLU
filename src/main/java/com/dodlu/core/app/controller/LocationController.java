package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.location.CreateCountryRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.location.model.CountryModel;
import com.dodlu.core.domain.location.service.DefaultLocationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

  //  @GetMapping
  //  @Operation(description = "Get all departments")
  //  @ApiResponses({
  //    @ApiResponse(
  //        code = 200,
  //        message = "Success",
  //        response = DepartmentResult.class,
  //        responseContainer = "page")
  //  })
  //  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT', 'EMPLOYER', 'SUPERVISOR', 'COORDINATOR')")
  //  public ResponseModel<DepartmentResult> getAllDepartments(@PageableDefault Pageable pageable) {
  //    Page<DepartmentModel> departmentPage = departmentService.getAllDepartments(pageable).join();
  //    DepartmentResult departmentResult = new DepartmentResult(departmentPage);
  //    return new ResponseModel<>(departmentResult);
  //  }
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
