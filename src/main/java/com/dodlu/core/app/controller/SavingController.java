package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.savings.*;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.savings.model.GroupSavingLoanModel;
import com.dodlu.core.domain.savings.model.GroupSavingModel;
import com.dodlu.core.domain.savings.model.LoanMetricModel;
import com.dodlu.core.domain.savings.service.GroupSavingsService;
import com.dodlu.core.domain.user.model.CustomerProfileModel;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/savings", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class SavingController {
  private final GroupSavingsService groupSavingsService;

  @PostMapping(value = "/group", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "create group savings")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = GroupSavingModel.class)})
  public ResponseModel<GroupSavingModel> createGroupSavings(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateGroupSavingRequest request) {
    GroupSavingModel groupSaving = groupSavingsService.createGroupSaving(user, request);
    return new ResponseModel<>(groupSaving);
  }

  @GetMapping("/group/{savingsId}")
  @Operation(description = "Get group savings by id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = GroupSavingModel.class)})
  public ResponseModel<GroupSavingModel> getGroupSavingById(
      @AuthenticationPrincipal User user, @PathVariable("savingsId") String savingsId) {
    GroupSavingModel groupSaving = groupSavingsService.getGroupSaving(user, savingsId);
    return new ResponseModel<>(groupSaving);
  }

  @GetMapping("/group/{groupId}/members")
  @Operation(description = "Get a group savings members")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = CustomerProfileModel.class,
        responseContainer = "Page")
  })
  public ResponseModel<Page<CustomerProfileModel>> getGroupSavingMembers(
      @PathVariable("groupId") String groupId, @PageableDefault Pageable pageable) {
    var result = groupSavingsService.getGroupMembers(groupId, pageable);
    return new ResponseModel<>(result);
  }

  @GetMapping("/group")
  @Operation(description = "Get user group savings")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = GroupSavingModel.class,
        responseContainer = "Page")
  })
  public ResponseModel<Page<GroupSavingModel>> getUserGroupSavings(
      @AuthenticationPrincipal User user, @PageableDefault Pageable pageable) {
    var groupSaving = groupSavingsService.getUserGroupSavings(user, pageable);
    return new ResponseModel<>(groupSaving);
  }

  @PostMapping("/group/record-transaction/{groupSavingId}")
  @Operation(description = "Add transaction to group saving")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> recordGroupTransaction(
      @AuthenticationPrincipal User user,
      @PathVariable("groupSavingId") String groupSavingId,
      @Valid @RequestBody GroupTransactionRequest request) {
    groupSavingsService.recordGroupTransaction(user, groupSavingId, request);
    return new ResponseModel<>(null);
  }

  //  @PostMapping("/group/invite")
  //  @Operation(description = "Invite a user to group saving")
  //  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  //  public ResponseModel<Void> inviteUserToGroupSaving(
  //      @AuthenticationPrincipal User user, @Valid @RequestBody InviteToGroupRequest request) {
  //    groupSavingsService.inviteUserToGroup(user, request.getGroupId(), request.getInviteeId());
  //    return new ResponseModel<>(null);
  //  }

  @PostMapping(value = "/group/send-invite", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Invite a user to group saving")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> userGroupInvite(
      @AuthenticationPrincipal User user, @Valid @RequestBody GroupInviteRequest request) {
    groupSavingsService.sendGroupInvite(user, request);
    return new ResponseModel<>(null);
  }

  @PostMapping(value = "/group/join-with-code/{inviteCode}")
  @Operation(description = "Join group with code")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<GroupSavingModel> joinGroupWithCode(
      @AuthenticationPrincipal User user, @PathVariable String inviteCode) {
    var response = groupSavingsService.joinGroupByInviteCode(user, inviteCode);
    return new ResponseModel<>(response);
  }

  @PostMapping("/group/request-join/{groupSavingId}")
  @Operation(description = "User requests to join group")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public void userRequestsToJoiGroup(
      @AuthenticationPrincipal User user, @PathVariable String groupSavingId) {
    groupSavingsService.requestToJoinGroup(user, groupSavingId);
  }

  @PostMapping("/group/loans/request")
  @Operation(description = "User requests group loan")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> requestGroupLoan(
      @AuthenticationPrincipal User user, @Valid @RequestBody GroupLoanRequest request) {
    groupSavingsService.requestLoan(user, request);
    return new ResponseModel<>(null);
  }

  @GetMapping("/group/{groupSavingId}/loans")
  @Operation(description = "Get all group loans")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = GroupSavingModel.class,
        responseContainer = "Page")
  })
  public ResponseModel<Page<GroupSavingLoanModel>> getGroupLoans(
      @AuthenticationPrincipal User user,
      @PathVariable("groupSavingId") String groupSavingId,
      @PageableDefault Pageable pageable) {
    var loans = groupSavingsService.getGroupLoans(user, groupSavingId, pageable);
    return new ResponseModel<>(loans);
  }

  @GetMapping("/group/{groupSavingId}/loan-metrics")
  @Operation(description = "Get Loan metrics for a group")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = LoanMetricModel.class)})
  public ResponseModel<LoanMetricModel> getLoanMetricForGroup(
      @AuthenticationPrincipal User user, @PathVariable("groupSavingId") String groupSavingId) {
    var metrics = groupSavingsService.getLoanMetrics(user, groupSavingId);
    return new ResponseModel<>(metrics);
  }

  @GetMapping("/group/loans/{groupLoanId}")
  @Operation(description = "Get group loan by Id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = GroupSavingModel.class)})
  public ResponseModel<GroupSavingLoanModel> getGroupLoan(
      @AuthenticationPrincipal User user, @PathVariable("groupLoanId") String groupLoanId) {
    var loan = groupSavingsService.getGroupLoanById(user, groupLoanId);
    return new ResponseModel<>(loan);
  }

  @PostMapping("/group/loans/{groupLoanId}/approve")
  @Operation(description = "Approve group loan by Id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> approveGroupLoan(
      @AuthenticationPrincipal User user, @PathVariable("groupLoanId") String groupLoanId) {
    groupSavingsService.approveLoan(user, groupLoanId);
    return new ResponseModel<>(null);
  }

  @PostMapping("/group/loans/{groupLoanId}/reject")
  @Operation(description = "Reject group loan by Id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> rejectGroupLoan(
      @AuthenticationPrincipal User user, @PathVariable("groupLoanId") String groupLoanId) {
    groupSavingsService.rejectLoan(user, groupLoanId);
    return new ResponseModel<>(null);
  }
}
