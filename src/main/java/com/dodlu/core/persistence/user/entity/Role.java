package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.app.model.request.user.CreateRoleRequest;
import com.dodlu.core.domain.user.model.RoleModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "role")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Role extends BaseEntity {
  private String name;
  private String description;

  public static Role fromRequest(CreateRoleRequest request) {
    return Role.builder().name(request.getName()).description(request.getDescription()).build();
  }

  public RoleModel toModel() {
    return RoleModel.builder().id(id).name(name).description(description).build();
  }
}
