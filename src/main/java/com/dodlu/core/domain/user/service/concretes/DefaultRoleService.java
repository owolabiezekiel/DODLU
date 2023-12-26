package com.dodlu.core.domain.user.service.concretes;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.user.CreateRoleRequest;
import com.dodlu.core.domain.user.model.RoleModel;
import com.dodlu.core.domain.user.service.abstracts.RoleService;
import com.dodlu.core.domain.utils.RoleUtils;
import com.dodlu.core.persistence.user.entity.Role;
import com.dodlu.core.persistence.user.repository.RoleRepository;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultRoleService implements RoleService {
  private final RoleRepository roleRepository;
  private final CoreExceptionService exceptionService;

  @Override
  public CompletableFuture<RoleModel> createRole(CreateRoleRequest request) {
    return supplyAsync(
        () -> {
          ensureRoleNameIsUnique(request.getName());
          ensureDescriptionIsUnique(request.getDescription());
          Role role = Role.fromRequest(request);
          return roleRepository.save(role).toModel();
        });
  }

  @Override
  public CompletableFuture<Page<RoleModel>> getAllRoles(Pageable pageable) {
    return supplyAsync(() -> roleRepository.findAll(pageable).map(Role::toModel));
  }

  @Override
  public CompletableFuture<RoleModel> getRoleById(String roleId) {
    return supplyAsync(
        () ->
            RoleUtils.getRoleById(
                    roleRepository,
                    () -> exceptionService.handleCreateNotFoundException("Role does not exist"),
                    roleId)
                .toModel());
  }

  @Override
  public CompletableFuture<Void> deleteRoleById(String roleId) {
    return runAsync(
        () -> {
          var role =
              roleRepository
                  .findById(roleId)
                  .orElseThrow(
                      () ->
                          exceptionService.handleCreateNotFoundException(
                              "Role does not exist", roleId));
          roleRepository.deleteById(role.getId());
        });
  }

  private void ensureRoleNameIsUnique(String name) {
    log.info("Checking if role name -> {} <- is unique", name);
    if (roleRepository.existsByName(name)) {
      throw exceptionService.handleCreateBadRequest("role name already exists");
    }
  }

  private void ensureDescriptionIsUnique(String description) {
    log.info("Checking if role description -> {} <- is unique", description);
    if (roleRepository.existsByDescription(description)) {
      throw exceptionService.handleCreateBadRequest("description exists for another role");
    }
  }
}
