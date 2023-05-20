package ru.smmassistant.smmbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.model.SocialNetworkName;
import ru.smmassistant.smmbackend.parser.SocialNetworkResponseParser;
import ru.smmassistant.smmbackend.repository.SocialNetworkRepository;
import ru.smmassistant.smmbackend.service.client.VkClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SocialNetworkService {

    private final SocialNetworkRepository socialNetworkRepository;
    private final VkClient vkClient;
    private final SocialNetworkResponseParser socialNetworkResponseParser;

    @Value("${api.vk.client-id}")
    private final Integer clientId;

    @Value("${api.vk.client-secret}")
    private final String clientSecret;

    @Transactional
    public void vkRegister(Integer userId, String code, String redirectUri) {
        SocialNetwork socialNetwork = SocialNetwork.builder()
            .name(SocialNetworkName.VK)
            .userId(userId)
            .build();

        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("client_id", clientId);
        requestParams.put("client_secret", clientSecret);
        requestParams.put("code", code);
        requestParams.put("redirect_uri", redirectUri);

        String response = vkClient.getAccessToken(requestParams);
        socialNetworkResponseParser.parseTo(socialNetwork, response);

        socialNetworkRepository.save(socialNetwork);
    }
}
