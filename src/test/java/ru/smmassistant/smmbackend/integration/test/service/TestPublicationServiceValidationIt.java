package ru.smmassistant.smmbackend.integration.test.service;

import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.integration.IntegrationTestBase;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.service.PublicationService;

@RequiredArgsConstructor
class TestPublicationServiceValidationIt extends IntegrationTestBase {

    private static final Integer USER_ID = 1;
    private static final Integer PRIVATE_OWNER_ID = 1234;
    private static final OffsetDateTime PUBLISH_DATE = OffsetDateTime.of(2023, 5, 13, 12, 32, 12, 123, ZoneOffset.UTC);
    private static final String MESSAGE = "Dummy message";
    private static final String ATTACHMENTS = "Dummy attachments";
    private static final String ACCESS_TOKEN = "Dummy accessToken";
    private static final Set<SocialNetwork> NETWORK_PUBLISH_SET = Set.of(SocialNetwork.VK, SocialNetwork.FACEBOOK);

    private final PublicationService publicationService;

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
            .networkPublishSet(NETWORK_PUBLISH_SET)
            .build();

        Assertions.assertThatThrownBy(() -> publicationService.publish(publicationCreateDto))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("Параметр user_id является обязательным");
    }

    @Test
    void publish_shouldThrowConstraintViolationExceptionBecauseNetworkPublishSetIsNull() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PRIVATE_OWNER_ID)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .networkPublishSet(null)
            .build();

        Assertions.assertThatThrownBy(() -> publicationService.publish(publicationCreateDto))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("Параметр network_publish_set должен содержать хотя бы одно значение");
    }
}
