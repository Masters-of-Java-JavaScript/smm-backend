package ru.smmassistant.smmbackend.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.smmassistant.smmbackend.model.Publication;
import ru.smmassistant.smmbackend.model.PublicationInfo;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.model.SocialNetworkName;
import ru.smmassistant.smmbackend.repository.PublicationInfoRepository;
import ru.smmassistant.smmbackend.repository.SocialNetworkRepository;
import ru.smmassistant.smmbackend.validation.group.VkService;

@RequiredArgsConstructor
@Service
@Validated(VkService.class)
@Transactional(readOnly = true)
public class VkPublicationService {

    private final VkApiClient vkApiClient;
    private final SocialNetworkRepository socialNetworkRepository;
    private final PublicationInfoRepository publicationInfoRepository;

    public void publish(@Valid Publication publication) {
        PublicationInfo publicationInfo = makePublish(publication);
        publicationInfoRepository.save(publicationInfo);
    }

    private PublicationInfo makePublish(Publication publication) {
        SocialNetwork socialNetwork = socialNetworkRepository.findByUserId(publication.getUserId());
        UserActor actor = new UserActor(socialNetwork.getUserId(), socialNetwork.getAccessToken());

        WallPostQuery query = vkApiClient.wall().post(actor);
        query.ownerId(socialNetwork.getAccountId().intValue());
        query.message(publication.getMessage());
        query.attachments(publication.getAttachments());
        query.publishDate(Math.toIntExact(publication.getPublishDate().toEpochSecond()));

        try {
            PostResponse response = query.execute();

            return PublicationInfo.builder()
                .socialNetworkName(SocialNetworkName.VK)
                .link(buildLink(socialNetwork.getAccountId(), response.getPostId().longValue()))
                .publication(publication)
                .build();
        } catch (ApiException | ClientException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildLink(Long accountId, Long postId) {
        return String.format("https://vk.com/id%d?w=wall%d_%d", accountId, accountId, postId);
    }
}
