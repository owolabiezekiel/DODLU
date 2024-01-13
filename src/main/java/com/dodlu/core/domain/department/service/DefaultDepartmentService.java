package com.dodlu.core.domain.department.service;

import static com.dodlu.core.domain.utils.FacultyUtils.getFacultyById;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.department.CreateDepartmentRequest;
import com.dodlu.core.domain.department.model.DepartmentModel;
import com.dodlu.core.domain.utils.DepartmentUtils;
import com.dodlu.core.persistence.department.entity.Department;
import com.dodlu.core.persistence.department.repository.DepartmentRepository;
import com.dodlu.core.persistence.faculty.entity.Faculty;
import com.dodlu.core.persistence.faculty.repository.FacultyRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultDepartmentService {
  private final FacultyRepository facultyRepository;
  private final DepartmentRepository departmentRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<DepartmentModel> createDepartment(CreateDepartmentRequest request) {
    return supplyAsync(
        () -> {
          Faculty faculty =
              getFacultyById(
                  facultyRepository,
                  () -> exceptionService.handleCreateBadRequest("Faculty does not exist"),
                  request.getFacultyId());
          ensureDepartmentIsUniqueInFaculty(request);
          Department department = buildDepartmentFromRequest(request);
          department.setFaculty(faculty);
          return departmentRepository.save(department).toModel();
        });
  }

  @Transactional
  public CompletableFuture<Page<DepartmentModel>> getAllDepartments(Pageable pageable) {
    return supplyAsync(() -> departmentRepository.findAll(pageable).map(Department::toModel));
  }

  @Transactional
  public CompletableFuture<DepartmentModel> getDepartmentById(String id) {
    return supplyAsync(
        () ->
            DepartmentUtils.getDepartmentById(
                    departmentRepository,
                    () ->
                        exceptionService.handleCreateNotFoundException("Department does not exist"),
                    id)
                .toModel());
  }

  @Transactional
  public CompletableFuture<DepartmentModel> updateDepartment(
      String id, CreateDepartmentRequest request) {
    return supplyAsync(
        () -> {
          Department department = getDepartment(id);
          Faculty faculty =
              getFacultyById(
                  facultyRepository,
                  () -> exceptionService.handleCreateBadRequest("Faculty does not exist"),
                  request.getFacultyId());

          ensureDepartmentIsUniqueInFaculty(request);
          department.setName(request.getName()).setFaculty(faculty);
          return departmentRepository.save(department).toModel();
        });
  }

  private void ensureDepartmentIsUniqueInFaculty(CreateDepartmentRequest request) {
    log.info("Checking if department name -> {} <- is unique in faculty", request.getName());
    if (departmentRepository.existsByNameEqualsIgnoreCaseAndFacultyId(
        request.getName(), request.getFacultyId())) {
      throw exceptionService.handleCreateBadRequest("Department already exists in faculty");
    }
  }

  private Department getDepartment(String deptId) {
    return DepartmentUtils.getDepartmentById(departmentRepository, exceptionService, deptId);
  }

  private Department buildDepartmentFromRequest(CreateDepartmentRequest request) {
    return Department.builder().name(request.getName()).build();
  }
}
