package com.yash.bank.banking_transfer_system.repository;

import com.yash.bank.banking_transfer_system.entity.Beneficiary;
import com.yash.bank.banking_transfer_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByUser(User user);

    Optional<Beneficiary> findByUserAndId(User user, Long id);

    boolean existsByUserAndBeneficiaryAccountNumber(User user, String accountNumber);
}
