package com.velo.payment.domain.repository;

import com.velo.payment.domain.model.PaymentMethod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentMethodRepository {
    List<PaymentMethod> findByDriverId(UUID driverId);
    Optional<PaymentMethod> findById(UUID id);
    PaymentMethod save(PaymentMethod paymentMethod);
    void deleteById(UUID id);
}
