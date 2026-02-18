package com.ead.authuser.errors;

public class PasswordMismatchException extends RuntimeException {
  public PasswordMismatchException(String message) {
    super(message);
  }
}
