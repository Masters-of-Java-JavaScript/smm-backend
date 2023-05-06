package ru.smmassistant.smmbackend.mapper;

import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.dto.PublicationReadDto;
import ru.smmassistant.smmbackend.model.Publication;

@Component
public class PublicationReadMapper implements Mapper<Publication, PublicationReadDto> {

    @Override
    public PublicationReadDto map(Publication publication) {
        return new PublicationReadDto(
            publication.getPublishDate(),
            publication.getMessage(),
            publication.getAttachments());
    }
}
