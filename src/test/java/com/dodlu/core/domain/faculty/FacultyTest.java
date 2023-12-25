package com.dodlu.core.domain.faculty;

import static com.dodlu.core.stubs.TextDataFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.faculty.CreateFacultyRequest;
import com.dodlu.core.domain.faculty.model.FacultyModel;
import com.dodlu.core.domain.faculty.service.DefaultFacultyService;
import com.dodlu.core.persistence.faculty.entity.Faculty;
import com.dodlu.core.persistence.faculty.repository.FacultyRepository;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class FacultyTest {
  @Mock private UserRepository userRepository;
  @Mock private FacultyRepository facultyRepository;
  @Mock private CoreExceptionService exceptions;

  private DefaultFacultyService facultyService;

  @BeforeEach
  void setup() {
    openMocks(this);
    exceptions = new CoreExceptionService();
    facultyService = new DefaultFacultyService(userRepository, facultyRepository, exceptions);
  }

  @Test
  void createFacultyFails_whenFacultyNameAlreadyExists() {
    when(facultyRepository.existsByName(anyString())).thenReturn(true);

    CreateFacultyRequest request = buildCreateFacultyRequest();

    CompletableFuture<FacultyModel> result = facultyService.createFaculty(request);
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCause(exceptions.handleCreateBadRequest("Faculty already exists"))
        .extracting(Throwable::getCause)
        .hasFieldOrPropertyWithValue("code", BAD_REQUEST.value());
  }

  @Test
  void createFacultyFails_whenDeanAlreadyExists() {
    CreateFacultyRequest request = buildCreateFacultyRequest();

    when(facultyRepository.existsByName(anyString())).thenReturn(false);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(buildUser()));
    when(facultyRepository.existsByDeanId(anyString())).thenReturn(true);

    CompletableFuture<FacultyModel> result = facultyService.createFaculty(request);
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCause(exceptions.handleCreateBadRequest("User is already dean of another faculty"))
        .extracting(Throwable::getCause)
        .hasFieldOrPropertyWithValue("code", BAD_REQUEST.value());
  }

  @Test
  void shouldCreateFacultyWithEmptyDeanSuccessfully() {
    CreateFacultyRequest request = buildCreateFacultyRequest();

    when(facultyRepository.existsByName(anyString())).thenReturn(false);
    when(userRepository.findById(anyString())).thenReturn(Optional.empty());
    when(facultyRepository.save(any(Faculty.class)))
        .then(
            i -> {
              var entity = i.getArgument(0, Faculty.class);
              entity.setId(DEFAULT_UUID_STRING);
              return entity;
            });

    var result = facultyService.createFaculty(request).join();
    assertNotNull(result);
    assertEquals(result.getName(), request.getName());
    assertNull(result.getDean());
    verify(facultyRepository, times(1)).existsByName(request.getName());
    verify(userRepository, times(1)).findById(request.getDeanId());
    verify(facultyRepository, times(1)).save(any());
  }

  @Test
  void shouldCreateFacultyWithDeanSuccessfully() {
    CreateFacultyRequest request = buildCreateFacultyRequest();
    User dean = buildUser();

    when(facultyRepository.existsByName(anyString())).thenReturn(false);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(dean));
    when(facultyRepository.existsByDeanId(anyString())).thenReturn(false);
    when(facultyRepository.save(any(Faculty.class)))
        .then(
            i -> {
              var entity = i.getArgument(0, Faculty.class);
              entity.setId(DEFAULT_UUID_STRING);
              return entity;
            });

    var result = facultyService.createFaculty(request).join();
    assertNotNull(result);
    assertEquals(result.getName(), request.getName());
    assertNotNull(result.getDean());
    verify(facultyRepository, times(1)).existsByName(request.getName());
    verify(userRepository, times(1)).findById(request.getDeanId());
    verify(facultyRepository, times(1)).existsByDeanId(request.getDeanId());
    verify(facultyRepository, times(1)).save(any());
  }

  @Test
  void shouldGetAllFacultiesSuccessfully() {
    Pageable pageable = PageRequest.ofSize(10);
    var faculties = List.of(buildFaculty("Science"), buildFaculty("Social Science"));

    when(facultyRepository.findAll(isA(Pageable.class))).thenReturn(new PageImpl<>(faculties));

    var result = facultyService.getAllFaculties(pageable).join();
    assertNotNull(result);
    assertEquals(result.getContent().size(), faculties.size());
    verify(facultyRepository, times(1)).findAll(pageable);
  }
}
