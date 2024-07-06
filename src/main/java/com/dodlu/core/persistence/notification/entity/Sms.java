package com.dodlu.core.persistence.notification.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "sms")
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Sms {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  private String smsId;

  @Column(nullable = false)
  private String sender;

  @Column(nullable = false)
  private String recipient;

  @Column(nullable = false)
  private String text;

  @Column(nullable = false)
  private String status;

  @JsonIgnore @CreationTimestamp LocalDateTime createdAt;
  @JsonIgnore @UpdateTimestamp LocalDateTime updatedAt;
  @JsonIgnore @CreatedBy String createdBy;
  @JsonIgnore @LastModifiedBy String UpdatedBy;
  protected boolean isDeleted;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sms that = (Sms) o;
    return Objects.nonNull(id) ? id.equals(that.id) : id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
