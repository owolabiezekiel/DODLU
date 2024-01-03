package com.dodlu.core.persistence.internship.entity;

import com.dodlu.core.domain.internship.model.PlacementModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.company.entity.Company;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
@Table(name = "placements")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Placement extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "company_id", referencedColumnName = "id")
  private Company company;

  @ManyToOne
  @JoinColumn(name = "student_id", referencedColumnName = "id")
  private StudentProfile student;

  private LocalDateTime validUntil;
  private Boolean active;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Placement that = (Placement) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public PlacementModel toModel() {
    return PlacementModel.builder()
        .id(id)
        .company(company.toModel())
        .student(student.toModel())
        .validUntil(validUntil)
        .active(active)
        .build();
  }

  public PlacementModel toModel(Placement placement) {
    return placement == null ? null : placement.toModel();
  }
}
