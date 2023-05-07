package ru.smmassistant.smmbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.dto.PublicationReadDto;
import ru.smmassistant.smmbackend.mapper.PublicationCreateMapper;
import ru.smmassistant.smmbackend.mapper.PublicationReadMapper;
import ru.smmassistant.smmbackend.model.Publication;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.repository.PublicationRepository;

@ExtendWith(MockitoExtension.class)
class PublicationServiceTest {

    private static final Integer USER_ID = 1;
    private static final Integer PRIVATE_OWNER_ID = 1234;
    private static final Integer PUBLIC_OWNER_ID = -1234;
    private static final Integer POST_ID = 347;
    private static final OffsetDateTime PUBLISH_DATE = OffsetDateTime.of(2023, 5, 13, 12, 32, 12, 123, ZoneOffset.UTC);
    private static final String MESSAGE = "Dummy message";
    private static final String ATTACHMENTS = "Dummy attachments";
    private static final String ACCESS_TOKEN = "Dummy accessToken";
    private static final Set<SocialNetwork> NETWORK_PUBLISH_SET = Set.of(SocialNetwork.VK, SocialNetwork.FACEBOOK);

    @Mock
    private PublicationRepository publicationRepository;

    @Spy
    private PublicationReadMapper publicationReadMapper;

    @Spy
    private PublicationCreateMapper publicationCreateMapper;

    @Mock
    private VkPublicationService vkPublicationService;

    @InjectMocks
    private PublicationService publicationService;

    @Test
    void findAllByUserId_shouldFindAllPublicationsByUserId() {
        List<Publication> publications = new ArrayList<>();
        Publication privatePublication = Publication.builder()
            .id(1L)
            .userId(USER_ID)
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .build();
        publications.add(privatePublication);
        Publication publicPublication = Publication.builder()
            .id(2L)
            .userId(USER_ID)
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .build();
        publications.add(publicPublication);

        List<PublicationReadDto> expectedResult = new ArrayList<>();
        PublicationReadDto privatePublicationReadDto = PublicationReadDto.builder()
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .build();
        expectedResult.add(privatePublicationReadDto);
        PublicationReadDto publicPublicationReadDto = PublicationReadDto.builder()
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .build();
        expectedResult.add(publicPublicationReadDto);

        doReturn(publications).when(publicationRepository).findAllByUserId(USER_ID);
        List<PublicationReadDto> actualResult = publicationService.findAllByUserId(USER_ID);

        assertFalse(actualResult.isEmpty());
        assertThat(actualResult).hasSize(2);
        assertEquals(expectedResult, actualResult);

        verify(publicationRepository).findAllByUserId(any(Integer.class));
        verify(publicationReadMapper, times(2)).map(any(Publication.class));
        verifyNoMoreInteractions(publicationRepository, publicationReadMapper, publicationCreateMapper, vkPublicationService);
    }

    @Test
    void publish_forPublicOwnerIdShouldPublishToSocialNetworksAndSaveToDb() {
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

        PublicationReadDto expectedResult = PublicationReadDto.builder()
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .build();

        PublicationReadDto actualResult = publicationService.publish(publicationCreateDto);

        assertThat(actualResult).isNotNull();
        assertEquals(expectedResult, actualResult);

        verify(publicationCreateMapper).map(any(PublicationCreateDto.class));
        verify(publicationRepository).save(any(Publication.class));
        verify(publicationReadMapper).map(any(Publication.class));
        verify(vkPublicationService).publish(any(PublicationCreateDto.class));
        verifyNoMoreInteractions(publicationRepository, publicationReadMapper, publicationCreateMapper, vkPublicationService);
    }

    @Test
    void publish_forPrivateOwnerIdShouldPublishToSocialNetworksAndSaveToDb() {
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

        PublicationReadDto expectedResult = PublicationReadDto.builder()
            .publishDate(PUBLISH_DATE)
            .message(MESSAGE)
            .attachments(ATTACHMENTS)
            .build();

        PublicationReadDto actualResult = publicationService.publish(publicationCreateDto);

        assertThat(actualResult).isNotNull();
        assertEquals(expectedResult, actualResult);

        verify(publicationCreateMapper).map(any(PublicationCreateDto.class));
        verify(publicationRepository).save(any(Publication.class));
        verify(publicationReadMapper).map(any(Publication.class));
        verify(vkPublicationService).publish(any(PublicationCreateDto.class));
        verifyNoMoreInteractions(publicationRepository, publicationReadMapper, publicationCreateMapper, vkPublicationService);
    }

    @Test
    void publish_forPrivateOwnerIdShouldConfirmDelayedSendToSocialNetworksAndSaveToDb() {
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

        PublicationReadDto expectedResult = PublicationReadDto.builder()
            .publishDate(OffsetDateTime.now())
            .message(null)
            .attachments(null)
            .build();

        PublicationReadDto actualResult = publicationService.publish(publicationCreateDto);

        assertThat(actualResult).isNotNull();
        assertEquals(expectedResult.attachments(), actualResult.attachments());
        assertEquals(expectedResult.message(), actualResult.message());
        assertThat(actualResult.publishDate()).isNotNull();
        assertTrue(actualResult.publishDate().isAfter(expectedResult.publishDate()));

        verify(publicationCreateMapper).map(any(PublicationCreateDto.class));
        verify(publicationRepository).save(any(Publication.class));
        verify(publicationReadMapper).map(any(Publication.class));
        verify(vkPublicationService).publish(any(PublicationCreateDto.class));
        verifyNoMoreInteractions(publicationRepository, publicationReadMapper, publicationCreateMapper, vkPublicationService);
    }
}
