package ru.smmassistant.smmbackend.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.model.Publication;

@Component
public class PublicationCreateMapper implements Mapper<PublicationCreateDto, Publication> {

    @Override
    public Publication map(PublicationCreateDto publicationCreateDto) {
        Publication publication = new Publication();
        copy(publicationCreateDto, publication);
        return  publication;
    }

    private void copy(PublicationCreateDto publicationCreateDto, Publication publication) {
        publication.setUserId(publicationCreateDto.userId());
        publication.setMessage(publicationCreateDto.message());
        publication.setAttachments(publicationCreateDto.attachments());
        publication.setPublishDate(Optional.ofNullable(publicationCreateDto.publishDate())
            .map(publishDate -> LocalDateTime.ofEpochSecond(
                publishDate,
                0,
                ZoneOffset.UTC))
            .orElse(LocalDateTime.now()));
    }
}
