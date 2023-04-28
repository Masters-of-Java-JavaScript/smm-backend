package ru.smmassistant.smmbackend.dto;

import java.time.LocalDateTime;
import lombok.Value;

@Value
public class PublicationReadDto {

    LocalDateTime createdDttm;

    String text;

    String link;

    Integer ownerId;
}
