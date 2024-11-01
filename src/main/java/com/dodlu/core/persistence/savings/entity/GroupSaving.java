package com.dodlu.core.persistence.savings.entity;

import static com.dodlu.core.app.utils.StringUtilities.emptyIfNull;

import com.dodlu.core.domain.savings.enums.Currency;
import com.dodlu.core.domain.savings.enums.SavingFrequency;
import com.dodlu.core.domain.savings.model.GroupSavingModel;
import com.dodlu.core.persistence.base.BaseEntity;
import com.dodlu.core.persistence.transaction.entity.GroupBalance;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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
@Table(name = "group_savings")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class GroupSaving extends BaseEntity {
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  private String url;

  @Column(nullable = false)
  private BigDecimal leastSavingAmount;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SavingFrequency savingFrequency;

  @Column(nullable = false)
  private String meetingLocation;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Currency currency;

  @ManyToOne
  @Fetch(FetchMode.JOIN)
  @JoinColumn(name = "admin_customer_profile_id", referencedColumnName = "id")
  private CustomerProfile admin;

  @ManyToMany(fetch = FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  @JoinTable(
      name = "group_saving_users",
      joinColumns = @JoinColumn(name = "group_saving_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "member_profile_id", referencedColumnName = "id"))
  private Set<CustomerProfile> members;

  @OneToOne(mappedBy = "groupSavings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @ToString.Exclude
  private GroupBalance balance;

  @OneToOne(mappedBy = "groupSavings", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @ToString.Exclude
  private GroupSetting setting;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    GroupSaving that = (GroupSaving) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public GroupSavingModel toModel() {
    return GroupSavingModel.builder()
        .id(id)
        .name(name)
        .description(description)
        .url(url)
        .leastSavingAmount(leastSavingAmount)
        .savingFrequency(savingFrequency.name())
        .meetingLocation(meetingLocation)
        .currency(currency.name())
        .admin(admin.toModel())
        .members(
            emptyIfNull(members).stream().map(CustomerProfile::toModel).collect(Collectors.toSet()))
        .balance(balance == null ? null : balance.toModel())
        .settings(setting == null ? null : setting.toModel())
        .build();
  }
}
