package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.app.model.request.user.CreateRoleRequest;
import com.dodlu.core.domain.user.model.RoleModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "roles")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {
  @Column(nullable = false)
  private String name;

  private String description;
  private String authority;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Role that = (Role) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public static Role fromRequest(CreateRoleRequest request) {
    return Role.builder().name(request.getName()).description(request.getDescription()).build();
  }

  public RoleModel toModel() {
    return RoleModel.builder()
        .id(id)
        .name(name)
        .description(description)
        .authority(authority)
        .build();
  }

  @Override
  public String getAuthority() {
    return this.authority;
  }
}
