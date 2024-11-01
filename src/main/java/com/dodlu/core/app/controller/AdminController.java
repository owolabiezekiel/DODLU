// package com.dodlu.core.app.controller;
//
// import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
// import com.dodlu.core.app.model.response.ResponseModel;
// import com.dodlu.core.domain.user.model.DashboardModel;
// import com.dodlu.core.domain.user.service.concretes.AdminService;
// import io.swagger.annotations.ApiResponse;
// import io.swagger.annotations.ApiResponses;
// import io.swagger.v3.oas.annotations.Operation;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
//
// @RequiredArgsConstructor
// @RestController
// @RequestMapping(value = "/api/v1/admin", produces = APPLICATION_JSON_VALUE)
// @Slf4j
// @CrossOrigin("*")
// public class AdminController {
//  private final AdminService adminService;
//
//  @GetMapping(value = "/dashboard-overview")
//  @Operation(description = "Get system overview")
//  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = DashboardModel.class)})
//  @PreAuthorize("hasAuthority('ADMIN')")
//  public ResponseModel<DashboardModel> getDashboardOverview() {
//    DashboardModel dashboard = adminService.getOverview().join();
//    return new ResponseModel<>(dashboard);
//  }
// }
