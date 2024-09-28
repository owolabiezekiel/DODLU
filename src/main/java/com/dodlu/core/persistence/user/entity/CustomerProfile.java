package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.domain.user.enums.Gender;
import com.dodlu.core.domain.user.model.CustomerProfileModel;
import com.dodlu.core.domain.user.model.GeneralProfileModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.location.entity.Country;
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
@Table(name = "customer_profiles")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class CustomerProfile extends BaseEntity {
  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String username;

  private String phoneNumber;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  private String middleName;
  private String profileImageUrl;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @ManyToOne
  @JoinColumn(name = "country_id", referencedColumnName = "id")
  private Country country;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    CustomerProfile that = (CustomerProfile) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public CustomerProfileModel toModel() {
    return CustomerProfileModel.builder()
        .id(id)
        .email(getEmail())
        .username(getUsername())
        .phoneNumber(getPhoneNumber())
        .firstName(getFirstName())
        .lastName(getLastName())
        .middleName(middleName)
        .gender(gender)
        .profileImageUrl(getProfileImageUrl())
        .country(country.toModel())
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
        .middleName(middleName)
        .country(country.toModel())
        .build();
  }
}
