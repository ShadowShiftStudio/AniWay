package com.shadowshiftstudio.aniwayauth.exception;

public class EmailIsOccupiedException extends RuntimeException {
    public EmailIsOccupiedException(String message) {
        super(message);
    }
}
