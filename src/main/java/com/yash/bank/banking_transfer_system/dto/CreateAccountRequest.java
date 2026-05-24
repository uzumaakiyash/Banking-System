package com.yash.bank.banking_transfer_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAccountRequest {
    @NotBlank
    private String accountType;

    @NotBlank
    private String currency;
}
