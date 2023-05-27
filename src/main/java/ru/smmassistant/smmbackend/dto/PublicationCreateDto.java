package ru.smmassistant.smmbackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import ru.smmassistant.smmbackend.model.SocialNetworkName;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankAttachments;
import ru.smmassistant.smmbackend.validation.annotation.NotBlankMessage;
import ru.smmassistant.smmbackend.validation.group.VkService;

@Builder
@NotBlankMessage(groups = VkService.class)
@NotBlankAttachments(groups = VkService.class)
public record PublicationCreateDto(

    @NotNull(message = "Параметр userId является обязательным")
    Integer userId,

    @NotNull(message = "Параметр ownerId является обязательным", groups = VkService.class)
    Long accountId,

    String message,

    String attachments,

    Optional<OffsetDateTime> publishDate,

    Optional<Long> postId,

    @NotEmpty(message = "Параметр socialNetworks должен содержать хотя бы одно значение")
    Set<SocialNetworkName> socialNetworks) {
}
