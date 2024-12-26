package com.dodlu.core.persistence.user.entity;

import com.dodlu.core.persistence.base.LongBaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customer_verifications")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@DynamicUpdate
public class CustomerVerification extends LongBaseEntity {
  @OneToOne(optional = false)
  @JoinColumn(name = "customer_id", nullable = false, unique = true)
  @ToString.Exclude
  private CustomerProfile customer;

  private Boolean isBVNVerified;
  private Boolean isNINVerified;
  private Boolean isDriverLicenseVerified;
  private Boolean isPassportVerified;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    CustomerVerification that = (CustomerVerification) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
