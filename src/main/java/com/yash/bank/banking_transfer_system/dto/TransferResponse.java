package com.yash.bank.banking_transfer_system.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponse {
    private String transactionReference;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String status;
    private LocalDateTime transactionDate;
}
