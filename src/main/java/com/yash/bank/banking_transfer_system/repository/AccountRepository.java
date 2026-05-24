package com.yash.bank.banking_transfer_system.repository;

import com.yash.bank.banking_transfer_system.entity.Account;
import com.yash.bank.banking_transfer_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUser(User user);

    boolean existsByAccountNumber(String accountNumber);
}
