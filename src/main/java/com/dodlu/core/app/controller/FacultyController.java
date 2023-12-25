package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.faculty.CreateFacultyRequest;
import com.dodlu.core.app.model.response.FacultyResult;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.faculty.model.FacultyModel;
import com.dodlu.core.domain.faculty.service.DefaultFacultyService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/faculty", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class FacultyController {
  private final DefaultFacultyService facultyService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "create faculty")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = FacultyModel.class)})
  public ResponseModel<FacultyModel> createFaculty(
      @Valid @RequestBody CreateFacultyRequest request) {
    FacultyModel faculty = facultyService.createFaculty(request).join();
    return new ResponseModel<>(faculty);
  }

  @GetMapping
  @Operation(description = "get all roles")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = FacultyResult.class,
        responseContainer = "page")
  })
  public ResponseModel<FacultyResult> getAllFaculties(@PageableDefault Pageable pageable) {
    Page<FacultyModel> facultyPage = facultyService.getAllFaculties(pageable).join();
    FacultyResult facultyResult = new FacultyResult(facultyPage);
    return new ResponseModel<>(facultyResult);
  }

  @GetMapping(value = "/{id}")
  @Operation(description = "get single faculty")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = FacultyModel.class)})
  public ResponseModel<FacultyModel> getFaculty(@PathVariable("id") String id) {
    FacultyModel faculty = facultyService.getFacultyById(id).join();
    return new ResponseModel<>(faculty);
  }
}
