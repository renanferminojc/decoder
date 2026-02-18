package com.ead.authuser.validators.annotations;

import com.ead.authuser.validators.impl.NoWhitespaceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoWhitespaceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhitespace {
    String message() default "{field.no_whitespace}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}