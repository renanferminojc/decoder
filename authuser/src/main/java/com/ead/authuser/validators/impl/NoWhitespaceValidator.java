package com.ead.authuser.validators.impl;

import com.ead.authuser.validators.annotations.NoWhitespace;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoWhitespaceValidator implements ConstraintValidator<NoWhitespace, String> {
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value == null || !value.matches(".*\\s+.*");
  }
}
