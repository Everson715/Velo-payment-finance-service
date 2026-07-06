package com.velo.payment.service;

import com.velo.payment.dto.request.WithdrawRequest;
import com.velo.payment.dto.response.WalletBalanceResponse;
import com.velo.payment.exception.InsufficientBalanceException;
import com.velo.payment.model.Wallet;
import com.velo.payment.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
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

    @Transactional
    public void withdraw(UUID driverId, WithdrawRequest request) {
        Wallet wallet = walletRepository.findByDriverIdWithPessimisticLock(driverId)
                .orElseThrow(() -> new InsufficientBalanceException("Wallet not found"));

        if (wallet.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Saldo Insuficiente");
        }

        wallet.setAvailableBalance(wallet.getAvailableBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);
    }
}
