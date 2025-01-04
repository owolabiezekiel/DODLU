package com.dodlu.core.persistence.kyc.entity;

import com.dodlu.core.domain.kyc.model.BvnData;
import com.dodlu.core.domain.kyc.model.YouVerifyBvnResponse;
import com.dodlu.core.persistence.base.LongBaseEntity;
import com.dodlu.core.persistence.user.entity.User;
import jakarta.persistence.*;
import java.util.Objects;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bvn_details")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@DynamicUpdate
public class BvnDetails extends LongBaseEntity {
  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Column(unique = true, nullable = false)
  private String bvn;

  private String parentId;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false)
  private boolean dataValidation;

  @Column(nullable = false)
  private boolean selfieValidation;

  @Column(nullable = false)
  private String firstName;

  private String middleName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String image;

  private String enrollmentBranch;
  private String enrollmentInstitution;

  @Column(nullable = false, unique = true)
  private String mobile;

  @Column(nullable = false)
  private String dateOfBirth;

  @Column(nullable = false)
  private boolean isConsent;

  @Column(nullable = false)
  private String idNumber;

  @Column(nullable = false, unique = true)
  private String nin;

  @Column(nullable = false)
  private String businessId;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String gender;

  @Column(nullable = false)
  private String requestedById;

  @Column(nullable = false)
  private String country;

  @Column(nullable = false)
  private String createdOn;

  @Column(nullable = false)
  private String lastModifiedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    BvnDetails that = (BvnDetails) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

  public static BvnDetails fromValidationResponse(YouVerifyBvnResponse response, User user) {
    BvnData data = response.data();
    return BvnDetails.builder()
        .user(user)
        .bvn(data.idNumber())
        .parentId(data.parentId())
        .status(data.status())
        .dataValidation(data.dataValidation())
        .selfieValidation(data.selfieValidation())
        .firstName(data.firstName())
        .middleName(data.middleName())
        .lastName(data.lastName())
        .image(data.image())
        .enrollmentBranch(data.enrollmentBranch())
        .enrollmentInstitution(data.enrollmentInstitution())
        .mobile(data.mobile())
        .dateOfBirth(data.dateOfBirth())
        .isConsent(data.isConsent())
        .idNumber(data.idNumber())
        .nin(data.nin())
        .businessId(data.businessId())
        .type(data.type())
        .gender(data.gender())
        .requestedById(data.requestedById())
        .country(data.country())
        .createdOn(data.createdAt())
        .lastModifiedAt(data.lastModifiedAt())
        .build();
  }
}
