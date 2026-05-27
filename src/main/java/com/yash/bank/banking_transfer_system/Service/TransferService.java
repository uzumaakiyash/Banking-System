package com.yash.bank.banking_transfer_system.Service;

import com.yash.bank.banking_transfer_system.dto.TransactionResponse;
import com.yash.bank.banking_transfer_system.dto.TransferRequest;
import com.yash.bank.banking_transfer_system.dto.TransferResponse;
import com.yash.bank.banking_transfer_system.entity.Account;
import com.yash.bank.banking_transfer_system.entity.Transaction;
import com.yash.bank.banking_transfer_system.entity.User;
import com.yash.bank.banking_transfer_system.enums.TransactionStatus;
import com.yash.bank.banking_transfer_system.exception.*;
import com.yash.bank.banking_transfer_system.repository.AccountRepository;
import com.yash.bank.banking_transfer_system.repository.TransactionRepository;
import com.yash.bank.banking_transfer_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));

        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!fromAccount.getUser().getEmail().equals(currentUserEmail)) {
            throw new TransferException("You can only transfer from your own accounts");
        }

        if (!fromAccount.isActive() || !toAccount.isActive()) {
            throw new AccountInactiveException("One or both accounts are inactive");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in source account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(TransactionStatus.SUCCESS)
                .build();
        transaction.setTransactionReference(generateReference());
        Transaction saved = transactionRepository.save(transaction);

        return TransferResponse.builder()
                .transactionReference(saved.getTransactionReference())
                .fromAccount(fromAccount.getAccountNumber())
                .toAccount(toAccount.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .transactionDate(saved.getTransactionDate())
                .build();
    }

    public Page<TransactionResponse> getTransactionHistory(Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Page<Transaction> transactions = transactionRepository.findAllByUserId(user.getId(), pageable);
        return transactions.map(this::mapToResponse);
    }

    private String generateReference() {
        return "TXN" + System.currentTimeMillis() + (int) (Math.random() * 1000);
    }

    private TransactionResponse mapToResponse(Transaction t) {
        return TransactionResponse.builder()
                .transactionReference(t.getTransactionReference())
                .fromAccountNumber(t.getFromAccount().getAccountNumber())
                .toAccountNumber(t.getToAccount().getAccountNumber())
                .amount(t.getAmount())
                .description(t.getDescription())
                .status(t.getStatus().name())
                .transactionDate(t.getTransactionDate())
                .build();
    }
}
