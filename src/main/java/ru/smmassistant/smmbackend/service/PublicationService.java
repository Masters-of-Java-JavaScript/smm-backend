package ru.smmassistant.smmbackend.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.dto.PublicationReadDto;
import ru.smmassistant.smmbackend.mapper.PublicationCreateMapper;
import ru.smmassistant.smmbackend.mapper.PublicationReadMapper;
import ru.smmassistant.smmbackend.repository.PublicationRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final PublicationReadMapper publicationReadMapper;
    private final PublicationCreateMapper publicationCreateMapper;

    public List<PublicationReadDto> findAllByUserId(Integer userId) {
        return publicationRepository.findAllByUserId(userId)
            .stream()
            .map(publicationReadMapper::map)
            .toList();
    }

    @Transactional
    public PublicationReadDto create(PublicationCreateDto publicationDto) {
        return Optional.of(publicationDto)
            .map(publicationCreateMapper::map)
            .map(publicationRepository::save)
            .map(publicationReadMapper::map)
            .orElseThrow();
    }
}
