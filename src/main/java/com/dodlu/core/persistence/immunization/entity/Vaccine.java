package com.dodlu.core.persistence.immunization.entity;

import com.dodlu.core.domain.immunization.model.VaccineModel;
import com.dodlu.core.domain.immunization.model.enums.AdministerRoute;
import com.dodlu.core.domain.immunization.model.enums.AgeTarget;
import com.dodlu.core.domain.immunization.model.enums.DosageType;
import com.dodlu.core.domain.immunization.model.enums.VaccineType;
import com.dodlu.core.persistence.base.BaseEntity;
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
@Table(name = "vaccine")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Vaccine extends BaseEntity {
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AgeTarget ageTarget;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private VaccineType type;

  @Column(nullable = false)
  private Double dosage;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DosageType dosageType;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AdministerRoute routeOfAdministration;

  @Column(nullable = false)
  private String site;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Vaccine that = (Vaccine) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public VaccineModel toModel() {
    return VaccineModel.builder()
        .id(id)
        .ageTarget(ageTarget)
        .type(type)
        .dosage(dosage)
        .dosageType(dosageType)
        .routeOfAdministration(routeOfAdministration)
        .site(site)
        .build();
  }
}
