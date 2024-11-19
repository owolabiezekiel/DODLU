package com.dodlu.core.persistence.marketplace.entity;

import com.dodlu.core.domain.marketplace.enums.MarketItemCategory;
import com.dodlu.core.domain.marketplace.model.MarketPostModel;
import com.dodlu.core.persistence.user.entity.CustomerProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "market_posts")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@DynamicUpdate
public class MarketPost {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String photoUrl;

  @Column(nullable = false)
  private BigDecimal price;

  private String description;

  @Column(nullable = false)
  private String availableUnit;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private MarketItemCategory category;

  @ManyToOne
  @JoinColumn(name = "marketer_id", referencedColumnName = "id", nullable = false)
  private CustomerProfile marketer;

  @JsonIgnore @CreationTimestamp private LocalDateTime createdAt;
  @JsonIgnore @UpdateTimestamp private LocalDateTime updatedAt;
  @JsonIgnore @CreatedBy private String createdBy;
  @JsonIgnore @LastModifiedBy private String UpdatedBy;

  @Column(nullable = false)
  private boolean isDeleted;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    MarketPost that = (MarketPost) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public MarketPostModel toModel() {
    return MarketPostModel.builder()
        .id(id)
        .title(title)
        .photoUrl(photoUrl)
        .price(price)
        .description(description)
        .availableUnit(availableUnit)
        .category(category)
        .marketer(marketer.toModel())
        .build();
  }
}
