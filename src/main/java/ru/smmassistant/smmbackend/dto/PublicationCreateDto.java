package ru.smmassistant.smmbackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import ru.smmassistant.smmbackend.model.SocialNetworkName;

@Builder
public record PublicationCreateDto(

    @NotNull(message = "Параметр userId является обязательным")
    Integer userId,

    String message,

    String attachments,

    Optional<OffsetDateTime> publishDate,

    @NotEmpty(message = "Параметр socialNetworks должен содержать хотя бы одно значение")
    Set<SocialNetworkName> socialNetworks) {
}
