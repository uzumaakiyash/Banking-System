package com.yash.bank.banking_transfer_system.controller;

import com.yash.bank.banking_transfer_system.Service.TransferService;
import com.yash.bank.banking_transfer_system.dto.ApiResponse;
import com.yash.bank.banking_transfer_system.dto.TransactionResponse;
import com.yash.bank.banking_transfer_system.dto.TransferRequest;
import com.yash.bank.banking_transfer_system.dto.TransferResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<ApiResponse<TransferResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.transfer(request);
        return ResponseEntity.ok(ApiResponse.<TransferResponse>builder()
                .result("Success")
                .message("Transfer completed successfully")
                .data(response)
                .build());
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Page<TransactionResponse>>> getHistory(Pageable pageable) {
        Page<TransactionResponse> history = transferService.getTransactionHistory(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<TransactionResponse>>builder()
                .result("Success")
                .data(history)
                .build());
    }
}
