package com.velo.payment.infrastructure.controller;

import com.velo.payment.dto.request.CreatePaymentMethodRequest;
import com.velo.payment.dto.response.PaymentMethodResponse;
import com.velo.payment.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments/methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    private UUID getUserId() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        return UUID.fromString(userIdStr);
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> getMethods() {
        return ResponseEntity.ok(paymentMethodService.getPaymentMethods(getUserId()));
    }

    @PostMapping
    public ResponseEntity<PaymentMethodResponse> addMethod(@Valid @RequestBody CreatePaymentMethodRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentMethodService.addPaymentMethod(getUserId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMethod(@PathVariable UUID id) {
        paymentMethodService.deletePaymentMethod(getUserId(), id);
        return ResponseEntity.noContent().build();
    }
}
