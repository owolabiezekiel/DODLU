package com.dodlu.core.persistence.company.entity;

import com.dodlu.core.domain.user.enums.Industry;
import com.dodlu.core.domain.user.model.CompanyModel;
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
@Table(name = "companies")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Company extends BaseEntity {
  @Column(nullable = false)
  private String name;

  private String vision;
  private String mission;

  @Enumerated(EnumType.STRING)
  private Industry industry;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Company that = (Company) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public CompanyModel toModel() {
    return CompanyModel.builder()
        .id(id)
        .name(name)
        .industry(industry)
        .vision(vision)
        .mission(mission)
        .build();
  }
}
