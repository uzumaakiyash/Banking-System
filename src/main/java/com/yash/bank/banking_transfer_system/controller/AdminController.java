package com.yash.bank.banking_transfer_system.controller;

import com.yash.bank.banking_transfer_system.dto.ApiResponse;
import com.yash.bank.banking_transfer_system.dto.TransactionResponse;
import com.yash.bank.banking_transfer_system.entity.Transaction;
import com.yash.bank.banking_transfer_system.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final TransactionRepository transactionRepository;

    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getAllTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        Page<TransactionResponse> response = transactions.map(this::mapToResponse);
        return ResponseEntity.ok(ApiResponse.<Page<TransactionResponse>>builder()
                .result("Success")
                .data(response)
                .build());
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return TransactionResponse.builder()
                .transactionReference(t.getTransactionReference())
                .fromAccountNumber(t.getFromAccount().getAccountNumber())
                .toAccountNumber(t.getToAccount().getAccountNumber())
                .amount(t.getAmount())
                .description(t.getDescription())
                .status(t.getStatus().name())
                .transactionDate(t.getTransactionDate())
                .build();
    }
}
