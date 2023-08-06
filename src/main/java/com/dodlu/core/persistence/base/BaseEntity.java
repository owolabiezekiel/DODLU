package com.dodlu.core.persistence.base;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @CreationTimestamp LocalDateTime createdAt;
  @UpdateTimestamp LocalDateTime modifiedDate;
  @CreatedBy String createdBy;
  @LastModifiedBy String modifiedBy;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BaseEntity that = (BaseEntity) o;
    return Objects.nonNull(id) ? id.equals(that.id) : id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
