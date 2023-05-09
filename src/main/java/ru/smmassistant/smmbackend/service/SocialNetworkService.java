package ru.smmassistant.smmbackend.service;

import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void vkRegister(Long accountId, String accessToken, Integer userId) {

        SocialNetwork vkSocialNetwork = SocialNetwork.builder()
            .name(SocialNetworkName.VK)
            .accountId(accountId)
            .accessToken(accessToken)
            .userId(userId)
            .build();

        socialNetworkRepository.save(vkSocialNetwork);
    }
}
