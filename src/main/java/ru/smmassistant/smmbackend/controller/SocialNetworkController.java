package ru.smmassistant.smmbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/vk")
    @ResponseStatus(HttpStatus.OK)
    public void vkRegister(
        @RequestParam("state") Integer userId,
        @RequestParam String code,
        HttpServletRequest httpServletRequest) {
        String redirectUri = httpServletRequest.getRequestURL().toString();
        socialNetworkService.vkRegister(userId, code, redirectUri);
    }
}
