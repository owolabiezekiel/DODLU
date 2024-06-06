package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.request.immunization.CreateChildRequest;
import com.dodlu.core.app.model.request.immunization.CreateParentRequest;
import com.dodlu.core.app.model.request.immunization.SearchAttributes;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.immunization.model.ChildWithParentModel;
import com.dodlu.core.domain.immunization.model.ParentModel;
import com.dodlu.core.domain.immunization.service.ImmunizationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/immuno", produces = APPLICATION_JSON_VALUE)
@Slf4j
@CrossOrigin("*")
public class ImmunizationController {
  private final ImmunizationService immunizationService;

  @PostMapping(value = "/parent", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "create parent details")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<ParentModel> createParent(@Valid @RequestBody CreateParentRequest request) {
    ParentModel parent = immunizationService.createParent(request).join();
    return new ResponseModel<>(parent);
  }

  @GetMapping(value = "/parent")
  @Operation(description = "get single parent details")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<ParentModel> getParent(@ModelAttribute SearchAttributes search) {
    ParentModel parent = immunizationService.getSingleParent(search).join();
    return new ResponseModel<>(parent);
  }

  @PostMapping(value = "/parent/{parentId}/child", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "create child details for parent")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<ParentModel> createChild(
      @Valid @RequestBody CreateChildRequest request, @PathVariable String parentId) {
    ParentModel parent = immunizationService.createChild(parentId, request).join();
    return new ResponseModel<>(parent);
  }

  @GetMapping(value = "/child/{childId}")
  @Operation(description = "get single child details")
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = ChildWithParentModel.class)
  })
  public ResponseModel<ChildWithParentModel> getChildById(@PathVariable String childId) {
    ChildWithParentModel child = immunizationService.getSingleChild(childId).join();
    return new ResponseModel<>(child);
  }

  //    @GetMapping
  //    @Operation(description = "Get a Parent details")
  //    @ApiResponses({
  //      @ApiResponse(
  //          code = 200,
  //          message = "Success",
  //          response = FacultyResult.class,
  //          responseContainer = "page")
  //    })
  //    public ResponseModel<FacultyResult> getAllFaculties(@PageableDefault Pageable pageable) {
  //      Page<FacultyModel> facultyPage = facultyService.getAllFaculties(pageable).join();
  //      FacultyResult facultyResult = new FacultyResult(facultyPage);
  //      return new ResponseModel<>(facultyResult);
  //    }

}
