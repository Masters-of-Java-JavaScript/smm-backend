package ru.smmassistant.smmbackend.dto;

import java.time.LocalDateTime;
import lombok.Value;

@Value
public class PublicationCreateDto {

    LocalDateTime createdDttm;

    Integer userId;

    String link;

    Integer ownerId;

    String text;
}
