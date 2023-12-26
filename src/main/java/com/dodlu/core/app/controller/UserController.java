package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.user.CreateSupervisorRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.user.model.SupervisorProfileModel;
import com.dodlu.core.domain.user.service.concretes.DefaultUserService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class UserController {
  private final DefaultUserService userService;

  @PostMapping(value = "/employer/supervisor", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Employer create supervisor")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = SupervisorProfileModel.class)
  })
  public ResponseModel<SupervisorProfileModel> createSupervisor(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateSupervisorRequest request) {
    SupervisorProfileModel supervisorProfileModel =
        userService.createSupervisor(user, request).join();
    return new ResponseModel<>(supervisorProfileModel);
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
