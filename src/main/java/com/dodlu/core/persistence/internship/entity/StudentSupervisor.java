package com.dodlu.core.persistence.internship.entity;

import com.dodlu.core.domain.internship.model.StudentSupervisorModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.company.entity.Company;
import com.dodlu.core.persistence.user.entity.StudentProfile;
import com.dodlu.core.persistence.user.entity.SupervisorProfile;
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
@Table(name = "student_supervisor")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class StudentSupervisor extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "company_id", referencedColumnName = "id")
  private Company company;

  @ManyToOne
  @JoinColumn(name = "supervisor_id", referencedColumnName = "id")
  private SupervisorProfile supervisor;

  @ManyToOne
  @JoinColumn(name = "student_id", referencedColumnName = "id")
  private StudentProfile student;

  private Boolean active;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StudentSupervisor that = (StudentSupervisor) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public StudentSupervisorModel toModel() {
    return StudentSupervisorModel.builder()
        .id(id)
        .student(student.toModel())
        .supervisor(supervisor.toModel())
        .active(active)
        .build();
  }
}
