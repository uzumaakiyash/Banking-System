package com.yash.bank.banking_transfer_system.exception;

public class AccountInactiveException extends RuntimeException {
    public AccountInactiveException(String message) {
        super(message);
    }
}
