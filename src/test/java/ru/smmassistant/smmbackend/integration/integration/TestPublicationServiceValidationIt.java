package ru.smmassistant.smmbackend.integration.integration;

import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.service.VkPublicationService;

@RequiredArgsConstructor
class TestPublicationServiceValidationIt extends IntegrationTestBase {

    private static final Integer USER_ID = 1;
    private static final Integer PRIVATE_OWNER_ID = 1234;
    private static final OffsetDateTime PUBLISH_DATE = OffsetDateTime.of(2023, 5, 13, 12, 32, 12, 123, ZoneOffset.UTC);
    private static final String MESSAGE = "Dummy message";
    private static final String ATTACHMENTS = "Dummy attachments";
    private static final String ACCESS_TOKEN = "Dummy accessToken";

    private final VkPublicationService vkPublicationService;

    @Test
    void publish_shouldThrowConstraintViolationExceptionBecauseUserIdIsNull() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(null)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PRIVATE_OWNER_ID)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .build();

        Assertions.assertThatThrownBy(() -> vkPublicationService.publish(publicationCreateDto))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("Параметр user_id является обязательным");
    }

    @Test
    void publish_shouldThrowConstraintViolationExceptionBecauseAccessTokenIsNull() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(null)
            .ownerId(PRIVATE_OWNER_ID)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .build();

        Assertions.assertThatThrownBy(() -> vkPublicationService.publish(publicationCreateDto))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("Параметр access_token является обязательным");
    }

    @Test
    void publish_shouldThrowConstraintViolationExceptionBecauseOwnerIdIsNull() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(null)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .build();

        Assertions.assertThatThrownBy(() -> vkPublicationService.publish(publicationCreateDto))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("Параметр owner_id является обязательным");
    }

    @Test
    void publish_shouldThrowConstraintViolationExceptionBecauseMessageAndAttachmentsIsNull() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PRIVATE_OWNER_ID)
            .message(null)
            .attachments(null)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .build();

        Assertions.assertThatThrownBy(() -> vkPublicationService.publish(publicationCreateDto))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("Параметр message является обязательным, если не задан параметр attachments")
            .hasMessageContaining("Параметр attachments является обязательным, если не задан параметр message");
    }
}