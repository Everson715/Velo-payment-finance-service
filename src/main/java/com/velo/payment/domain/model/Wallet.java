package com.velo.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    private UUID id;
    private UUID driverId;
    private BigDecimal availableBalance;

    public void addBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.availableBalance = this.availableBalance.add(amount);
    }

    public void subtractBalance(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (this.availableBalance.compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        this.availableBalance = this.availableBalance.subtract(amount);
    }
}
