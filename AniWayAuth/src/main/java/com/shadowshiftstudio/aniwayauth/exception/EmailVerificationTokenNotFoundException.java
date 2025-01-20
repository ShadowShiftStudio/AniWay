package com.shadowshiftstudio.aniwayauth.exception;

public class EmailVerificationTokenNotFoundException extends RuntimeException {
    public EmailVerificationTokenNotFoundException(String message) {
        super(message);
    }
}
