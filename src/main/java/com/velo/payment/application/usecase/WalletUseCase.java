package com.velo.payment.application.usecase;

import com.velo.payment.application.dto.request.WithdrawRequest;
import com.velo.payment.application.dto.response.WalletBalanceResponse;

import java.util.UUID;

public interface WalletUseCase {
    WalletBalanceResponse getBalance(UUID driverId);
    void withdraw(UUID driverId, WithdrawRequest request);
}
