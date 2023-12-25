package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.user.CreateRoleRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.app.model.response.user.RoleResult;
import com.dodlu.core.domain.user.model.RoleModel;
import com.dodlu.core.domain.user.service.abstracts.RoleService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/role", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class RoleController {
  private final RoleService roleService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "create role")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = RoleModel.class)})
  public CompletableFuture<ResponseModel<RoleModel>> createRole(
      @Valid @RequestBody CreateRoleRequest request) {
    return roleService.createRole(request).thenApply(ResponseModel::new);
  }

  @GetMapping
  @Operation(description = "get all roles")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = RoleResult.class)})
  public CompletableFuture<ResponseModel<RoleResult>> getAllRoles(
      @PageableDefault Pageable pageable) {
    return roleService
        .getAllRoles(pageable)
        .thenApply(RoleResult::new)
        .thenApply(ResponseModel::new);
  }

  @GetMapping(value = "/{id}")
  @Operation(description = "get single role")
  //  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT', 'LECTURER')")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = RoleModel.class)})
  public ResponseModel<RoleModel> getRole(@PathVariable("id") String id) {
    RoleModel role = roleService.getRoleById(id).join();
    return new ResponseModel<>(role);
  }
}
