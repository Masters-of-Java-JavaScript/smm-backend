package ru.smmassistant.smmbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.smmassistant.smmbackend.service.SocialNetworkService;

@RequiredArgsConstructor
@RequestMapping("/register")
@RestController
public class SocialNetworkController {

    private final SocialNetworkService socialNetworkService;

    @PostMapping("/vk")
    @ResponseStatus(HttpStatus.OK)
    public void vkRegister(
        @RequestParam("user_id") Integer userId,
        @RequestParam("access_token") String accessToken,
        @RequestParam("account_id") Long accountId) {
        socialNetworkService.vkRegister(userId, accessToken, accountId);
    }
}
