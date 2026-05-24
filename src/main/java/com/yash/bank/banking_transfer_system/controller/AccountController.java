package com.yash.bank.banking_transfer_system.controller;

import com.yash.bank.banking_transfer_system.Service.AccountService;
import com.yash.bank.banking_transfer_system.dto.AccountResponse;
import com.yash.bank.banking_transfer_system.dto.ApiResponse;
import com.yash.bank.banking_transfer_system.dto.CreateAccountRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.ok(ApiResponse.<AccountResponse>builder()
                .result("Success")
                .message("Account created successfully")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getMyAccounts() {
        List<AccountResponse> accounts = accountService.getMyAccounts();
        return ResponseEntity.ok(ApiResponse.<List<AccountResponse>>builder()
                .result("Success")
                .message("Accounts retrieved")
                .data(accounts)
                .build());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable String accountNumber) {
        AccountResponse account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(ApiResponse.<AccountResponse>builder()
                .result("Success")
                .data(account)
                .build());
    }
}
