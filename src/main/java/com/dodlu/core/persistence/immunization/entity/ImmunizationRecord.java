package com.dodlu.core.persistence.immunization.entity;

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
@Table(name = "immunization_record")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class ImmunizationRecord extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "child_id")
  private Child child;

  @ManyToOne
  @JoinColumn(name = "immunization_id")
  private Immunization immunization;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ImmunizationRecord that = (ImmunizationRecord) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
