package com.dodlu.core.persistence.faculty.entity;

import com.dodlu.core.domain.faculty.model.FacultyModel;
import com.dodlu.core.persistence.base.BaseEntity;
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
@Table(name = "faculties")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Faculty extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @OneToOne
  @JoinColumn(name = "dean_id", referencedColumnName = "id")
  private User dean;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Faculty that = (Faculty) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public FacultyModel toModel() {
    return FacultyModel.builder()
        .id(id)
        .name(name)
        .dean(dean == null ? null : dean.toModel())
        .build();
  }
}
