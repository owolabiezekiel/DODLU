package com.dodlu.core.persistence.banking.entity;


import com.dodlu.core.domain.banking.enums.BankUserType;
import com.dodlu.core.domain.banking.model.BankAccountDetailsModel;
import com.dodlu.core.persistence.base.LongBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.util.Objects;
import java.util.Optional;

@Accessors(chain = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bank_account_details")
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
public class BankAccountDetails extends LongBaseEntity {
  @Column(unique = true, nullable = false)
  private String ownerId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private BankUserType ownerType;

  @Column(nullable = false)
  private boolean isActive;

  @Column(nullable = false)
  private String parentAccountId;

  @Column(unique = true, nullable = false)
  private String customerId;

  @Column(unique = true, nullable = false)
  private String subAccountId;

  @Column(nullable = false)
  private String subAccountType;

  @Column(unique = true, nullable = false)
  private String virtualNubanId;

  @Column(nullable = false)
  private String pin;

  @Column(nullable = false)
  private String virtualBankName;

  @Column(nullable = false)
  private String virtualBankNipCode;

  @Column(nullable = false)
  private String virtualBankAccountName;

  @Column(nullable = false)
  private String virtualBankAccountNumber;

  @Column private String updatedVirtualNubanId;

  @Column private String updatedVirtualBankName;

  @Column private String updatedVirtualBankNipCode;

  @Column private String updatedVirtualBankAccountName;

  @Column private String updatedVirtualBankAccountNumber;

  @Column(nullable = false)
  private boolean accountUpdated;

  @Column(nullable = false)
  private String currency;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false)
  private boolean blocked;

  @Column(nullable = false)
  private long dailyLimit;

  private String bvn;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    BankAccountDetails that = (BankAccountDetails) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), id);
  }

//  public WalletDetails fromSubAccountResponse(
//      CreateSubAccountResponse subAccountDetails,
//      User user,
//      VirtualNubanDetailsResponse virtualNubanDetails) {
//    SubAccountResponseData subAccountData = subAccountDetails.getData();
//    VirtualNubanAttributes virtualNubanAttributes = virtualNubanDetails.getData().getAttributes();
//
//    return WalletDetails.builder()
//        .username(user.getUsername())
//        .isActive(true)
//        .parentAccountId(subAccountData.getRelationships().getParentAccount().getData().getId())
//        .customerId(subAccountData.getRelationships().getCustomer().getData().getId())
//        .subAccountId(subAccountData.getId())
//        .subAccountType(subAccountData.getAttributes().getAccountType())
//        .virtualNubanId(virtualNubanDetails.getData().getId())
//        .virtualBankName(virtualNubanAttributes.getBank().getName())
//        .virtualBankNipCode(virtualNubanAttributes.getBank().getNipCode())
//        .virtualBankAccountName(virtualNubanAttributes.getAccountName())
//        .virtualBankAccountNumber(virtualNubanAttributes.getAccountNumber())
//        .currency(virtualNubanAttributes.getCurrency())
//        .status(virtualNubanAttributes.getStatus())
//        .blocked(false)
//        .bvn(null)
//        .build();
//  }

  public BankAccountDetailsModel toModel() {
    return BankAccountDetailsModel.builder()
        .ownerId(ownerId)
        .isActive(isActive)
        .virtualBankName(
            Optional.ofNullable(getUpdatedVirtualBankName()).orElseGet(this::getVirtualBankName))
        .virtualBankAccountName(
            Optional.ofNullable(getUpdatedVirtualBankAccountName())
                .orElseGet(this::getVirtualBankAccountName))
        .virtualBankAccountNumber(
            Optional.ofNullable(getUpdatedVirtualBankAccountNumber())
                .orElseGet(this::getVirtualBankAccountNumber))
        .currency(getCurrency())
        .status(getStatus())
        .blocked(isBlocked())
        .accountUpdated(accountUpdated)
        .bvn(bvn)
        .dailyLimit(dailyLimit)
        .build();
  }
}
