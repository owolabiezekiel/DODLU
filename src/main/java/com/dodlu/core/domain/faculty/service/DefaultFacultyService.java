package com.dodlu.core.domain.faculty.service;

import static com.dodlu.core.domain.utils.UserUtils.getUserOrNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.faculty.CreateFacultyRequest;
import com.dodlu.core.domain.faculty.model.FacultyModel;
import com.dodlu.core.domain.utils.FacultyUtils;
import com.dodlu.core.persistence.faculty.entity.Faculty;
import com.dodlu.core.persistence.faculty.repository.FacultyRepository;
import com.dodlu.core.persistence.user.entity.User;
import com.dodlu.core.persistence.user.repository.UserRepository;
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
public class DefaultFacultyService {
  private final UserRepository userRepository;
  private final FacultyRepository facultyRepository;
  private final CoreExceptionService exceptionService;

  @Transactional
  public CompletableFuture<FacultyModel> createFaculty(CreateFacultyRequest request) {
    return supplyAsync(
        () -> {
          ensureFacultyNameIsUnique(request.getName());
          Faculty faculty = buildFacultyFromRequest(request);
          return facultyRepository.save(faculty).toModel();
        });
  }

  @Transactional
  public CompletableFuture<Page<FacultyModel>> getAllFaculties(Pageable pageable) {
    return supplyAsync(() -> facultyRepository.findAll(pageable).map(Faculty::toModel));
  }

  @Transactional
  public CompletableFuture<FacultyModel> getFacultyById(String id) {
    return supplyAsync(
        () -> FacultyUtils.getFacultyById(facultyRepository, exceptionService, id).toModel());
  }

  private void ensureFacultyNameIsUnique(String name) {
    log.info("Checking if faculty name -> {} <- is unique", name);
    if (facultyRepository.existsByName(name)) {
      throw exceptionService.handleCreateBadRequest("Faculty already exists");
    }
  }

  private void ensureUserIsNotDeanOfAnotherFaculty(String deanId) {
    if (facultyRepository.existsByDeanId(deanId)) {
      throw exceptionService.handleCreateBadRequest("User is already dean of another faculty");
    }
  }

  private Faculty buildFacultyFromRequest(CreateFacultyRequest request) {
    User dean = getUserOrNull(userRepository, request.getDeanId());
    if (dean == null) {
      log.info("Could not find dean with id. proceeding to create faculty without dean");
    } else {
      ensureUserIsNotDeanOfAnotherFaculty(request.getDeanId());
    }
    return Faculty.builder().name(request.getName()).dean(dean).build();
  }
}
