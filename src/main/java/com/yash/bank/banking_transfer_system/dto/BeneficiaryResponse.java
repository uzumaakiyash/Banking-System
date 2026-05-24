package com.yash.bank.banking_transfer_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeneficiaryResponse {
    private Long id;
    private String beneficiaryAccountNumber;
    private String beneficiaryName;
    private String nickname;
    private boolean active;
}
