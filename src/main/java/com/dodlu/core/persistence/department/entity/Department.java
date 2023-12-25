package com.dodlu.core.persistence.department.entity;

import com.dodlu.core.domain.department.model.DepartmentModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.faculty.entity.Faculty;
import com.dodlu.core.persistence.user.entity.User;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "departments")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Department extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @OneToOne
  @JoinColumn(name = "hod_id", referencedColumnName = "id")
  private User hod;

  @ManyToOne
  @JoinColumn(name = "coordinator_id", referencedColumnName = "id")
  private User coordinator;

  @OneToOne
  @JoinColumn(name = "faculty_id", referencedColumnName = "id")
  private Faculty faculty;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Department that = (Department) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public DepartmentModel toModel() {
    return DepartmentModel.builder()
        .id(id)
        .name(name)
        .hod(hod == null ? null : hod.toModel())
        .coordinator(coordinator == null ? null : coordinator.toModel())
        .faculty(faculty.toModel())
        .build();
  }
}
