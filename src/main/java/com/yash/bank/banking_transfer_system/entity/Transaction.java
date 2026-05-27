package com.yash.bank.banking_transfer_system.entity;

import com.yash.bank.banking_transfer_system.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
        if (transactionReference == null) {
            transactionReference = "TXN" + System.currentTimeMillis() + id;
        }
    }
}
