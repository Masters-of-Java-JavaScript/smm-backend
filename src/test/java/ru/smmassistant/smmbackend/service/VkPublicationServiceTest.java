package ru.smmassistant.smmbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.model.PublicationResponse;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.parser.VkParser;
import ru.smmassistant.smmbackend.service.client.VkClient;

@ExtendWith(MockitoExtension.class)
class VkPublicationServiceTest {

    private static final Integer USER_ID = 1;
    private static final Integer PRIVATE_OWNER_ID = 1234;
    private static final Integer PUBLIC_OWNER_ID = -1234;
    private static final Integer POST_ID = 347;
    private static final OffsetDateTime PUBLISH_DATE = OffsetDateTime.of(2023, 5, 13, 12, 32, 12, 123, ZoneOffset.UTC);
    private static final String MESSAGE = "Dummy message";
    private static final String ATTACHMENTS = "Dummy attachments";
    private static final String ACCESS_TOKEN = "Dummy accessToken";
    private static final Set<SocialNetwork> NETWORK_PUBLISH_SET = Set.of(SocialNetwork.VK, SocialNetwork.FACEBOOK);
    private static final String RESPONSE = "{\"response\":{\"post_id\":%d}}".formatted(POST_ID);
    private static final String PUBLIC_PUBLICATION_URL = "https://vk.com/public%d?w=wall%d_%d"
        .formatted(Math.abs(PUBLIC_OWNER_ID), PUBLIC_OWNER_ID, POST_ID);
    private static final String PRIVATE_PUBLICATION_URL = "https://vk.com/id%d?w=wall%d_%d"
        .formatted(PRIVATE_OWNER_ID, PRIVATE_OWNER_ID, POST_ID);

    @Spy
    private VkParser vkParser;

    @Mock
    private VkClient vkClient;

    @InjectMocks
    private VkPublicationService vkPublicationService;

    @Test
    void publish_forPublicOwnerIdShouldPublishToVk() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PUBLIC_OWNER_ID)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .networkPublishSet(NETWORK_PUBLISH_SET)
            .build();

        PublicationResponse expectedResult = PublicationResponse.builder()
            .postId(POST_ID)
            .link(PUBLIC_PUBLICATION_URL)
            .build();

        doReturn(RESPONSE).when(vkClient).publish(anyMap());
        PublicationResponse actualResult = vkPublicationService.publish(publicationCreateDto);

        assertThat(actualResult).isNotNull();
        assertEquals(expectedResult, actualResult);

        verify(vkClient).publish(anyMap());
        verify(vkParser).parse(RESPONSE);
        verifyNoMoreInteractions(vkClient, vkParser);
    }

    @Test
    void publish_forPrivateOwnerIdShouldPublishToVk() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PRIVATE_OWNER_ID)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .networkPublishSet(NETWORK_PUBLISH_SET)
            .build();

        PublicationResponse expectedResult = PublicationResponse.builder()
            .postId(POST_ID)
            .link(PRIVATE_PUBLICATION_URL)
            .build();

        doReturn(RESPONSE).when(vkClient).publish(anyMap());
        PublicationResponse actualResult = vkPublicationService.publish(publicationCreateDto);

        assertThat(actualResult).isNotNull();
        assertEquals(expectedResult, actualResult);

        verify(vkClient).publish(anyMap());
        verify(vkParser).parse(RESPONSE);
        verifyNoMoreInteractions(vkClient, vkParser);
    }

    @Test
    void publish_forPrivateOwnerIdShouldConfirmDelayedSendToVk() {
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PRIVATE_OWNER_ID)
            .message(null)
            .attachments(null)
            .publishDate(null)
            .postId(POST_ID)
            .networkPublishSet(NETWORK_PUBLISH_SET)
            .build();

        PublicationResponse expectedResult = PublicationResponse.builder()
            .postId(POST_ID)
            .link(PRIVATE_PUBLICATION_URL)
            .build();

        doReturn(RESPONSE).when(vkClient).publish(anyMap());
        PublicationResponse actualResult = vkPublicationService.publish(publicationCreateDto);

        assertThat(actualResult).isNotNull();
        assertEquals(expectedResult, actualResult);

        verify(vkClient).publish(anyMap());
        verify(vkParser).parse(RESPONSE);
        verifyNoMoreInteractions(vkClient, vkParser);
    }
}
