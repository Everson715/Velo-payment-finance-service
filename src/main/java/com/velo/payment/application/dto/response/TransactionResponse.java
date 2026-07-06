package com.velo.payment.application.dto.response;

import com.velo.payment.model.enums.TransactionStatus;
import com.velo.payment.model.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransactionResponse {
    private UUID id;
    private UUID tripId;
    private BigDecimal amount;
    private TransactionStatus status;
    private TransactionType type;
}
