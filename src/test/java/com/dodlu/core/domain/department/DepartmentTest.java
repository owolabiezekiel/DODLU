package com.dodlu.core.domain.department;

import static com.dodlu.core.stubs.TextDataFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.department.CreateDepartmentRequest;
import com.dodlu.core.domain.department.model.DepartmentModel;
import com.dodlu.core.domain.department.service.DefaultDepartmentService;
import com.dodlu.core.persistence.department.entity.Department;
import com.dodlu.core.persistence.department.repository.DepartmentRepository;
import com.dodlu.core.persistence.faculty.entity.Faculty;
import com.dodlu.core.persistence.faculty.repository.FacultyRepository;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepartmentTest {
  @Mock private DepartmentRepository departmentRepository;
  @Mock private FacultyRepository facultyRepository;
  @Mock private CoreExceptionService exceptions;

  private DefaultDepartmentService departmentService;

  @BeforeEach
  void setup() {
    openMocks(this);
    exceptions = new CoreExceptionService();
    departmentService =
        new DefaultDepartmentService(facultyRepository, departmentRepository, exceptions);
  }

  @Test
  void createDepartmentFails_whenFacultyDoesntExist() {
    when(facultyRepository.findById(anyString())).thenReturn(Optional.empty());

    CreateDepartmentRequest request = buildCreateDepartmentRequest();

    CompletableFuture<DepartmentModel> result = departmentService.createDepartment(request);
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCause(exceptions.handleCreateBadRequest("Faculty does not exist"))
        .extracting(Throwable::getCause)
        .hasFieldOrPropertyWithValue("code", BAD_REQUEST.value());
  }

  @Test
  void createDepartmentFails_whenItAlreadyExistsInFaculty() {
    when(facultyRepository.findById(anyString())).thenReturn(Optional.of(buildFaculty()));
    when(departmentRepository.existsByNameEqualsIgnoreCaseAndFacultyId(anyString(), anyString()))
        .thenReturn(true);

    CreateDepartmentRequest request = buildCreateDepartmentRequest();

    CompletableFuture<DepartmentModel> result = departmentService.createDepartment(request);
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCause(exceptions.handleCreateBadRequest("Department already exists in faculty"))
        .extracting(Throwable::getCause)
        .hasFieldOrPropertyWithValue("code", BAD_REQUEST.value());
  }

  @Test
  void shouldCreateDepartmentSuccessfully() {
    CreateDepartmentRequest request = buildCreateDepartmentRequest();
    Faculty faculty = buildFaculty();

    when(facultyRepository.findById(anyString())).thenReturn(Optional.of(faculty));
    when(departmentRepository.existsByNameEqualsIgnoreCaseAndFacultyId(anyString(), anyString()))
        .thenReturn(false);
    when(departmentRepository.save(ArgumentMatchers.any(Department.class)))
        .then(
            i -> {
              var entity = i.getArgument(0, Department.class);
              entity.setId(DEFAULT_UUID_STRING);
              return entity;
            });

    var result = departmentService.createDepartment(request).join();
    assertNotNull(result);
    assertEquals(result.getName(), request.getName());
    assertNotNull(result.getFaculty());
    assertNull(result.getHod());
    assertNull(result.getCoordinator());

    verify(facultyRepository, times(1)).findById(request.getFacultyId());
    verify(departmentRepository, times(1))
        .existsByNameEqualsIgnoreCaseAndFacultyId(request.getName(), request.getFacultyId());
    verify(departmentRepository, times(1)).save(any());
  }
}
