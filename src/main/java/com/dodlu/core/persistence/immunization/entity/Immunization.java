package com.dodlu.core.persistence.immunization.entity;

import com.dodlu.core.domain.immunization.model.ImmunizationModel;
import com.dodlu.core.persistence.base.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
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
@Table(name = "immunization")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Immunization extends BaseEntity {
  @ManyToOne private Vaccine vaccine;

  @Column(nullable = false)
  private LocalDate minimumAdministerDate;

  @Column(nullable = false)
  private Boolean administered;

  private LocalDate dateOfAdministration;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Immunization that = (Immunization) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public ImmunizationModel toModel() {
    return ImmunizationModel.builder()
        .id(id)
        .vaccine(vaccine.toModel())
        .minimumAdministerDate(minimumAdministerDate)
        .administered(administered)
        .dateOfAdministration(dateOfAdministration)
        .build();
  }
}
