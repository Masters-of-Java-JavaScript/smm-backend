package ru.smmassistant.smmbackend.integration.test.validation;

import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.integration.IntegrationTestBase;
import ru.smmassistant.smmbackend.model.SocialNetworkName;
import ru.smmassistant.smmbackend.service.PublicationService;

@RequiredArgsConstructor
class TestPublicationServiceValidationIt extends IntegrationTestBase {

    private static final Integer USER_ID = 1;
    private static final Integer PRIVATE_OWNER_ID = 1234;
    private static final OffsetDateTime PUBLISH_DATE = OffsetDateTime.of(2023, 5, 13, 12, 32, 12, 123, ZoneOffset.UTC);
    private static final String MESSAGE = "Dummy message";
    private static final String ATTACHMENTS = "Dummy attachments";
    private static final String ACCESS_TOKEN = "Dummy accessToken";
    private static final Set<SocialNetworkName> SOCIAL_NETWORK_NAMES = Set.of(SocialNetworkName.VK, SocialNetworkName.FACEBOOK);

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
            .socialNetworks(SOCIAL_NETWORK_NAMES)
            .build();

        Assertions.assertThatThrownBy(() -> publicationService.publish(publicationCreateDto))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("Параметр userId является обязательным");
    }

    @Test
    void publish_shouldThrowConstraintViolationExceptionBecausesocialNetworksIsNull() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PRIVATE_OWNER_ID)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .socialNetworks(null)
            .build();

        Assertions.assertThatThrownBy(() -> publicationService.publish(publicationCreateDto))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("Параметр socialNetworks должен содержать хотя бы одно значение");
    }
}
