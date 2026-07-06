package com.velo.payment.infrastructure.persistence.mapper;

import com.velo.payment.domain.model.Wallet;
import com.velo.payment.infrastructure.persistence.entity.WalletEntity;

public class WalletEntityMapper {

    public static Wallet toDomain(WalletEntity entity) {
        if (entity == null) return null;
        return Wallet.builder()
                .id(entity.getId())
                .driverId(entity.getDriverId())
                .availableBalance(entity.getAvailableBalance())
                .build();
    }

    public static WalletEntity toEntity(Wallet domain) {
        if (domain == null) return null;
        return WalletEntity.builder()
                .id(domain.getId())
                .driverId(domain.getDriverId())
                .availableBalance(domain.getAvailableBalance())
                .build();
    }
}
