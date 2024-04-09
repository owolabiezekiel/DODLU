package com.dodlu.core.persistence.location.entity;

import com.dodlu.core.domain.location.model.StateModel;
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
@Table(name = "states")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class State extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "country_id", referencedColumnName = "id")
  private Country country;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    State that = (State) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public StateModel toModel() {
    return StateModel.builder().id(id).name(name).country(country.toModel()).build();
  }
}
