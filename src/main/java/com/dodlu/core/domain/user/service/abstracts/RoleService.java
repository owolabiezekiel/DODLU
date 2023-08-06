package com.dodlu.core.domain.user.service.abstracts;

import com.dodlu.core.app.model.request.user.CreateRoleRequest;
import com.dodlu.core.domain.user.model.RoleModel;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
  CompletableFuture<RoleModel> createRole(CreateRoleRequest request);

  CompletableFuture<Page<RoleModel>> getAllRoles(Pageable pageable);

  CompletableFuture<RoleModel> getRoleById(Long roleId);

  CompletableFuture<Void> deleteRoleById(Long roleId);
}
