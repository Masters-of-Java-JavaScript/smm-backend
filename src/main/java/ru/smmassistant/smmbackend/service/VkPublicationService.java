package ru.smmassistant.smmbackend.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.model.PublicationResponse;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.repository.SocialNetworkRepository;
import ru.smmassistant.smmbackend.validation.group.VkService;

@RequiredArgsConstructor
@Service
@Validated(VkService.class)
@Transactional(readOnly = true)
public class VkPublicationService {

    private static final String PRIVATE_PUBLICATION_URL = "https://vk.com/id%d?w=wall%d_%d";
    private static final String PUBLIC_PUBLICATION_URL = "https://vk.com/public%d?w=wall%d_%d";

    private final VkApiClient vkApiClient;
    private final SocialNetworkRepository socialNetworkRepository;

    public PublicationResponse publish(@Valid PublicationCreateDto publicationCreateDto) {
        PostResponse response = makePublish(publicationCreateDto);

        return PublicationResponse.builder()
            .postId(response.getPostId().longValue())
            .link(buildLink(publicationCreateDto.ownerId(), response.getPostId().longValue()))
            .build();
    }

    private PostResponse makePublish(PublicationCreateDto publicationCreateDto) {
        SocialNetwork socialNetwork = socialNetworkRepository.findByUserId(publicationCreateDto.userId());
        UserActor actor = new UserActor(socialNetwork.getUserId(), socialNetwork.getAccessToken());

        try {
            return vkApiClient.wall()
                .post(actor)
                .ownerId(publicationCreateDto.ownerId())
                .message(publicationCreateDto.message())
                .attachments(publicationCreateDto.attachments())
                .publishDate(Optional.ofNullable(publicationCreateDto.publishDate())
                    .map(OffsetDateTime::toEpochSecond)
                    .map(Math::toIntExact)
                    .orElse(0))
                .postId(Optional.ofNullable(publicationCreateDto.postId())
                    .map(Math::toIntExact)
                    .orElse(0))
                .execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildLink(Integer ownerId, Long postId) {
        if (ownerId > 0) {
            return PRIVATE_PUBLICATION_URL.formatted(ownerId, ownerId, postId);
        } else {
            return PUBLIC_PUBLICATION_URL.formatted(Math.abs(ownerId), ownerId, postId);
        }
    }
}
