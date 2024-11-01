// package com.dodlu.core.app.controller;
//
// import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
// import com.dodlu.core.app.model.request.internship.InsertLogbookEntryRequest;
// import com.dodlu.core.app.model.request.internship.LogbookCommentRequest;
// import com.dodlu.core.app.model.response.LogbookEntryResult;
// import com.dodlu.core.app.model.response.ResponseModel;
// import com.dodlu.core.domain.internship.model.LogbookEntryModel;
// import com.dodlu.core.domain.internship.model.LogbookModel;
// import com.dodlu.core.domain.internship.service.LogbookService;
// import com.dodlu.core.persistence.user.entity.User;
// import io.swagger.annotations.ApiResponse;
// import io.swagger.annotations.ApiResponses;
// import io.swagger.v3.oas.annotations.Operation;
// import jakarta.validation.Valid;
// import java.util.List;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.*;
//
// @RequiredArgsConstructor
// @RestController
// @RequestMapping(value = "/api/v1/logbook", produces = APPLICATION_JSON_VALUE)
// @Slf4j
// @CrossOrigin("*")
// public class LogbookController {
//  private final LogbookService logbookService;
//
//  @GetMapping()
//  @Operation(description = "Get student logbook")
//  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = LogbookModel.class)})
//  @PreAuthorize("hasAnyAuthority('STUDENT')")
//  public ResponseModel<LogbookModel> getLogbook(@AuthenticationPrincipal User user) {
//    LogbookModel logbookModel = logbookService.getLogbook(user).join();
//    return new ResponseModel<>(logbookModel);
//  }
//
//  @GetMapping(value = "/{studentProfileId}")
//  @Operation(description = "Get student logbook by student profile id")
//  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = LogbookModel.class)})
//  @PreAuthorize("hasAnyAuthority('EMPLOYER', 'ADMIN', 'COORDINATOR', 'SUPERVISOR')")
//  public ResponseModel<LogbookModel> getLogbook(@PathVariable String studentProfileId) {
//    LogbookModel logbookModel =
//        logbookService.getLogbookByStudentProfileId(studentProfileId).join();
//    return new ResponseModel<>(logbookModel);
//  }
//
//  @PostMapping(value = "/start")
//  @Operation(description = "Set start day on logbook")
//  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = LogbookModel.class)})
//  @PreAuthorize("hasAnyAuthority('STUDENT')")
//  public ResponseModel<LogbookModel> setLogbookStartDate(@AuthenticationPrincipal User user) {
//    LogbookModel logbook = logbookService.setLogbookStartDate(user).join();
//    return new ResponseModel<>(logbook);
//  }
//
//  @GetMapping("/entries")
//  @Operation(description = "Get logbook contents")
//  @ApiResponses({
//    @ApiResponse(
//        code = 200,
//        message = "Success",
//        response = LogbookEntryResult.class,
//        responseContainer = "list")
//  })
//  @PreAuthorize("hasAnyAuthority('STUDENT')")
//  public ResponseModel<List<LogbookEntryModel>> getLogbookEntries(
//      @AuthenticationPrincipal User user) {
//    List<LogbookEntryModel> logbookEntryResult = logbookService.getLogbookEntries(user).join();
//    return new ResponseModel<>(logbookEntryResult);
//  }
//
//  @GetMapping("/entries/{studentProfileId}")
//  @Operation(description = "Get logbook contents by student profile id")
//  @ApiResponses({
//    @ApiResponse(
//        code = 200,
//        message = "Success",
//        response = LogbookEntryResult.class,
//        responseContainer = "list")
//  })
//  @PreAuthorize("hasAnyAuthority('EMPLOYER', 'ADMIN', 'COORDINATOR', 'SUPERVISOR')")
//  public ResponseModel<List<LogbookEntryModel>> getLogbookEntries(
//      @PathVariable String studentProfileId) {
//    List<LogbookEntryModel> logbookEntryResult =
//        logbookService.getLogbookEntriesByStudentProfileId(studentProfileId).join();
//    return new ResponseModel<>(logbookEntryResult);
//  }
//
//  @PostMapping(value = "/entries", consumes = APPLICATION_JSON_VALUE)
//  @Operation(description = "Fill logbook for today")
//  @ApiResponses({@ApiResponse(code = 200, message = "Success", response =
// LogbookEntryModel.class)})
//  @PreAuthorize("hasAnyAuthority('STUDENT')")
//  public ResponseModel<LogbookEntryModel> insertLogbookEntry(
//      @AuthenticationPrincipal User user, @Valid @RequestBody InsertLogbookEntryRequest request) {
//    LogbookEntryModel entry = logbookService.insertLogbookEntry(user, request).join();
//    return new ResponseModel<>(entry);
//  }
//
//  @PostMapping(value = "/{studentProfileId}/comment/supervisor", consumes =
// APPLICATION_JSON_VALUE)
//  @Operation(description = "Supervisor Comment on logbook entry for the week")
//  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
//  @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
//  public ResponseModel<Void> supervisorComment(
//      @AuthenticationPrincipal User user,
//      @Valid @RequestBody LogbookCommentRequest request,
//      @PathVariable String studentProfileId) {
//    Void entry = logbookService.supervisorComment(user, request, studentProfileId).join();
//    return new ResponseModel<>(entry);
//  }
//
//  @PostMapping(value = "/{studentProfileId}/comment/employer", consumes = APPLICATION_JSON_VALUE)
//  @Operation(description = "Employer Comment on logbook entry for the week")
//  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
//  @PreAuthorize("hasAnyAuthority('EMPLOYER')")
//  public ResponseModel<Void> employerComment(
//      @AuthenticationPrincipal User user,
//      @Valid @RequestBody LogbookCommentRequest request,
//      @PathVariable String studentProfileId) {
//    Void entry = logbookService.employerComment(user, request, studentProfileId).join();
//    return new ResponseModel<>(entry);
//  }
// }
