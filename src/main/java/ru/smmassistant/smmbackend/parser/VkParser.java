package ru.smmassistant.smmbackend.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.model.PublicationResponse;

@Component
public class VkParser implements Parser {

    @Override
    public PublicationResponse parse(final String response) {

        ObjectNode node;
        try {
            node = new ObjectMapper().readValue(response, ObjectNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return PublicationResponse.builder()
            .postId(getPostId(node).orElse(null))
            .build();
    }

    private Optional<Long> getPostId(ObjectNode node) {

        Long postId = null;

        if (node.has("response")) {
            JsonNode response = node.get("response");

            if (response.has("post_id")) {
                postId = response.get("post_id").asLong();
            }
        }

        return Optional.ofNullable(postId);
    }
}
