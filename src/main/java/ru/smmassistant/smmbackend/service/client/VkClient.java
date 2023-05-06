package ru.smmassistant.smmbackend.service.client;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
    url = "${api.vk.url}",
    accept = "application/json"
)
public interface VkClient {

    @PostExchange("/wall.post")
    String publish(@RequestParam Map<String, Object> requestParam);
}
