package ru.smmassistant.smmbackend.dto;

import java.time.OffsetDateTime;

public record PublicationReadDto(

    OffsetDateTime publishDate,

    String message,

    String attachments,

    String link) {
}
