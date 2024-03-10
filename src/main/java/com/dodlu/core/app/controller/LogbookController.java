package com.dodlu.core.app.controller;

import static com.dodlu.core.app.utils.MapperUtil.buildPageable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.PagingAndSortingParams;
import com.dodlu.core.app.model.request.internship.InsertLogbookEntryRequest;
import com.dodlu.core.app.model.response.LogbookEntryResult;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.internship.model.LogbookEntryModel;
import com.dodlu.core.domain.internship.model.LogbookModel;
import com.dodlu.core.domain.internship.service.LogbookService;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/logbook", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class LogbookController {
  private final LogbookService logbookService;

  @GetMapping()
  @Operation(description = "Get student logbook")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = LogbookModel.class)})
  @PreAuthorize("hasAnyAuthority('STUDENT')")
  public ResponseModel<LogbookModel> getLogbook(@AuthenticationPrincipal User user) {
    LogbookModel logbookModel = logbookService.getLogbook(user).join();
    return new ResponseModel<>(logbookModel);
  }

  @GetMapping("/entries")
  @Operation(description = "Get logbook contents")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = LogbookEntryResult.class,
        responseContainer = "page")
  })
  @PreAuthorize("hasAnyAuthority('STUDENT')")
  public ResponseModel<LogbookEntryResult> getLogbookEntries(
      @AuthenticationPrincipal User user, @ModelAttribute PagingAndSortingParams paging) {
    Pageable pageable = buildPageable(paging);
    Page<LogbookEntryModel> logbookEntryPage =
        logbookService.getLogbookEntries(user, pageable).join();
    LogbookEntryResult logbookEntryResult = new LogbookEntryResult(logbookEntryPage);
    return new ResponseModel<>(logbookEntryResult);
  }

  @PostMapping(value = "/entries", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Fill logbook for today")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = LogbookEntryModel.class)})
  @PreAuthorize("hasAnyAuthority('STUDENT')")
  public ResponseModel<LogbookEntryModel> insertLogbookEntry(
      @AuthenticationPrincipal User user, @Valid @RequestBody InsertLogbookEntryRequest request) {
    LogbookEntryModel entry = logbookService.insertLogbookEntry(user, request).join();
    return new ResponseModel<>(entry);
  }
}
