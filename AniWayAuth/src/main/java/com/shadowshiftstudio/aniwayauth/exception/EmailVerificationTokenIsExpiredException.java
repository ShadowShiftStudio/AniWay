package com.shadowshiftstudio.aniwayauth.exception;

public class EmailVerificationTokenIsExpiredException extends RuntimeException {
    public EmailVerificationTokenIsExpiredException(String message) {
        super(message);
    }
}
