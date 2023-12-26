package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.domain.user.enums.Industry;
import com.dodlu.core.domain.user.model.EmployerProfileModel;
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
@Table(name = "employer_profiles")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class EmployerProfile extends UserProfile {
  private String companyName;
  private String vision;
  private String mission;

  @Enumerated(EnumType.STRING)
  private Industry industry;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    EmployerProfile that = (EmployerProfile) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public EmployerProfileModel toModel() {
    return EmployerProfileModel.builder()
        .id(id)
        .email(getEmail())
        .username(getUsername())
        .phoneNumber(getPhoneNumber())
        .firstName(getFirstName())
        .lastName(getLastName())
        .profileImageUrl(getProfileImageUrl())
        .companyName(companyName)
        .vision(vision)
        .mission(mission)
        .industry(industry)
        .build();
  }
}
