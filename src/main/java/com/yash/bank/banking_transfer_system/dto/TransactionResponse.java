package com.yash.bank.banking_transfer_system.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private String transactionReference;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private String description;
    private String status;
    private LocalDateTime transactionDate;
}
