package com.yash.bank.banking_transfer_system.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String currency;
    private String accountType;
    private boolean active;
    private LocalDateTime createdAt;
}
