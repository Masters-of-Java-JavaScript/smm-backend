package ru.smmassistant.smmbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankAttachments;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankMessage;

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

    Long publishDate,

    Integer postId) {
}
