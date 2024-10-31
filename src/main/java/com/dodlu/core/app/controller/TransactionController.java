package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.transaction.model.TransactionModel;
import com.dodlu.core.domain.transaction.service.TransactionService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/transactions", produces = APPLICATION_JSON_VALUE)
@CrossOrigin("*")
public class TransactionController {
  private final TransactionService transactionService;

  @GetMapping(value = "/customer", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Get customer transactions")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = TransactionModel.class,
        responseContainer = "Page")
  })
  public ResponseModel<Page<TransactionModel>> getCustomerTransactions(
      @AuthenticationPrincipal User user, @PageableDefault Pageable pageable) {
    var transactions = transactionService.getCustomerTransactions(user, pageable).join();
    return new ResponseModel<>(transactions);
  }

  @GetMapping("/group/{groupId}")
  @Operation(description = "Get group savings transaction by id")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = TransactionModel.class,
        responseContainer = "Page")
  })
  public ResponseModel<Page<TransactionModel>> getGroupTransactions(
      @AuthenticationPrincipal User user,
      @PathVariable("groupId") String groupId,
      @PageableDefault Pageable pageable) {
    var transactions = transactionService.getGroupTransactions(user, groupId, pageable).join();
    return new ResponseModel<>(transactions);
  }
  //
  //  @GetMapping("/group")
  //  @Operation(description = "Get user group savings")
  //  @ApiResponses({
  //    @ApiResponse(
  //        code = 200,
  //        message = "Success",
  //        response = GroupSavingModel.class,
  //        responseContainer = "Page")
  //  })
  //  public ResponseModel<Page<GroupSavingModel>> getUserGroupSavings(
  //      @AuthenticationPrincipal User user, @PageableDefault Pageable pageable) {
  //    var groupSaving = groupSavingsService.getUserGroupSavings(user, pageable).join();
  //    return new ResponseModel<>(groupSaving);
  //  }
}
