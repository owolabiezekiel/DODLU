package com.dodlu.core.persistence.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
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
  @Id @UuidGenerator() protected String id;

  @JsonIgnore @CreationTimestamp LocalDateTime createdAt;
  @JsonIgnore @UpdateTimestamp LocalDateTime updatedAt;
  @JsonIgnore @CreatedBy String createdBy;
  @JsonIgnore @LastModifiedBy String UpdatedBy;
  protected boolean isDeleted;

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
