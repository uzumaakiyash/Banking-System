package com.yash.bank.banking_transfer_system.Service;

import com.yash.bank.banking_transfer_system.dto.BeneficiaryRequest;
import com.yash.bank.banking_transfer_system.dto.BeneficiaryResponse;
import com.yash.bank.banking_transfer_system.dto.UpdateBeneficiaryRequest;
import com.yash.bank.banking_transfer_system.entity.Beneficiary;
import com.yash.bank.banking_transfer_system.entity.User;
import com.yash.bank.banking_transfer_system.exception.AccountNotFoundException;
import com.yash.bank.banking_transfer_system.exception.BeneficiaryAlreadyExistsException;
import com.yash.bank.banking_transfer_system.exception.BeneficiaryNotFoundException;
import com.yash.bank.banking_transfer_system.exception.UserNotFoundException;
import com.yash.bank.banking_transfer_system.repository.BeneficiaryRepository;
import com.yash.bank.banking_transfer_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;

    public BeneficiaryResponse addBeneficiary(BeneficiaryRequest request) {
        User currentUser = getCurrentUser();

        try {
            accountService.getAccountByNumber(request.getBeneficiaryAccountNumber());
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Beneficiary account number does not exist");
        }

        if (beneficiaryRepository.existsByUserAndBeneficiaryAccountNumber(currentUser, request.getBeneficiaryAccountNumber())) {
            throw new BeneficiaryAlreadyExistsException("Beneficiary already added");
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .user(currentUser)
                .beneficiaryAccountNumber(request.getBeneficiaryAccountNumber())
                .beneficiaryName(request.getBeneficiaryName())
                .nickname(request.getNickname())
                .active(true)
                .build();

        Beneficiary saved = beneficiaryRepository.save(beneficiary);
        return mapToResponse(saved);
    }

    public List<BeneficiaryResponse> getMyBeneficiaries() {
        User currentUser = getCurrentUser();
        return beneficiaryRepository.findByUser(currentUser)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BeneficiaryResponse updateBeneficiary(Long id, UpdateBeneficiaryRequest request) {
        User currentUser = getCurrentUser();
        Beneficiary beneficiary = beneficiaryRepository.findByUserAndId(currentUser, id)
                .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));

        beneficiary.setBeneficiaryName(request.getBeneficiaryName());
        beneficiary.setNickname(request.getNickname());

        Beneficiary saved = beneficiaryRepository.save(beneficiary);
        return mapToResponse(saved);
    }

    public void deleteBeneficiary(Long id) {
        User currentUser = getCurrentUser();
        Beneficiary beneficiary = beneficiaryRepository.findByUserAndId(currentUser, id)
                .orElseThrow(() -> new BeneficiaryNotFoundException("Beneficiary not found"));
        beneficiary.setActive(false);
        beneficiaryRepository.save(beneficiary);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private BeneficiaryResponse mapToResponse(Beneficiary b) {
        return BeneficiaryResponse.builder()
                .id(b.getId())
                .beneficiaryAccountNumber(b.getBeneficiaryAccountNumber())
                .beneficiaryName(b.getBeneficiaryName())
                .nickname(b.getNickname())
                .active(b.isActive())
                .build();
    }
}
