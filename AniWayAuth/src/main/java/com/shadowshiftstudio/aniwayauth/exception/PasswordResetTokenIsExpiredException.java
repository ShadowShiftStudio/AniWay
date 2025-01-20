package com.shadowshiftstudio.aniwayauth.exception;

public class PasswordResetTokenIsExpiredException extends RuntimeException {
  public PasswordResetTokenIsExpiredException(String message) {
    super(message);
  }
}
