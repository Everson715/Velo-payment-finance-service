package com.velo.payment.service;

import com.velo.payment.dto.request.AuthorizeRequest;
import com.velo.payment.dto.request.CaptureRequest;
import com.velo.payment.dto.request.RefundRequest;
import com.velo.payment.dto.response.TransactionResponse;
import com.velo.payment.exception.BusinessException;
import com.velo.payment.exception.ResourceNotFoundException;
import com.velo.payment.model.Transaction;
import com.velo.payment.model.Wallet;
import com.velo.payment.model.enums.TransactionStatus;
import com.velo.payment.model.enums.TransactionType;
import com.velo.payment.repository.TransactionRepository;
import com.velo.payment.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    private static final BigDecimal PLATFORM_FEE_PERCENTAGE = new BigDecimal("0.20"); // 20%

    @Transactional
    public TransactionResponse authorize(AuthorizeRequest request) {
        if (transactionRepository.findByTripId(request.getTripId()).isPresent()) {
            throw new BusinessException("Transaction already exists for this trip");
        }

        Transaction transaction = Transaction.builder()
                .tripId(request.getTripId())
                .driverId(request.getDriverId())
                .paymentMethodId(request.getPaymentMethodId())
                .amount(request.getAmount())
                .status(TransactionStatus.AUTHORIZED)
                .type(TransactionType.CHARGE)
                .build();

        transaction = transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponse capture(CaptureRequest request, String idempotencyKey) {
        if (idempotencyKey != null) {
            Optional<Transaction> existing = transactionRepository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                return mapToResponse(existing.get()); // Idempotency hit
            }
        }

        Transaction transaction = transactionRepository.findByTripId(request.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Authorized transaction not found for this trip"));

        if (transaction.getStatus() != TransactionStatus.AUTHORIZED) {
            throw new BusinessException("Transaction is not in AUTHORIZED state");
        }

        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionStatus.CAPTURED);
        if (idempotencyKey != null) {
            transaction.setIdempotencyKey(idempotencyKey);
        }

        // Calculate payout to driver
        BigDecimal fee = request.getAmount().multiply(PLATFORM_FEE_PERCENTAGE);
        BigDecimal netPayout = request.getAmount().subtract(fee);

        // Update driver wallet with lock
        Wallet wallet = walletRepository.findByDriverIdWithPessimisticLock(transaction.getDriverId())
                .orElseGet(() -> Wallet.builder()
                        .driverId(transaction.getDriverId())
                        .availableBalance(BigDecimal.ZERO)
                        .build());
        
        wallet.setAvailableBalance(wallet.getAvailableBalance().add(netPayout));
        walletRepository.save(wallet);

        transaction = transactionRepository.save(transaction);
        return mapToResponse(transaction);
    }

    @Transactional
    public TransactionResponse refund(RefundRequest request) {
        Transaction transaction = transactionRepository.findByTripId(request.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.CAPTURED && transaction.getStatus() != TransactionStatus.AUTHORIZED) {
            throw new BusinessException("Cannot refund a transaction in state: " + transaction.getStatus());
        }

        if (transaction.getStatus() == TransactionStatus.CAPTURED) {
            BigDecimal fee = transaction.getAmount().multiply(PLATFORM_FEE_PERCENTAGE);
            BigDecimal netPayout = transaction.getAmount().subtract(fee);

            Wallet wallet = walletRepository.findByDriverIdWithPessimisticLock(transaction.getDriverId())
                    .orElseThrow(() -> new BusinessException("Driver wallet not found"));

            // Deduct the refunded amount from driver's wallet
            wallet.setAvailableBalance(wallet.getAvailableBalance().subtract(netPayout));
            walletRepository.save(wallet);
        }

        transaction.setStatus(TransactionStatus.REFUNDED);
        transaction.setType(TransactionType.REFUND);
        // Assuming full refund for simplicity, partial refund could adjust the amount
        transaction = transactionRepository.save(transaction);
        return mapToResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getHistory(UUID driverId) {
        return transactionRepository.findByDriverId(driverId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionResponse getReceipt(UUID tripId) {
        Transaction transaction = transactionRepository.findByTripId(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found for trip"));
        return mapToResponse(transaction);
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .tripId(transaction.getTripId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .type(transaction.getType())
                .build();
    }
}
