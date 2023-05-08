package ru.smmassistant.smmbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Builder;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankAttachments;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankMessage;
import ru.smmassistant.smmbackend.validation.group.VkService;

@Builder
@NotBlankMessage(groups = VkService.class)
@NotBlankAttachments(groups = VkService.class)
public record PublicationCreateDto(

    @NotNull(message = "Параметр userId является обязательным")
    Integer userId,

    @NotBlank(message = "Параметр accessToken является обязательным", groups = VkService.class)
    String accessToken,

    @NotNull(message = "Параметр ownerId является обязательным", groups = VkService.class)
    Integer ownerId,

    String message,

    String attachments,

    OffsetDateTime publishDate,

    Integer postId,

    @NotEmpty(message = "Параметр socialNetworks должен содержать хотя бы одно значение")
    Set<SocialNetwork> socialNetworks) {
}
