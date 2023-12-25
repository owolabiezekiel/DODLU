package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.faculty.CreateFacultyRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.faculty.model.FacultyModel;
import com.dodlu.core.domain.faculty.service.DefaultFacultyService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
