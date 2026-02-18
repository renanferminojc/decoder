package com.ead.authuser.validators.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotBlank(message = "{field.required}")
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredField {
  @OverridesAttribute(constraint = NotBlank.class, name = "message")
  String message() default "{field.required}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
