package ru.smmassistant.smmbackend.validation.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import ru.smmassistant.smmbackend.validation.validator.AttachmentsValidator;

@Constraint(validatedBy = AttachmentsValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface NotBlankAttachments {

    String message() default "Параметр attachments является обязательным, если не задан параметр message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
