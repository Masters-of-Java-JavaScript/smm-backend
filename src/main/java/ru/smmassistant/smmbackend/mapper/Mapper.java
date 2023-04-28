package ru.smmassistant.smmbackend.mapper;

public interface Mapper<F, T> {

    T map(F object);
}
