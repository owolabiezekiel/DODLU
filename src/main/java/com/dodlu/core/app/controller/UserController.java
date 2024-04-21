package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.dodlu.core.app.model.request.user.*;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.internship.model.StudentDocumentModel;
import com.dodlu.core.domain.user.model.*;
import com.dodlu.core.domain.user.service.concretes.DefaultUserService;
import com.dodlu.core.persistence.base.UserProfile;
import com.dodlu.core.persistence.user.entity.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/user", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class UserController {
  private final DefaultUserService userService;

  @PostMapping(value = "/supervisor", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Employer create supervisor")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = SupervisorProfileModel.class)
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYER')")
  public ResponseModel<SupervisorProfileModel> createSupervisor(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateSupervisorRequest request) {
    SupervisorProfileModel supervisorProfileModel =
        userService.createSupervisor(user, request).join();
    return new ResponseModel<>(supervisorProfileModel);
  }

  @PostMapping(value = "/coordinator", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Admin create coordinator")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = CoordinatorProfileModel.class)
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYER')")
  public ResponseModel<CoordinatorProfileModel> createCoordinator(
      @Valid @RequestBody CreateCoordinatorRequest request) {
    CoordinatorProfileModel coordinatorProfileModel = userService.createCoordinator(request).join();
    return new ResponseModel<>(coordinatorProfileModel);
  }

  @PostMapping(value = "/student", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Coordinator Create student")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = StudentProfileModel.class)
  })
  @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDINATOR')")
  public ResponseModel<StudentProfileModel> createStudent(
      @AuthenticationPrincipal User user, @Valid @RequestBody CreateStudentRequest request) {
    StudentProfileModel studentProfileModel = userService.createStudent(user, request).join();
    return new ResponseModel<>(studentProfileModel);
  }

  @PostMapping(value = "/students", consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(description = "Coordinator create students")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  public ResponseModel<Void> createStudent(
      @AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
    userService.createStudents(user, file).join();
    return new ResponseModel<>(null);
  }

  @PostMapping(value = "/profile-image", consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(description = "Update display picture")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = UserProfile.class)})
  public ResponseModel<? extends UserProfile> updateDisplayPicture(
      @AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
    UserProfile userProfile = userService.updateDisplayPicture(user, file).join();
    return new ResponseModel<>(userProfile);
  }

  @GetMapping(value = "/profile")
  @Operation(description = "Get logged in user profile")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = GeneralProfileModel.class)
  })
  public ResponseModel<GeneralProfileModel> getCurrentUserProfile(
      @AuthenticationPrincipal User user) {
    GeneralProfileModel userProfile = userService.getCurrentUserProfile(user).join();
    return new ResponseModel<>(userProfile);
  }

  @PutMapping(value = "/profile_update/student")
  @Operation(description = "Student update user profile")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = StudentProfileModel.class)
  })
  @PreAuthorize("hasAnyAuthority('STUDENT')")
  public ResponseModel<StudentProfileModel> updateStudentProfile(
      @AuthenticationPrincipal User user, @Valid @RequestBody UpdateStudentProfileRequest request) {
    StudentProfileModel userProfile = userService.updateStudentProfile(user, request).join();
    return new ResponseModel<>(userProfile);
  }

  @PutMapping(value = "/profile_update/employer")
  @Operation(description = "Employer update user profile")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = EmployerProfileModel.class)
  })
  @PreAuthorize("hasAnyAuthority('EMPLOYER')")
  public ResponseModel<EmployerProfileModel> employerUpdateProfile(
      @AuthenticationPrincipal User user, @Valid @RequestBody UpdateEmployerRequest request) {
    EmployerProfileModel userProfile = userService.employerUpdateProfile(user, request).join();
    return new ResponseModel<>(userProfile);
  }

  //  @GetMapping(value = "/sample-email")
  //  @Operation(description = "Get logged in user profile")
  //  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  //  public ResponseModel<Void> sendSampleEmail(@AuthenticationPrincipal User user) {
  //    userService.sendSampleEmail();
  //    return new ResponseModel<>(null);
  //  }

  @GetMapping(value = "/student/documents")
  @Operation(description = "Student get their list of uploaded documents")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  @PreAuthorize("hasAnyAuthority('STUDENT')")
  public ResponseModel<StudentDocumentModel> getStudentDocuments(
      @AuthenticationPrincipal User user) {
    StudentDocumentModel documentModel = userService.getStudentDocuments(user).join();
    return new ResponseModel<>(documentModel);
  }

  @PostMapping(value = "student/update_resume", consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(description = "Student update resume")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = UserProfile.class)})
  public ResponseModel<StudentDocumentModel> updateResume(
      @AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
    StudentDocumentModel studentDocument = userService.studentUpdateResume(user, file).join();
    return new ResponseModel<>(studentDocument);
  }

  @PostMapping(value = "/student/update_cover_letter", consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(description = "Student update cover letter")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = UserProfile.class)})
  public ResponseModel<StudentDocumentModel> studentUpdateCoverLetter(
      @AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
    StudentDocumentModel studentDocument = userService.studentUpdateCoverLetter(user, file).join();
    return new ResponseModel<>(studentDocument);
  }

  @PostMapping(value = "student/update_transcript", consumes = MULTIPART_FORM_DATA_VALUE)
  @Operation(description = "Student update transcript")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = UserProfile.class)})
  public ResponseModel<StudentDocumentModel> studentUpdateTranscript(
      @AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
    StudentDocumentModel studentDocument = userService.studentUpdateTranscript(user, file).join();
    return new ResponseModel<>(studentDocument);
  }

  @GetMapping(value = "/student/documents/{studentProfileId}")
  @Operation(description = "Get student documents by student profile id")
  @ApiResponses({@ApiResponse(code = 200, message = "Success")})
  @PreAuthorize("hasAnyAuthority('EMPLOYER', 'ADMIN', 'SUPERVISOR', 'COORDINATOR')")
  public ResponseModel<StudentDocumentModel> getStudentDocumentsById(
      @PathVariable("studentProfileId") String studentProfileId) {
    StudentDocumentModel documentModel = userService.getStudentDocument(studentProfileId).join();
    return new ResponseModel<>(documentModel);
  }
}
