package com.velo.payment.application.usecase.impl;

import com.velo.payment.application.dto.request.WithdrawRequest;
import com.velo.payment.application.dto.response.WalletBalanceResponse;
import com.velo.payment.application.usecase.WalletUseCase;
import com.velo.payment.domain.exception.InsufficientBalanceException;
import com.velo.payment.domain.model.Wallet;
import com.velo.payment.domain.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletUseCaseImpl implements WalletUseCase {

    private final WalletRepository walletRepository;

    public WalletUseCaseImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletBalanceResponse getBalance(UUID driverId) {
        Wallet wallet = walletRepository.findByDriverId(driverId)
                .orElseGet(() -> Wallet.builder()
                        .driverId(driverId)
                        .availableBalance(BigDecimal.ZERO)
                        .build());

        return WalletBalanceResponse.builder()
                .driverId(wallet.getDriverId())
                .availableBalance(wallet.getAvailableBalance())
                .build();
    }

    @Override
    public void withdraw(UUID driverId, WithdrawRequest request) {
        Wallet wallet = walletRepository.findByDriverId(driverId)
                .orElseThrow(() -> new InsufficientBalanceException("Wallet not found"));

        wallet.subtractBalance(request.getAmount());
        walletRepository.save(wallet);
    }
}
