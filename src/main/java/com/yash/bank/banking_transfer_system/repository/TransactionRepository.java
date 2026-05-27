package com.yash.bank.banking_transfer_system.repository;

import com.yash.bank.banking_transfer_system.entity.Account;
import com.yash.bank.banking_transfer_system.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.user.id = :userId OR t.toAccount.user.id = :userId")
    Page<Transaction> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
