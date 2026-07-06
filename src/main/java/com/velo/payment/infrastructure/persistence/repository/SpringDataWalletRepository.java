package com.velo.payment.infrastructure.persistence.repository;

import com.velo.payment.infrastructure.persistence.entity.WalletEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataWalletEntityRepository extends JpaRepository<WalletEntity, UUID> {
    
    Optional<WalletEntity> findByDriverId(UUID driverId);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WalletEntity w WHERE w.driverId = :driverId")
    Optional<WalletEntity> findByDriverIdWithPessimisticLock(@Param("driverId") UUID driverId);
}
