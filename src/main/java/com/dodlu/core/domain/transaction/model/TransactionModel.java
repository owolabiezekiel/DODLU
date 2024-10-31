package com.dodlu.core.domain.transaction.model;

import com.dodlu.core.domain.savings.enums.Currency;
import com.dodlu.core.domain.transaction.enums.EntityType;
import com.dodlu.core.domain.transaction.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TransactionModel(
    String id,
    String senderId,
    String senderName,
    EntityType senderType,
    String receiverId,
    String receiverName,
    EntityType receiverType,
    Currency currency,
    BigDecimal amount,
    String reference,
    String description,
    String status,
    String failureReason,
    TransactionType type,
    LocalDateTime transactionDateTime) {}
