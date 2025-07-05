package com.dodlu.core.app.controller;

import static com.dodlu.core.app.utils.MapperUtil.buildPageable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.PagingAndSortingParams;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.app.model.response.SupervisorResult;
import com.dodlu.core.domain.user.model.SupervisorProfileModel;
import com.dodlu.core.domain.user.service.concretes.EmployerService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/employer", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
@Hidden
public class EmployerController {
  private final EmployerService employerService;

  @GetMapping(value = "/supervisors")
  @Operation(description = "Employer get all their supervisors")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = SupervisorProfileModel.class,
        responseContainer = "page")
  })
  @PreAuthorize("hasAnyAuthority('EMPLOYER', 'ADMIN')")
  @Hidden
  public ResponseModel<SupervisorResult> getEmployerSupervisor(
      @AuthenticationPrincipal User user, @ModelAttribute PagingAndSortingParams paging) {
    Pageable pageable = buildPageable(paging);
    Page<SupervisorProfileModel> supervisorsPage =
        employerService.getEmployerSupervisors(user, pageable).join();
    SupervisorResult supervisorResult = new SupervisorResult(supervisorsPage);
    return new ResponseModel<>(supervisorResult);
  }
}
