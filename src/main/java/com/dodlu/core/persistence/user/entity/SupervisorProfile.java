package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.domain.user.model.SupervisorProfileModel;
import com.dodlu.core.persistence.base.UserProfile;
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
@Table(name = "supervisor_profiles")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class SupervisorProfile extends UserProfile {
  @Enumerated(EnumType.STRING)
  private Gender gender;

  private String title;

  @ManyToOne
  @JoinColumn(name = "employer_id", referencedColumnName = "id")
  private EmployerProfile employer;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    SupervisorProfile that = (SupervisorProfile) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public SupervisorProfileModel toModel() {
    return SupervisorProfileModel.builder()
        .id(id)
        .email(getEmail())
        .username(getUsername())
        .phoneNumber(getPhoneNumber())
        .firstName(getFirstName())
        .lastName(getLastName())
        .profileImageUrl(getProfileImageUrl())
        .gender(gender)
        .title(title)
        .employer(employer.toModel())
        .build();
  }
}
