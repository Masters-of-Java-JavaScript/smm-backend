package ru.smmassistant.smmbackend.dto;

import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record PublicationReadDto(

    OffsetDateTime publishDate,

    String message,

    String attachments,

    String link) {
}
