package ru.smmassistant.smmbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StartController {

    @GetMapping
    public String getStarted() {
        return "Hello";
    }
}
