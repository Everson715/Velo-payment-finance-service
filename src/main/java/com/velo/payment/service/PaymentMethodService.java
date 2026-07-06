package com.velo.payment.service;

import com.velo.payment.dto.request.CreatePaymentMethodRequest;
import com.velo.payment.dto.response.PaymentMethodResponse;
import com.velo.payment.exception.ResourceNotFoundException;
import com.velo.payment.model.PaymentMethod;
import com.velo.payment.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public PaymentMethodResponse addPaymentMethod(UUID userId, CreatePaymentMethodRequest request) {
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .userId(userId)
                .gatewayToken(request.getGatewayToken())
                .lastFour(request.getLastFour())
                .build();
        
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        
        return PaymentMethodResponse.builder()
                .id(saved.getId())
                .lastFour(saved.getLastFour())
                .build();
    }

    @Transactional(readOnly = true)
    public List<PaymentMethodResponse> getPaymentMethods(UUID userId) {
        return paymentMethodRepository.findByUserId(userId).stream()
                .map(pm -> PaymentMethodResponse.builder()
                        .id(pm.getId())
                        .lastFour(pm.getLastFour())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePaymentMethod(UUID userId, UUID paymentMethodId) {
        PaymentMethod pm = paymentMethodRepository.findByIdAndUserId(paymentMethodId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));
        
        paymentMethodRepository.delete(pm);
    }
}
