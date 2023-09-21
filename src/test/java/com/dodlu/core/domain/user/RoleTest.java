package com.dodlu.core.domain.user;

import static com.dodlu.core.stubs.TextDataFixtures.DEFAULT_UUID_STRING;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.user.CreateRoleRequest;
import com.dodlu.core.domain.user.model.RoleModel;
import com.dodlu.core.domain.user.service.abstracts.RoleService;
import com.dodlu.core.domain.user.service.concretes.DefaultRoleService;
import com.dodlu.core.persistence.user.entity.Role;
import com.dodlu.core.persistence.user.repository.RoleRepository;
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
  void shouldCreateRoleSuccessfully() {
    CreateRoleRequest createRoleRequest = buildCreateRoleRequest("STUDENT");

    when(roleRepository.existsByName(any())).thenReturn(false);
    when(roleRepository.existsByDescription(any())).thenReturn(false);
    when(roleRepository.save(any(Role.class)))
        .then(
            i -> {
              var entity = i.getArgument(0, Role.class);
              entity.setId("id");
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

  @Test
  void shouldGetAllAvatarsSuccessfully() {
    Pageable pageable = PageRequest.ofSize(10);
    var roles = List.of(buildRole("ADMIN"), buildRole("STUDENT"));

    when(roleRepository.findAll(isA(Pageable.class))).thenReturn(new PageImpl<>(roles));

    var result = roleService.getAllRoles(pageable).join();
    assertNotNull(result);
    assertEquals(result.getContent().size(), roles.size());
    verify(roleRepository, times(1)).findAll(pageable);
  }

  @Test
  void getRoleByIdFails_whenRoleDoesNotExist() {
    when(roleRepository.findById(eq(DEFAULT_UUID_STRING))).thenReturn(Optional.empty());

    var result = roleService.getRoleById(DEFAULT_UUID_STRING);
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCause(exceptions.handleCreateBadRequest("Role does not exist"))
        .extracting(Throwable::getCause)
        .hasFieldOrPropertyWithValue("code", NOT_FOUND.value());
  }

  @Test
  void shouldGetRoleByIdSuccessfully() {
    when(roleRepository.findById(eq(DEFAULT_UUID_STRING)))
        .thenReturn(Optional.of(buildRole("ADMIN")));
    var result = roleService.getRoleById(DEFAULT_UUID_STRING).join();
    assertNotNull(result);
    assertEquals(result.getName(), "ADMIN");
    assertEquals(result.getId(), DEFAULT_UUID_STRING);
    verify(roleRepository, times(1)).findById(DEFAULT_UUID_STRING);
  }

  @Test
  void deleteRoleFails_whenRoleDoesNotExist() {
    when(roleRepository.findById(eq(DEFAULT_UUID_STRING))).thenReturn(Optional.empty());
    var result = roleService.deleteRoleById(DEFAULT_UUID_STRING);
    assertThatThrownBy(result::join)
        .isInstanceOf(CompletionException.class)
        .hasCause(exceptions.handleCreateBadRequest("Role does not exist"))
        .extracting(Throwable::getCause)
        .hasFieldOrPropertyWithValue("code", NOT_FOUND.value());
  }

  @Test
  void shouldDeleteRoleSuccessfully() {
    when(roleRepository.findById(eq(DEFAULT_UUID_STRING)))
        .thenReturn(Optional.of(buildRole("ADMIN")));
    var result = roleService.deleteRoleById(DEFAULT_UUID_STRING).join();
    assertNull(result);
    verify(roleRepository, times(1)).findById(DEFAULT_UUID_STRING);
    verify(roleRepository, times(1)).deleteById(DEFAULT_UUID_STRING);
  }

  private CreateRoleRequest buildCreateRoleRequest(String name) {
    return CreateRoleRequest.builder()
        .name(name)
        .description(format("%s Description", name))
        .build();
  }

  private Role buildRole(String name) {
    return Role.builder()
        .id(DEFAULT_UUID_STRING)
        .name(name)
        .description(format("%s Description", name))
        .build();
  }
}
