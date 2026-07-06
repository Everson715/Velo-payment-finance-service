package com.velo.payment.domain.model;

import com.velo.payment.domain.model.enums.TransactionStatus;
import com.velo.payment.domain.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private UUID id;
    private UUID tripId;
    private UUID driverId;
    private UUID paymentMethodId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private String description;
    private String idempotencyKey;
    
    public void complete() {
        this.status = TransactionStatus.COMPLETED;
    }
    
    public void fail() {
        this.status = TransactionStatus.FAILED;
    }
}
