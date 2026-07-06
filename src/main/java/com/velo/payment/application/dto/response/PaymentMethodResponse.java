package com.velo.payment.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentMethodResponse {
    private UUID id;
    private String lastFour;
}
