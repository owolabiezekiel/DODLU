// package com.dodlu.core.app.controller;
//
// import static com.dodlu.core.app.utils.MapperUtil.buildPageable;
// import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
// import com.dodlu.core.app.model.PagingAndSortingParams;
// import com.dodlu.core.app.model.request.internship.AssignSupervisorRequest;
// import com.dodlu.core.app.model.response.ResponseModel;
// import com.dodlu.core.app.model.response.StudentProfileResult;
// import com.dodlu.core.domain.internship.model.PlacementModel;
// import com.dodlu.core.domain.internship.model.StudentSupervisorModel;
// import com.dodlu.core.domain.internship.service.DefaultPlacementService;
// import com.dodlu.core.domain.user.model.StudentProfileModel;
// import com.dodlu.core.domain.user.model.SupervisorProfileModel;
// import com.dodlu.core.persistence.user.entity.User;
// import io.swagger.annotations.ApiResponse;
// import io.swagger.annotations.ApiResponses;
// import io.swagger.v3.oas.annotations.Operation;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.*;
//
// @RequiredArgsConstructor
// @RestController
// @RequestMapping(value = "/api/v1/placement", produces = APPLICATION_JSON_VALUE)
// @Slf4j
// @CrossOrigin("*")
// public class PlacementController {
//  private final DefaultPlacementService placementService;
//
//  @GetMapping("/student")
//  @Operation(description = "Get student placement")
//  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = PlacementModel.class)})
//  public ResponseModel<PlacementModel> getPlacement(@AuthenticationPrincipal User user) {
//    PlacementModel placementModel = placementService.getPlacement(user).join();
//    return new ResponseModel<>(placementModel);
//  }
//
//  @PostMapping(value = "/supervisor", consumes = APPLICATION_JSON_VALUE)
//  @Operation(description = "Assign supervisor to student")
//  @ApiResponses({
//    @ApiResponse(code = 200, message = "Success", response = StudentSupervisorModel.class)
//  })
//  public ResponseModel<StudentSupervisorModel> assignStudentSupervisor(
//      @AuthenticationPrincipal User user, @Valid @RequestBody AssignSupervisorRequest request) {
//    StudentSupervisorModel studentSupervisorModel =
//        placementService.assignStudentSupervisor(user, request).join();
//    return new ResponseModel<>(studentSupervisorModel);
//  }
//
//  @GetMapping("/student/supervisor")
//  @Operation(description = "Get student supervisor")
//  @ApiResponses({
//    @ApiResponse(code = 200, message = "Success", response = SupervisorProfileModel.class)
//  })
//  @PreAuthorize("hasAnyAuthority('STUDENT')")
//  public ResponseModel<SupervisorProfileModel> getStudentSupervisor(
//      @AuthenticationPrincipal User user) {
//    SupervisorProfileModel supervisor = placementService.getStudentSupervisor(user).join();
//    return new ResponseModel<>(supervisor);
//  }
//
//  @GetMapping("/student/{studentProfileId}/supervisor")
//  @Operation(description = "Get student supervisor by student profile id")
//  @ApiResponses({
//    @ApiResponse(code = 200, message = "Success", response = SupervisorProfileModel.class)
//  })
//  @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR', 'COORDINATOR', 'EMPLOYER', 'STUDENT')")
//  public ResponseModel<SupervisorProfileModel> getStudentSupervisor(
//      @PathVariable("studentProfileId") String studentProfileId) {
//    SupervisorProfileModel supervisor =
//        placementService.getStudentSupervisor(studentProfileId).join();
//    return new ResponseModel<>(supervisor);
//  }
//
//  @GetMapping("/supervisor/students")
//  @Operation(description = "Get  supervisor students")
//  @ApiResponses({
//    @ApiResponse(
//        code = 200,
//        message = "Success",
//        response = StudentProfileResult.class,
//        responseContainer = "page")
//  })
//  @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
//  public ResponseModel<StudentProfileResult> getSupervisorStudents(
//      @AuthenticationPrincipal User user, @ModelAttribute PagingAndSortingParams paging) {
//    Pageable pageable = buildPageable(paging);
//    Page<StudentProfileModel> studentsPage =
//        placementService.getSupervisorStudents(user, pageable).join();
//    StudentProfileResult studentsResult = new StudentProfileResult(studentsPage);
//    return new ResponseModel<>(studentsResult);
//  }
//
//  @GetMapping("/supervisor/{supervisorProfileId}/students")
//  @Operation(description = "Get  supervisor students by supervisor profile id")
//  @ApiResponses({
//    @ApiResponse(
//        code = 200,
//        message = "Success",
//        response = StudentProfileResult.class,
//        responseContainer = "page")
//  })
//  @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPERVISOR', 'COORDINATOR', 'EMPLOYER')")
//  public ResponseModel<StudentProfileResult> getSupervisorStudents(
//      @PathVariable("supervisorProfileId") String supervisorProfileId,
//      @ModelAttribute PagingAndSortingParams paging) {
//    Pageable pageable = buildPageable(paging);
//    Page<StudentProfileModel> studentsPage =
//        placementService.getSupervisorStudents(supervisorProfileId, pageable).join();
//    StudentProfileResult studentsResult = new StudentProfileResult(studentsPage);
//    return new ResponseModel<>(studentsResult);
//  }
//
//  @GetMapping("/employer/interns")
//  @Operation(description = "Get all employer interns")
//  @ApiResponses({
//    @ApiResponse(
//        code = 200,
//        message = "Success",
//        response = StudentProfileResult.class,
//        responseContainer = "page")
//  })
//  @PreAuthorize("hasAnyAuthority('EMPLOYER')")
//  public ResponseModel<StudentProfileResult> getInterns(
//      @AuthenticationPrincipal User user, @ModelAttribute PagingAndSortingParams paging) {
//    Pageable pageable = buildPageable(paging);
//    Page<StudentProfileModel> studentsPage = placementService.getInterns(user, pageable).join();
//    StudentProfileResult studentsResult = new StudentProfileResult(studentsPage);
//    return new ResponseModel<>(studentsResult);
//  }
// }
