package com.velo.payment.infrastructure.persistence.repository;

import com.velo.payment.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTransactionEntityRepository extends JpaRepository<TransactionEntity, UUID> {
    Optional<TransactionEntity> findByTripId(UUID tripId);
    List<TransactionEntity> findByDriverId(UUID driverId);
    Optional<TransactionEntity> findByIdempotencyKey(String idempotencyKey);
}
