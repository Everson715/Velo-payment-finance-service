package com.velo.payment.infrastructure.controller;

import com.velo.payment.dto.request.WithdrawRequest;
import com.velo.payment.dto.response.WalletBalanceResponse;
import com.velo.payment.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    private UUID getDriverId() {
        String driverIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        return UUID.fromString(driverIdStr);
    }

    @GetMapping("/balance")
    public ResponseEntity<WalletBalanceResponse> getBalance() {
        return ResponseEntity.ok(walletService.getBalance(getDriverId()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody WithdrawRequest request) {
        walletService.withdraw(getDriverId(), request);
        return ResponseEntity.noContent().build();
    }
}
