package com.dodlu.core.persistence.immunization.entity;

import com.dodlu.core.domain.immunization.model.ChildModel;
import com.dodlu.core.domain.immunization.model.ChildWithParentModel;
import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
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
@Table(name = "child")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Child extends BaseEntity {
  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(nullable = false)
  private LocalDate dateOfBirth;

  @ManyToOne
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  private Parent parent;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Child that = (Child) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public ChildModel toModel() {
    return ChildModel.builder()
        .id(id)
        .firstName(firstName)
        .lastName(lastName)
        .gender(gender)
        .dateOfBirth(dateOfBirth)
        .build();
  }

  public ChildWithParentModel toChildWithParentModel() {
    return ChildWithParentModel.builder()
        .id(id)
        .firstName(firstName)
        .lastName(lastName)
        .gender(gender)
        .dateOfBirth(dateOfBirth)
        .parent(parent.toModel())
        .build();
  }
}
