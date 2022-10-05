package ru.michaelshell.dto;

import ru.michaelshell.entity.PersonalInfo;
import ru.michaelshell.entity.Role;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          String info,
                          Role role,
                          CompanyReadDto company) {
}
