package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.approval.model.ApprovalRequestModel;
import com.dodlu.core.domain.approval.service.ApprovalService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/approvals-requests", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class ApprovalRequestController {
  private final ApprovalService service;

  @Operation(description = "Approval Requests initiated by loggedIn user")
  @GetMapping(value = "/my-request")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = ApprovalRequestModel.class)
  })
  public ResponseModel<Page<ApprovalRequestModel>> getUserRequests(
      @AuthenticationPrincipal User user, @PageableDefault Pageable pageable) {
    var approvalRequests = service.getUserRequests(user, pageable).join();
    return new ResponseModel<>(approvalRequests);
  }

  @Operation(description = "Approval requests for logged in user")
  @GetMapping(value = "/my-approvals")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = ApprovalRequestModel.class)
  })
  public ResponseModel<Page<ApprovalRequestModel>> getApprovalRequests(
      @AuthenticationPrincipal User user, @PageableDefault Pageable pageable) {
    var approvalRequests = service.getApprovalRequests(user, pageable).join();
    return new ResponseModel<>(approvalRequests);
  }
}
