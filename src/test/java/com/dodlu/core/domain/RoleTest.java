package com.dodlu.core.domain;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.user.CreateRoleRequest;
import com.dodlu.core.domain.user.model.RoleModel;
import com.dodlu.core.domain.user.service.abstracts.RoleService;
import com.dodlu.core.domain.user.service.concretes.DefaultRoleService;
import com.dodlu.core.persistence.user.entity.Role;
import com.dodlu.core.persistence.user.repository.RoleRepository;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleTest {
  @Mock private RoleRepository roleRepository;
  @Mock private CoreExceptionService exceptions;
  private RoleService roleService;

  @BeforeEach
  void setup() {
    openMocks(this);
    exceptions = new CoreExceptionService();
    roleService = new DefaultRoleService(roleRepository, exceptions);
  }

  @Test
  void createRoleFails_whenThereIsDuplicateName() {
    when(roleRepository.existsByName(any())).thenReturn(true);
    CreateRoleRequest createRoleRequest = buildCreateRoleRequest("ADMIN");

    CompletableFuture<RoleModel> result = roleService.createRole(createRoleRequest);
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCause(exceptions.handleCreateBadRequest("role name already exists"))
        .extracting(Throwable::getCause)
        .hasFieldOrPropertyWithValue("code", BAD_REQUEST.value());
  }

  @Test
  void createRoleFails_whenThereIsDuplicateDescription() {
    when(roleRepository.existsByName(any())).thenReturn(false);
    when(roleRepository.existsByDescription(any())).thenReturn(true);
    CreateRoleRequest createRoleRequest = buildCreateRoleRequest("STUDENT");

    CompletableFuture<RoleModel> result = roleService.createRole(createRoleRequest);
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCause(exceptions.handleCreateBadRequest("description exists for another role"))
        .extracting(Throwable::getCause)
        .hasFieldOrPropertyWithValue("code", BAD_REQUEST.value());
  }

  @Test
  void createRoleSuccessfully() {
    CreateRoleRequest createRoleRequest = buildCreateRoleRequest("STUDENT");

    when(roleRepository.existsByName(any())).thenReturn(false);
    when(roleRepository.existsByDescription(any())).thenReturn(false);
    when(roleRepository.save(any(Role.class)))
        .then(
            i -> {
              var entity = i.getArgument(0, Role.class);
              entity.setId(1L);
              return entity;
            });

    var result = roleService.createRole(createRoleRequest).join();
    assertNotNull(result);
    assertEquals(result.getName(), createRoleRequest.getName());
    assertEquals(result.getDescription(), createRoleRequest.getDescription());
    verify(roleRepository, times(1)).existsByDescription(any());
    verify(roleRepository, times(1)).existsByName(any());
    verify(roleRepository, times(1)).save(any());
  }

  private CreateRoleRequest buildCreateRoleRequest(String name) {
    return CreateRoleRequest.builder()
        .name(name)
        .description(format("%s Description", name))
        .build();
  }
}
