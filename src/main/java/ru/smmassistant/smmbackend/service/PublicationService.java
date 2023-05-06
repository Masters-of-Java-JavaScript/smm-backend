package ru.smmassistant.smmbackend.service;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.dto.PublicationReadDto;
import ru.smmassistant.smmbackend.mapper.PublicationCreateMapper;
import ru.smmassistant.smmbackend.mapper.PublicationReadMapper;
import ru.smmassistant.smmbackend.model.Publication;
import ru.smmassistant.smmbackend.repository.PublicationRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
@Service
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final PublicationReadMapper publicationReadMapper;
    private final PublicationCreateMapper publicationCreateMapper;
    private final VkPublicationService vkPublicationService;

    public List<PublicationReadDto> findAllByUserId(Integer userId) {
        return publicationRepository.findAllByUserId(userId)
            .stream()
            .map(publicationReadMapper::map)
            .toList();
    }

    @Transactional
    public PublicationReadDto publish(@Valid PublicationCreateDto publicationCreateDto) {
        vkPublicationService.publish(publicationCreateDto);

        Publication publication = publicationCreateMapper.map(publicationCreateDto);
        publicationRepository.save(publication);

        return publicationReadMapper.map(publication);
    }
}
