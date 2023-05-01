package ru.smmassistant.smmbackend.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.dto.PublicationReadDto;
import ru.smmassistant.smmbackend.mapper.PublicationCreateMapper;
import ru.smmassistant.smmbackend.mapper.PublicationReadMapper;
import ru.smmassistant.smmbackend.model.Publication;
import ru.smmassistant.smmbackend.repository.PublicationRepository;
import ru.smmassistant.smmbackend.service.client.VkClient;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PublicationService {

    private static final String PRIVATE_PUBLICATION_URL = "https://vk.com/id%d?w=wall%d_%d";
    private static final String PUBLIC_PUBLICATION_URL = "https://vk.com/public%d?w=wall%d_%d";

    @Value("${api.vk.version}")
    private final String apiVersion;
    private final PublicationRepository publicationRepository;
    private final PublicationReadMapper publicationReadMapper;
    private final PublicationCreateMapper publicationCreateMapper;
    private final Validator validator;
    private final VkClient vkClient;

    public List<PublicationReadDto> findAllByUserId(Integer userId) {
        return publicationRepository.findAllByUserId(userId)
            .stream()
            .map(publicationReadMapper::map)
            .toList();
    }

    @Transactional
    public PublicationReadDto publish(PublicationCreateDto publicationCreateDto) {
        validate(publicationCreateDto);

        Object response = makePublish(publicationCreateDto);

        Publication publication = publicationCreateMapper.map(publicationCreateDto);
        Integer postId = ((LinkedHashMap<String, LinkedHashMap<String, Integer>>) response)
            .get("response")
            .get("post_id");
        if (publicationCreateDto.ownerId() > 0) {
            publication.setLink(PRIVATE_PUBLICATION_URL.formatted(
                publicationCreateDto.ownerId(),
                publicationCreateDto.ownerId(),
                postId));
        } else {
            publication.setLink(PUBLIC_PUBLICATION_URL.formatted(
                Math.abs(publicationCreateDto.ownerId()),
                publicationCreateDto.ownerId(),
                postId));
        }
        publication.setResponse(response.toString());
        publicationRepository.save(publication);

        return publicationReadMapper.map(publication);
    }

    private void validate(@Valid PublicationCreateDto publicationCreateDto) {
        Set<ConstraintViolation<PublicationCreateDto>> validationResult = validator.validate(publicationCreateDto);
        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }
    }

    private Object makePublish(@Valid PublicationCreateDto publicationCreateDto) {
        Map<String, Object> requestParams = new HashMap<>();

        requestParams.put("access_token", publicationCreateDto.accessToken());
        requestParams.put("owner_id", publicationCreateDto.ownerId());
        requestParams.put("message", publicationCreateDto.message());
        requestParams.put("attachments", publicationCreateDto.attachments());
        requestParams.put("publish_date", publicationCreateDto.publishDate().toEpochSecond());
        requestParams.put("post_id", publicationCreateDto.postId());
        requestParams.put("v", apiVersion);

        return vkClient.publish(requestParams);
    }
}
