package ru.smmassistant.smmbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import lombok.Builder;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankAttachments;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankMessage;

@Builder
@NotBlankMessage
@NotBlankAttachments
public record PublicationCreateDto(

    @NotNull(message = "Параметр user_id является обязательным")
    Integer userId,

    @NotBlank(message = "Параметр access_token является обязательным")
    String accessToken,

    @NotNull(message = "Параметр owner_id является обязательным")
    Integer ownerId,

    String message,

    String attachments,

    OffsetDateTime publishDate,

    Integer postId) {
}
