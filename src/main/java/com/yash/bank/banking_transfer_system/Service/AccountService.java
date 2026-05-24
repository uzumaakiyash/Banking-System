package com.yash.bank.banking_transfer_system.Service;

import com.yash.bank.banking_transfer_system.dto.AccountResponse;
import com.yash.bank.banking_transfer_system.dto.CreateAccountRequest;
import com.yash.bank.banking_transfer_system.entity.Account;
import com.yash.bank.banking_transfer_system.entity.User;
import com.yash.bank.banking_transfer_system.exception.AccountNotFoundException;
import com.yash.bank.banking_transfer_system.repository.AccountRepository;
import com.yash.bank.banking_transfer_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        User currentUser = getCurrentUser();
        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .user(currentUser)
                .balance(BigDecimal.ZERO)
                .currency(request.getCurrency())
                .accountType(request.getAccountType())
                .active(true)
                .build();

        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    public List<AccountResponse> getMyAccounts() {
        User currentUser = getCurrentUser();
        return accountRepository.findByUser(currentUser)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return mapToResponse(account);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("User not found"));
    }

    private String generateAccountNumber() {
        String number;
        do {
            number = String.format("%012d", random.nextLong() % 1_000_000_000_000L);
            if (number.startsWith("-")) number = number.substring(1);
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .accountType(account.getAccountType())
                .active(account.isActive())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
