package ru.michaelshell.dto;

import java.util.Map;

public record CompanyReadDto(Integer id,
                             String name,
                             Map<String, String> locales) {
}
