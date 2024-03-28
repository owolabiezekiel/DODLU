package com.dodlu.core.persistence.internship.entity;

import com.dodlu.core.domain.internship.model.LogbookModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import jakarta.persistence.*;
import java.time.LocalDate;
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
@Table(name = "logbooks")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Logbook extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "student_id", referencedColumnName = "id")
  private StudentProfile student;

  private Integer capacityInMonths;
  private LocalDateTime validUntil;
  private Boolean active;
  private Integer supervisorGrade;
  private Integer itccGrade;
  private LocalDate startDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Logbook that = (Logbook) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public LogbookModel toModel() {
    return LogbookModel.builder()
        .id(id)
        .student(student.toModel())
        .validUntil(validUntil)
        .capacityInMonths(capacityInMonths)
        .active(active)
        .supervisorGrade(supervisorGrade)
        .itccGrade(itccGrade)
        .startDate(startDate)
        .build();
  }
}
