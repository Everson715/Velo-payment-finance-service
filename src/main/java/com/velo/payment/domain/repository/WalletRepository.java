package com.velo.payment.domain.repository;

import com.velo.payment.domain.model.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Optional<Wallet> findByDriverId(UUID driverId);
    Wallet save(Wallet wallet);
}
