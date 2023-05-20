package ru.smmassistant.smmbackend.service;

import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.model.PublicationResponse;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.parser.VkResponseParser;
import ru.smmassistant.smmbackend.repository.SocialNetworkRepository;
import ru.smmassistant.smmbackend.service.client.VkClient;
import ru.smmassistant.smmbackend.validation.group.VkService;

@RequiredArgsConstructor
@Service
@Validated(VkService.class)
@Transactional(readOnly = true)
public class VkPublicationService {

    private static final String PRIVATE_PUBLICATION_URL = "https://vk.com/id%d?w=wall%d_%d";
    private static final String PUBLIC_PUBLICATION_URL = "https://vk.com/public%d?w=wall%d_%d";

    @Value("${api.vk.version}")
    private final String apiVersion;
    private final VkClient vkClient;
    private final VkResponseParser vkResponseParser;
    private final SocialNetworkRepository socialNetworkRepository;

    public PublicationResponse publish(@Valid PublicationCreateDto publicationCreateDto) {
        String response = makePublish(publicationCreateDto);
        PublicationResponse publicationResponse = vkResponseParser.parse(response);

        String link = buildLink(publicationCreateDto.ownerId(), publicationResponse.getPostId());
        publicationResponse.setLink(link);

        return publicationResponse;
    }

    private String makePublish(PublicationCreateDto publicationCreateDto) {
        Map<String, Object> requestParams = new HashMap<>();

        SocialNetwork socialNetwork = socialNetworkRepository.findByUserId(publicationCreateDto.userId());
        requestParams.put("access_token", socialNetwork.getAccessToken());
        requestParams.put("owner_id", publicationCreateDto.ownerId());
        requestParams.put("message", publicationCreateDto.message());
        requestParams.put("attachments", publicationCreateDto.attachments());
        requestParams.put("publish_date", Optional.ofNullable(publicationCreateDto.publishDate())
            .map(OffsetDateTime::toEpochSecond));
        requestParams.put("post_id", publicationCreateDto.postId());
        requestParams.put("v", apiVersion);

        return vkClient.publish(requestParams);
    }

    private String buildLink(Integer ownerId, Long postId) {
        if (ownerId > 0) {
            return PRIVATE_PUBLICATION_URL.formatted(ownerId, ownerId, postId);
        } else {
            return PUBLIC_PUBLICATION_URL.formatted(Math.abs(ownerId), ownerId, postId);
        }
    }
}
