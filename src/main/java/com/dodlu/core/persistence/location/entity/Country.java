package com.dodlu.core.persistence.location.entity;

import com.dodlu.core.domain.location.model.CountryModel;
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
@Table(name = "countries")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class Country extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String iso3Code;

  @Column(unique = true)
  private String phoneExtensionCode;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Country that = (Country) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public CountryModel toModel() {
    return CountryModel.builder()
        .id(id)
        .name(name)
        .iso3Code(iso3Code)
        .phoneExtensionCode(phoneExtensionCode)
        .build();
  }
}
