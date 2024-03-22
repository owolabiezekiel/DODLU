package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.domain.user.enums.Level;
import com.dodlu.core.domain.user.enums.Programme;
import com.dodlu.core.domain.user.model.*;
import com.dodlu.core.persistence.base.UserProfile;
import com.dodlu.core.persistence.department.entity.Department;
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
@Table(name = "student_profiles")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class StudentProfile extends UserProfile {
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Enumerated(EnumType.STRING)
  private Programme programme;

  @Enumerated(EnumType.STRING)
  private Level level;

  private Double cgpa;

  @ManyToOne
  @JoinColumn(name = "department_id", referencedColumnName = "id")
  private Department department;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    StudentProfile that = (StudentProfile) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public StudentProfileModel toModel() {
    return StudentProfileModel.builder()
        .id(id)
        .email(getEmail())
        .username(getUsername())
        .phoneNumber(getPhoneNumber())
        .firstName(getFirstName())
        .lastName(getLastName())
        .gender(gender)
        .profileImageUrl(getProfileImageUrl())
        .programme(programme)
        .level(level)
        .department(department.toModel())
        .cgpa(cgpa)
        .build();
  }

  public GeneralProfileModel toGeneralProfileModel() {
    return GeneralProfileModel.builder()
        .id(id)
        .email(getEmail())
        .username(getUsername())
        .phoneNumber(getPhoneNumber())
        .firstName(getFirstName())
        .lastName(getLastName())
        .gender(gender)
        .profileImageUrl(getProfileImageUrl())
        .programme(programme)
        .level(level)
        .cgpa(cgpa)
        .department(department.toModel())
        .build();
  }
}
