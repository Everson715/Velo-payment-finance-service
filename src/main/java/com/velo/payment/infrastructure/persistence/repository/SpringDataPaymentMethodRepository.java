package com.velo.payment.infrastructure.persistence.repository;

import com.velo.payment.infrastructure.persistence.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataPaymentMethodEntityRepository extends JpaRepository<PaymentMethodEntity, UUID> {
    List<PaymentMethodEntity> findByUserId(UUID userId);
    Optional<PaymentMethodEntity> findByIdAndUserId(UUID id, UUID userId);
}
