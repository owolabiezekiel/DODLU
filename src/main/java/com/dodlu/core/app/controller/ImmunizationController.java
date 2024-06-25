package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.dodlu.core.app.model.filters.ChildFilter;
import com.dodlu.core.app.model.filters.ParentFilter;
import com.dodlu.core.app.model.request.immunization.CreateChildRequest;
import com.dodlu.core.app.model.request.immunization.CreateImmunizationRequest;
import com.dodlu.core.app.model.request.immunization.CreateParentRequest;
import com.dodlu.core.app.model.request.immunization.SearchAttributes;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.immunization.model.*;
import com.dodlu.core.domain.immunization.service.ImmunizationService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/immuno", produces = APPLICATION_JSON_VALUE)
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

  @PutMapping(value = "/parent/{parentId}")
  @Operation(description = "update parent details")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<ParentModel> updateParent(
      @PathVariable String parentId, @Valid @RequestBody CreateParentRequest request) {
    ParentModel parent = immunizationService.updateParent(parentId, request).join();
    return new ResponseModel<>(parent);
  }

  @GetMapping(value = "/parent")
  @Operation(description = "get single parent details")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<ParentModel> getParent(@ModelAttribute SearchAttributes search) {
    ParentModel parent = immunizationService.getSingleParent(search).join();
    return new ResponseModel<>(parent);
  }

  @GetMapping(value = "/parents")
  @Operation(description = "get all parent details")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = ParentModel.class,
        responseContainer = "page")
  })
  public ResponseModel<Page<ParentWithNoChildModel>> getParent(
      @PageableDefault Pageable pageable, @ModelAttribute ParentFilter filter) {
    Page<ParentWithNoChildModel> parents =
        immunizationService.getAllParents(pageable, filter).join();
    return new ResponseModel<>(parents);
  }

  @GetMapping(value = "/parents/count")
  @Operation(description = "get parent count")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Long.class)})
  public ResponseModel<Long> getParentCount() {
    Long parentCount = immunizationService.getParentsCount().join();
    return new ResponseModel<>(parentCount);
  }

  @PostMapping(value = "/parent/{parentId}/child", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "create child details for parent")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<ParentModel> createChild(
      @Valid @RequestBody CreateChildRequest request, @PathVariable String parentId) {
    ParentModel parent = immunizationService.createChild(parentId, request).join();
    return new ResponseModel<>(parent);
  }

  @PutMapping(value = "/child/{childId}")
  @Operation(description = "update child details")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<ChildModel> updateChild(
      @PathVariable String childId, @Valid @RequestBody CreateChildRequest request) {
    ChildModel parent = immunizationService.updateChild(childId, request).join();
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

  @GetMapping(value = "/children")
  @Operation(description = "get all children details")
  @ApiResponses({
    @ApiResponse(
        code = 200,
        message = "Success",
        response = ChildWithParentModel.class,
        responseContainer = "page")
  })
  public ResponseModel<Page<ChildWithParentModel>> getChildren(
      @PageableDefault Pageable pageable, @ModelAttribute ChildFilter filter) {
    Page<ChildWithParentModel> children =
        immunizationService.getAllChildren(pageable, filter).join();
    return new ResponseModel<>(children);
  }

  @GetMapping(value = "/children/count")
  @Operation(description = "get children count")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Long.class)})
  public ResponseModel<Long> getChildrenCount() {
    Long childrenCount = immunizationService.getChildrenCount().join();
    return new ResponseModel<>(childrenCount);
  }

  @PostMapping(value = "/immuno-record/{childId}", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "create immunization for child")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<Void> createImmunization(
      @Valid @RequestBody CreateImmunizationRequest request, @PathVariable String childId) {
    immunizationService.createImmunizationRecord(childId, request).join();
    return new ResponseModel<>(null);
  }

  @GetMapping(value = "/immuno-record/count")
  @Operation(description = "get administered immunization count")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Long.class)})
  public ResponseModel<Long> getAdministeredImmunizationCount() {
    Long administeredImmunizationCount =
        immunizationService.getAdministeredImmunizationCount().join();
    return new ResponseModel<>(administeredImmunizationCount);
  }

  @PutMapping(value = "/immuno-record/{immunoRecordId}")
  @Operation(description = "update immunization record")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = ParentModel.class)})
  public ResponseModel<Void> updateImmunizationRecord(
      @PathVariable String immunoRecordId, @Valid @RequestBody CreateImmunizationRequest request) {
    immunizationService.updateImmunizationRecord(immunoRecordId, request).join();
    return new ResponseModel<>(null);
  }

  @GetMapping(value = "/pending/today")
  @Operation(description = "get today's pending immunization")
  @ApiResponses({@ApiResponse(code = 200, message = "Success", response = Long.class)})
  public ResponseModel<Page<ImminentImmunoRecordModel>> getTodayImminentImmunization(
      @PageableDefault Pageable pageable) {
    Page<ImminentImmunoRecordModel> records =
        immunizationService.getPendingImmunizationToday(pageable).join();
    return new ResponseModel<>(records);
  }
}
