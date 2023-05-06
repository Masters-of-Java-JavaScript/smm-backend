package ru.smmassistant.smmbackend.parser;

import ru.smmassistant.smmbackend.model.PublicationResponse;

public interface Parser {

    PublicationResponse parse(String response);
}
