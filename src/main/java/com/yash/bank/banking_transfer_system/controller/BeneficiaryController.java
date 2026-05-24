package com.yash.bank.banking_transfer_system.controller;

import com.yash.bank.banking_transfer_system.Service.BeneficiaryService;
import com.yash.bank.banking_transfer_system.dto.ApiResponse;
import com.yash.bank.banking_transfer_system.dto.BeneficiaryRequest;
import com.yash.bank.banking_transfer_system.dto.BeneficiaryResponse;
import com.yash.bank.banking_transfer_system.dto.UpdateBeneficiaryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> addBeneficiary(@Valid @RequestBody BeneficiaryRequest request) {
        BeneficiaryResponse response = beneficiaryService.addBeneficiary(request);
        return ResponseEntity.ok(ApiResponse.<BeneficiaryResponse>builder()
                .result("Success")
                .message("Beneficiary added")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BeneficiaryResponse>>> getMyBeneficiaries() {
        List<BeneficiaryResponse> beneficiaries = beneficiaryService.getMyBeneficiaries();
        return ResponseEntity.ok(ApiResponse.<List<BeneficiaryResponse>>builder()
                .result("Success")
                .data(beneficiaries)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> updateBeneficiary(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBeneficiaryRequest request) {
        BeneficiaryResponse response = beneficiaryService.updateBeneficiary(id, request);
        return ResponseEntity.ok(ApiResponse.<BeneficiaryResponse>builder()
                .result("Success")
                .message("Beneficiary updated")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .result("Success")
                .message("Beneficiary removed")
                .build());
    }
}
