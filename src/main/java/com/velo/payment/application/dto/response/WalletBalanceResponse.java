package com.velo.payment.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class WalletBalanceResponse {
    private UUID driverId;
    private BigDecimal availableBalance;
}
