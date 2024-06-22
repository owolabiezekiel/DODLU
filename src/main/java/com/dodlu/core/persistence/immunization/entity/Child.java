package com.dodlu.core.persistence.immunization.entity;

import static com.dodlu.core.app.utils.StringUtilities.emptyIfNull;

import com.dodlu.core.domain.immunization.model.ChildModel;
import com.dodlu.core.domain.immunization.model.ChildWithParentModel;
import com.dodlu.core.domain.user.enums.Gender;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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

  @Transient
  @Setter(AccessLevel.NONE)
  private String age;

  @ManyToOne
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "parent_id", referencedColumnName = "id")
  private Parent parent;

  @ManyToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  @JoinTable(
      name = "immunization_record",
      joinColumns = @JoinColumn(name = "child_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "immunization_id", referencedColumnName = "id"))
  private Set<Immunization> immunizations;

  public String getAge() {
    LocalDate now = LocalDate.now();
    Period period = Period.between(this.dateOfBirth, now);
    return formatPeriod(period);
  }

  private String formatPeriod(Period period) {
    return String.format(
        "%d years, %d months, and %d days",
        period.getYears(), period.getMonths(), period.getDays());
  }

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
        .age(getAge())
        .immunizations(
            emptyIfNull(immunizations).stream()
                .map(Immunization::toModel)
                .collect(Collectors.toSet()))
        .build();
  }

  public ChildWithParentModel toChildWithParentModel() {
    return ChildWithParentModel.builder()
        .id(id)
        .firstName(firstName)
        .lastName(lastName)
        .gender(gender)
        .dateOfBirth(dateOfBirth)
        .age(getAge())
        .parent(parent.toModel())
        .immunizations(
            emptyIfNull(immunizations).stream()
                .map(Immunization::toModel)
                .collect(Collectors.toSet()))
        .build();
  }
}
