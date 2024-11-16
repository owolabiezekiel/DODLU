package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.savings.CreateGroupSavingRequest;
import com.dodlu.core.app.model.request.savings.GroupTransactionRequest;
import com.dodlu.core.app.model.request.savings.InviteToGroupRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.savings.model.GroupSavingModel;
import com.dodlu.core.domain.savings.service.GroupSavingsService;
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
    GroupSavingModel groupSaving = groupSavingsService.createGroupSaving(user, request).join();
    return new ResponseModel<>(groupSaving);
  }

  @GetMapping("/group/{savingsId}")
  @Operation(description = "Get group savings by id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = GroupSavingModel.class)})
  public ResponseModel<GroupSavingModel> getGroupSavingById(
      @AuthenticationPrincipal User user, @PathVariable("savingsId") String savingsId) {
    GroupSavingModel groupSaving = groupSavingsService.getGroupSaving(user, savingsId).join();
    return new ResponseModel<>(groupSaving);
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
    var groupSaving = groupSavingsService.getUserGroupSavings(user, pageable).join();
    return new ResponseModel<>(groupSaving);
  }

  @PostMapping("/group/record-transaction/{groupSavingId}")
  @Operation(description = "Add transaction to group saving")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> recordGroupTransaction(
      @AuthenticationPrincipal User user,
      @PathVariable("groupSavingId") String groupSavingId,
      @Valid @RequestBody GroupTransactionRequest request) {
    groupSavingsService.recordGroupTransaction(user, groupSavingId, request).join();
    return new ResponseModel<>(null);
  }

  @PostMapping("/group/invite")
  @Operation(description = "Invite a user to group saving")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> inviteUserToGroupSaving(
      @AuthenticationPrincipal User user, @Valid @RequestBody InviteToGroupRequest request) {
    groupSavingsService
        .inviteUserToGroup(user, request.getGroupId(), request.getInviteeId())
        .join();
    return new ResponseModel<>(null);
  }
}
