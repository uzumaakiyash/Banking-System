package com.yash.bank.banking_transfer_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BeneficiaryRequest {
    @NotBlank
    private String beneficiaryAccountNumber;

    @NotBlank
    private String beneficiaryName;

    private String nickname;
}
