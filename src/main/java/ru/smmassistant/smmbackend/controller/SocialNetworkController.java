package ru.smmassistant.smmbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/vk/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public void vkRegister(
        @RequestParam("access_token") String accessToken,
        @RequestParam("user_id") Long accountId,
        @PathVariable("user_id") Integer userId) {
        socialNetworkService.vkRegister(accountId, accessToken, userId);
    }
}
