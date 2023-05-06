package ru.smmassistant.smmbackend.parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.model.PublicationResponse;

@Component
public class VkParser implements Parser {

    @Override
    public PublicationResponse parse(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
        JsonObject responseElement = jsonObject.getAsJsonObject("response");
        Integer postId = responseElement.get("post_id").getAsInt();

        return PublicationResponse.builder()
            .postId(postId)
            .build();
    }
}
