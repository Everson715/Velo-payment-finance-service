package com.velo.payment.infrastructure.persistence.adapter;

import com.velo.payment.domain.model.Wallet;
import com.velo.payment.domain.repository.WalletRepository;
import com.velo.payment.infrastructure.persistence.entity.WalletEntity;
import com.velo.payment.infrastructure.persistence.mapper.WalletEntityMapper;
import com.velo.payment.infrastructure.persistence.repository.SpringDataWalletRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class WalletRepositoryAdapter implements WalletRepository {

    private final SpringDataWalletRepository jpaRepository;

    public WalletRepositoryAdapter(SpringDataWalletRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Wallet> findByDriverId(UUID driverId) {
        return jpaRepository.findByDriverId(driverId)
                .map(WalletEntityMapper::toDomain);
    }

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity entity = WalletEntityMapper.toEntity(wallet);
        WalletEntity savedEntity = jpaRepository.save(entity);
        return WalletEntityMapper.toDomain(savedEntity);
    }
}
