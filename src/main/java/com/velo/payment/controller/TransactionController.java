package com.velo.payment.controller;

import com.velo.payment.dto.request.AuthorizeRequest;
import com.velo.payment.dto.request.CaptureRequest;
import com.velo.payment.dto.request.RefundRequest;
import com.velo.payment.dto.response.TransactionResponse;
import com.velo.payment.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private UUID getUserId() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        return UUID.fromString(userIdStr);
    }

    @PostMapping("/authorize")
    public ResponseEntity<TransactionResponse> authorize(@Valid @RequestBody AuthorizeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.authorize(request));
    }

    @PostMapping("/capture")
    public ResponseEntity<TransactionResponse> capture(
            @Valid @RequestBody CaptureRequest request,
            @RequestHeader(value = "X-Idempotency-Key", required = false) String idempotencyKey) {
        return ResponseEntity.ok(transactionService.capture(request, idempotencyKey));
    }

    @PostMapping("/refund")
    public ResponseEntity<TransactionResponse> refund(@Valid @RequestBody RefundRequest request) {
        return ResponseEntity.ok(transactionService.refund(request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponse>> getHistory() {
        return ResponseEntity.ok(transactionService.getHistory(getUserId()));
    }

    @GetMapping("/receipt/{tripId}")
    public ResponseEntity<TransactionResponse> getReceipt(@PathVariable UUID tripId) {
        return ResponseEntity.ok(transactionService.getReceipt(tripId));
    }
}
