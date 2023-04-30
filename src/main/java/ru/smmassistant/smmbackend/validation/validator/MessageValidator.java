package ru.smmassistant.smmbackend.validation.validator;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankMessage;

@Component
public class MessageValidator implements ConstraintValidator<NotBlankMessage, PublicationCreateDto> {

    @Override
    public boolean isValid(PublicationCreateDto publicationCreateDto, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(publicationCreateDto.attachments())) {
            return true;
        }

        return StringUtils.isNotBlank(publicationCreateDto.message());
    }
}
