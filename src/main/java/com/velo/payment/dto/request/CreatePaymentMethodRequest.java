package com.velo.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePaymentMethodRequest {
    
    @NotBlank(message = "Gateway token is required")
    private String gatewayToken;

    @NotBlank(message = "Last four digits are required")
    @Size(min = 4, max = 4, message = "Last four digits must be exactly 4 characters")
    private String lastFour;
}
