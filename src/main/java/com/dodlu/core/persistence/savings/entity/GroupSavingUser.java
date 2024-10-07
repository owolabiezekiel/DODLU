package com.dodlu.core.persistence.savings.entity;

import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_saving_users")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class GroupSavingUser extends BaseEntity {
  @ManyToOne
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "group_saving_id", referencedColumnName = "id")
  private GroupSaving group;

  @ManyToOne
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "member_profile_id", referencedColumnName = "id")
  private CustomerProfile member;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    GroupSavingUser that = (GroupSavingUser) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }
}
