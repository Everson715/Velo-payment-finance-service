package com.velo.payment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {
    private UUID id;
    private UUID driverId;
    private String type; // e.g., CREDIT_CARD, PIX
    private String lastFourDigits;
    private String token;
}
