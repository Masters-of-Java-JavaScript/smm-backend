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

    @NotNull(message = "Параметр user_id является обязательным")
    Integer userId,

    @NotBlank(message = "Параметр access_token является обязательным", groups = VkService.class)
    String accessToken,

    @NotNull(message = "Параметр owner_id является обязательным", groups = VkService.class)
    Integer ownerId,

    String message,

    String attachments,

    OffsetDateTime publishDate,

    Integer postId,

    @NotEmpty(message = "Параметр network_publish_set должен содержать хотя бы одно значение")
    Set<SocialNetwork> socialNetworks) {
}
