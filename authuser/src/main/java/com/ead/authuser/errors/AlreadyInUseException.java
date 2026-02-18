package com.ead.authuser.errors;

public class AlreadyInUseException extends RuntimeException {
  public AlreadyInUseException(String message) {
    super(message);
  }
}
