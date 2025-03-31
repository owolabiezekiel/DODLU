package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.discussion.CreateDiscussionRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.discussion.model.DiscussionModel;
import com.dodlu.core.domain.discussion.service.DiscussionService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/discussions", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class DiscussionController {
  private final DiscussionService discussionService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Create discussion in a group")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = DiscussionModel.class)})
  public ResponseModel<DiscussionModel> createGroupDiscussion(
      @AuthenticationPrincipal User user, @Validated @RequestBody CreateDiscussionRequest request) {
    var discussion = discussionService.createDiscussion(user, request);
    return new ResponseModel<>(discussion);
  }

  //  @GetMapping("/group/{groupId}")
  //  @Operation(description = "Get group savings transaction by id")
  //  @ApiResponses({
  //    @ApiResponse(
  //        code = 200,
  //        message = "Success",
  //        response = TransactionModel.class,
  //        responseContainer = "Page")
  //  })
  //  public ResponseModel<Page<TransactionModel>> getGroupTransactions(
  //      @AuthenticationPrincipal User user,
  //      @PathVariable("groupId") String groupId,
  //      @PageableDefault Pageable pageable) {
  //    var transactions = transactionService.getGroupTransactions(user, groupId, pageable).join();
  //    return new ResponseModel<>(transactions);
  //  }
  //
  //  @GetMapping("/group/{groupId}/customer/{customerId}")
  //  @Operation(description = "Get all of a users transaction in a group")
  //  @ApiResponses({
  //    @ApiResponse(
  //        code = 200,
  //        message = "Success",
  //        response = TransactionModel.class,
  //        responseContainer = "Page")
  //  })
  //  public ResponseModel<Page<TransactionModel>> getUserGroupTransactions(
  //      @AuthenticationPrincipal User user,
  //      @PathVariable("groupId") String groupId,
  //      @PathVariable("customerId") String customerId,
  //      @PageableDefault Pageable pageable) {
  //    var transactions =
  //        transactionService.getUserGroupTransactions(user, groupId, customerId, pageable);
  //    return new ResponseModel<>(transactions);
  //  }

}
