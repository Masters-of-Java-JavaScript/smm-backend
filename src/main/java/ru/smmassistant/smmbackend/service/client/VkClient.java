package ru.smmassistant.smmbackend.service.client;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface VkClient {

    @GetExchange("https://oauth.vk.com/access_token")
    String getAccessToken(@RequestParam Map<String, Object> requestParams);

    @PostExchange("${api.vk.url}/wall.post")
    String publish(@RequestParam Map<String, Object> requestParams);
}
