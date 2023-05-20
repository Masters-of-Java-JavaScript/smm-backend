package ru.smmassistant.smmbackend.parser;

public interface ResponseParser<T> {

    T parse(final String response);
}
