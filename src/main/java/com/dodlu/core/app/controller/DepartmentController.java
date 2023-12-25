package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.department.CreateDepartmentRequest;
import com.dodlu.core.app.model.response.DepartmentResult;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.department.model.DepartmentModel;
import com.dodlu.core.domain.department.service.DefaultDepartmentService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/department", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class DepartmentController {
  private final DefaultDepartmentService departmentService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Create department")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = DepartmentModel.class)})
  public ResponseModel<DepartmentModel> createFaculty(
      @Valid @RequestBody CreateDepartmentRequest request) {
    DepartmentModel department = departmentService.createDepartment(request).join();
    return new ResponseModel<>(department);
  }

  @GetMapping
  @Operation(description = "Get all departments")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = DepartmentResult.class,
        responseContainer = "page")
  })
  public ResponseModel<DepartmentResult> getAllDepartments(@PageableDefault Pageable pageable) {
    Page<DepartmentModel> departmentPage = departmentService.getAllDepartments(pageable).join();
    DepartmentResult departmentResult = new DepartmentResult(departmentPage);
    return new ResponseModel<>(departmentResult);
  }

  @GetMapping(value = "/{id}")
  @Operation(description = "get single department")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = DepartmentModel.class)})
  public ResponseModel<DepartmentModel> getDepartment(@PathVariable("id") String id) {
    DepartmentModel department = departmentService.getDepartmentById(id).join();
    return new ResponseModel<>(department);
  }
}
