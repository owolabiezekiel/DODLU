package com.dodlu.core.persistence.immunization.entity;

import static com.dodlu.core.app.utils.StringUtilities.emptyIfNull;
import static com.dodlu.core.app.utils.StringUtilities.formatPeriod;

import com.dodlu.core.domain.immunization.model.ParentModel;
import com.dodlu.core.domain.immunization.model.ParentWithNoChildModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "parents")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Parent extends BaseEntity {
  private String title;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String phoneNumber;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private LocalDate dateOfBirth;

  @Transient
  @Setter(AccessLevel.NONE)
  private String age;

  public String getAge() {
    LocalDate now = LocalDate.now();
    Period period = Period.between(this.dateOfBirth, now);
    return formatPeriod(period);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Parent that = (Parent) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public ParentModel toModel(Set<Child> children) {
    return ParentModel.builder()
        .id(id)
        .title(title)
        .firstName(firstName)
        .lastName(lastName)
        .phoneNumber(phoneNumber)
        .email(email)
        .dateOfBirth(dateOfBirth)
        .age(getAge())
        .children(
            emptyIfNull(children).stream()
                .map(Child::toModelWithoutImmunization)
                .collect(Collectors.toSet()))
        .build();
  }

  public ParentWithNoChildModel toModel() {
    return ParentWithNoChildModel.builder()
        .id(id)
        .title(title)
        .firstName(firstName)
        .lastName(lastName)
        .phoneNumber(phoneNumber)
        .email(email)
        .dateOfBirth(dateOfBirth)
        .age(getAge())
        .build();
  }
}
