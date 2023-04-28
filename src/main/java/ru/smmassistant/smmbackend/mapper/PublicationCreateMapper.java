package ru.smmassistant.smmbackend.mapper;

import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.model.Publication;

@Component
public class PublicationCreateMapper implements Mapper<PublicationCreateDto, Publication> {

    @Override
    public Publication map(PublicationCreateDto publicationDto) {
        Publication publication = new Publication();
        copy(publicationDto, publication);
        return  publication;
    }

    private void copy(PublicationCreateDto publicationDto, Publication publication) {
        publication.setText(publicationDto.getText());
        publication.setUserId(publicationDto.getUserId());
        publication.setCreateDttm(publicationDto.getCreatedDttm());
        publication.setOwnerId(publicationDto.getOwnerId());
        publication.setLink(publicationDto.getLink());
    }
}
