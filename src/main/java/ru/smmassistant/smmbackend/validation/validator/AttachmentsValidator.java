package ru.smmassistant.smmbackend.validation.validator;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankAttachments;

@Component
public class AttachmentsValidator implements ConstraintValidator<NotBlankAttachments, PublicationCreateDto> {

    @Override
    public boolean isValid(PublicationCreateDto publicationCreateDto, ConstraintValidatorContext context) {
        if (StringUtils.isNotBlank(publicationCreateDto.message())) {
            return true;
        }

        return StringUtils.isNotBlank(publicationCreateDto.attachments());
    }
}
