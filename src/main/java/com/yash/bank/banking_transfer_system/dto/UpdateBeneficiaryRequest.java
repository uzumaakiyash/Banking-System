package com.yash.bank.banking_transfer_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateBeneficiaryRequest {
    @NotBlank
    private String beneficiaryName;

    private String nickname;
}
