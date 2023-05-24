package ru.smmassistant.smmbackend.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.UserAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.model.SocialNetworkName;
import ru.smmassistant.smmbackend.repository.SocialNetworkRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SocialNetworkService {

    private final SocialNetworkRepository socialNetworkRepository;
    private final VkApiClient vkApiClient;

    @Value("${api.vk.client-id}")
    private final Integer clientId;

    @Value("${api.vk.client-secret}")
    private final String clientSecret;

    @Transactional
    public void vkRegister(Integer userId, String code, String redirectUri) {

        try {
            UserAuthResponse response = vkApiClient.oAuth()
                .userAuthorizationCodeFlow(clientId, clientSecret, redirectUri, code)
                .execute();

            SocialNetwork socialNetwork = SocialNetwork.builder()
                .name(SocialNetworkName.VK)
                .userId(userId)
                .accessToken(response.getAccessToken())
                .accountId(response.getUserId().longValue())
                .build();

            socialNetworkRepository.save(socialNetwork);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
    }
}
