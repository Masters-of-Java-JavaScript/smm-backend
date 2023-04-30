package ru.smmassistant.smmbackend.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.smmassistant.smmbackend.dto.PublicationCreateDto;
import ru.smmassistant.smmbackend.dto.PublicationReadDto;
import ru.smmassistant.smmbackend.service.PublicationService;

@RequiredArgsConstructor
@RequestMapping("/publication")
@RestController
public class PublicationController {

    private final PublicationService publicationService;

    @GetMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<PublicationReadDto> findAllByUserId(@PathVariable("user_id") Integer userId) {
        return publicationService.findAllByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicationReadDto publish(@RequestBody PublicationCreateDto publicationCreateDto) {
        return publicationService.publish(publicationCreateDto);
    }
}
