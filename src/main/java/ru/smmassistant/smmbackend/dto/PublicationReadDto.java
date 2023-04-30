package ru.smmassistant.smmbackend.dto;

import java.time.LocalDateTime;

public record PublicationReadDto(

    LocalDateTime publishDate,

    String message,

    String attachments,

    String link) {
}
