package ru.smmassistant.smmbackend.mapper;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.smmassistant.smmbackend.dto.PublicationReadDto;
import ru.smmassistant.smmbackend.model.Publication;
import ru.smmassistant.smmbackend.model.PublicationInfo;

@Component
public class PublicationReadMapper implements Mapper<Publication, PublicationReadDto> {

    @Override
    public PublicationReadDto map(Publication publication) {
        return new PublicationReadDto(
            publication.getPublishDate(),
            publication.getMessage(),
            publication.getAttachments(),
            publication.getPublicationInfoList().stream()
                .collect(Collectors.toMap(
                    publicationInfo -> publicationInfo.getSocialNetworkName().name(),
                    PublicationInfo::getLink)));
    }
}
