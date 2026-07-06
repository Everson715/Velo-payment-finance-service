package com.velo.payment.domain.repository;

import com.velo.payment.domain.model.Transaction;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    Optional<Transaction> findById(UUID id);
    Transaction save(Transaction transaction);
}
