package com.yash.bank.banking_transfer_system.exception;

public class BeneficiaryAlreadyExistsException extends RuntimeException {
    public BeneficiaryAlreadyExistsException(String message) {
        super(message);
    }
}
