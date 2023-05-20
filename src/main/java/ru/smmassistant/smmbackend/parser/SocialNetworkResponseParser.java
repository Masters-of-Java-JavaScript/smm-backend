package ru.smmassistant.smmbackend.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.model.SocialNetwork;
import ru.smmassistant.smmbackend.model.SocialNetworkName;

@Component
public class SocialNetworkResponseParser {

    public void parseTo(SocialNetwork socialNetwork, final String response) {
        ObjectNode node;
        try {
            node = new ObjectMapper().readValue(response, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка парсинга. Ответ от внешнего сервиса не валидный", e);
        }

        if (socialNetwork.getName().equals(SocialNetworkName.VK)) {
            vkParse(socialNetwork, node);
        } else {
            throw new RuntimeException("Ошибка парсинга. Социальной сети не существует");
        }
    }

    private void vkParse(SocialNetwork socialNetwork, ObjectNode node) {
        if (!(node.has("access_token") || node.has("user_id"))) {
            if (node.has("error") && node.has("error_description")) {
                throw new RuntimeException("Не удалось получить access_token: error is %s, error_description is %s"
                    .formatted(node.get("error").asText(), node.get("error_description").asText()));
            }
        }

        socialNetwork.setAccessToken(node.get("access_token").asText());
        socialNetwork.setAccountId(node.get("user_id").asLong());
    }
}
