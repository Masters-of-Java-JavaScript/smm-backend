package ru.smmassistant.smmbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Стартовый контроллер")
@SecurityRequirement(name = "bearerAuth")
public class StartController {

    @Operation(summary = "Стартовый метод, возвращающий \"Hello\"")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Запрос успешно выполнен"),
        @ApiResponse(responseCode = "401", description = "Недостаточно прав"),
        @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
        @ApiResponse(responseCode = "404", description = "Ничего не найдено по запросу"),
        @ApiResponse(responseCode = "500", description = "Сервис недоступен")
    })
    @GetMapping
    public String getStarted() {
        return "Hello";
    }
}
