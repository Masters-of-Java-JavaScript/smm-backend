package ru.smmassistant.smmbackend.service;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.smmassistant.smmbackend.config.validation.ValidationConfiguration;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.dto.PublicationReadDto;
import ru.smmassistant.smmbackend.mapper.PublicationCreateMapper;
import ru.smmassistant.smmbackend.mapper.PublicationReadMapper;
import ru.smmassistant.smmbackend.model.Publication;
import ru.smmassistant.smmbackend.repository.PublicationRepository;
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
    private static final String RESPONSE = "{response={post_id=347}}";
    private static final String ACCESS_TOKEN = "Dummy accessToken";
    private static final String PRIVATE_PUBLICATION_URL = "https://vk.com/id%d?w=wall%d_%d";
    private static final String PUBLIC_PUBLICATION_URL = "https://vk.com/public%d?w=wall%d_%d";

    @Mock
    private PublicationRepository publicationRepository;

    @Spy
    private PublicationReadMapper publicationReadMapper;

    @Spy
    private PublicationCreateMapper publicationCreateMapper;

    @Mock
    private VkClient vkClient;

    @Spy
    private Validator validator = new ValidationConfiguration().validator();

    @InjectMocks
    private VkPublicationService vkPublicationService;

    @Test
    void findAllByUserId_shouldFindAllPublicationsByUserId() {
        List<Publication> publications = new ArrayList<>();
        Publication privatePublication = Publication.builder()
            .id(1L)
            .userId(USER_ID)
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .link(PRIVATE_PUBLICATION_URL.formatted(PRIVATE_OWNER_ID, PRIVATE_OWNER_ID, POST_ID))
            .response(RESPONSE)
            .build();
        publications.add(privatePublication);
        Publication publicPublication = Publication.builder()
            .id(2L)
            .userId(USER_ID)
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .link(PUBLIC_PUBLICATION_URL.formatted(abs(PUBLIC_OWNER_ID), PUBLIC_OWNER_ID, POST_ID))
            .response(RESPONSE)
            .build();
        publications.add(publicPublication);

        List<PublicationReadDto> expectedResult = new ArrayList<>();
        PublicationReadDto privatePublicationReadDto = PublicationReadDto.builder()
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .link(PRIVATE_PUBLICATION_URL.formatted(PRIVATE_OWNER_ID, PRIVATE_OWNER_ID, POST_ID))
            .build();
        expectedResult.add(privatePublicationReadDto);
        PublicationReadDto publicPublicationReadDto = PublicationReadDto.builder()
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .link(PUBLIC_PUBLICATION_URL.formatted(abs(PUBLIC_OWNER_ID), PUBLIC_OWNER_ID, POST_ID))
            .build();
        expectedResult.add(publicPublicationReadDto);

        doReturn(publications).when(publicationRepository).findAllByUserId(USER_ID);
        List<PublicationReadDto> actualResult = vkPublicationService.findAllByUserId(USER_ID);

        assertFalse(actualResult.isEmpty());
        assertThat(actualResult).hasSize(2);
        assertEquals(expectedResult, actualResult);

        verify(publicationRepository).findAllByUserId(any(Integer.class));
        verify(publicationReadMapper, times(2)).map(any(Publication.class));
        verifyNoMoreInteractions(publicationRepository, publicationReadMapper);
    }

    @Test
    void publish_forPublicOwnerIdShouldValidatePublicationCreateDtoAndPublishToVkAndSaveToDb() {
        LinkedHashMap<String, Integer> postId = new LinkedHashMap<>();
        postId.put("post_id", POST_ID);
        LinkedHashMap<String, LinkedHashMap<String, Integer>> response = new LinkedHashMap<>();
        response.put("response", postId);
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PUBLIC_OWNER_ID)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .build();

        PublicationReadDto expectedResult = PublicationReadDto.builder()
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .link(PUBLIC_PUBLICATION_URL.formatted(abs(PUBLIC_OWNER_ID), PUBLIC_OWNER_ID, POST_ID))
            .build();

        doReturn(response).when(vkClient).publish(anyMap());
        PublicationReadDto actualResult = vkPublicationService.publish(publicationCreateDto);

        assertThat(actualResult).isNotNull();
        assertEquals(expectedResult, actualResult);

        verify(vkClient).publish(any(Map.class));
        verify(publicationCreateMapper).map(any(PublicationCreateDto.class));
        verify(publicationRepository).save(any(Publication.class));
        verify(publicationReadMapper).map(any(Publication.class));
        verifyNoMoreInteractions(validator, vkClient, publicationCreateMapper, publicationRepository, publicationReadMapper);
    }

    @Test
    void publish_forPrivateOwnerIdShouldValidatePublicationCreateDtoAndPublishToVkAndSaveToDb() {
        LinkedHashMap<String, Integer> postId = new LinkedHashMap<>();
        postId.put("post_id", POST_ID);
        LinkedHashMap<String, LinkedHashMap<String, Integer>> response = new LinkedHashMap<>();
        response.put("response", postId);
        PublicationCreateDto publicationCreateDto = PublicationCreateDto.builder()
            .userId(USER_ID)
            .accessToken(ACCESS_TOKEN)
            .ownerId(PRIVATE_OWNER_ID)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .publishDate(PUBLISH_DATE)
            .postId(null)
            .build();

        PublicationReadDto expectedResult = PublicationReadDto.builder()
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .link(PRIVATE_PUBLICATION_URL.formatted(PRIVATE_OWNER_ID, PRIVATE_OWNER_ID, POST_ID))
            .build();

        doReturn(response).when(vkClient).publish(anyMap());
        PublicationReadDto actualResult = vkPublicationService.publish(publicationCreateDto);

        assertThat(actualResult).isNotNull();
        assertEquals(expectedResult, actualResult);

        verify(vkClient).publish(any(Map.class));
        verify(publicationCreateMapper).map(any(PublicationCreateDto.class));
        verify(publicationRepository).save(any(Publication.class));
        verify(publicationReadMapper).map(any(Publication.class));
        verifyNoMoreInteractions(validator, vkClient, publicationCreateMapper, publicationRepository, publicationReadMapper);
    }

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

        assertThrows(ConstraintViolationException.class, () -> vkPublicationService.publish(publicationCreateDto));
        verifyNoMoreInteractions(validator, vkClient, publicationCreateMapper, publicationRepository, publicationReadMapper);
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

        assertThrows(ConstraintViolationException.class, () -> vkPublicationService.publish(publicationCreateDto));
        verifyNoMoreInteractions(validator, vkClient, publicationCreateMapper, publicationRepository, publicationReadMapper);
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

        assertThrows(ConstraintViolationException.class, () -> vkPublicationService.publish(publicationCreateDto));
        verifyNoMoreInteractions(validator, vkClient, publicationCreateMapper, publicationRepository, publicationReadMapper);
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

        assertThrows(ConstraintViolationException.class, () -> vkPublicationService.publish(publicationCreateDto));
        verifyNoMoreInteractions(validator, vkClient, publicationCreateMapper, publicationRepository, publicationReadMapper);
    }
}
