package ru.michaelshell.dto;

import ru.michaelshell.entity.PersonalInfo;
import ru.michaelshell.entity.Role;

public record UserCreateDto(
        String username,
        PersonalInfo personalInfo,
        String info,
        Role role,
        Integer companyId) {
}
