package ru.michaelshell.mapper;

import ru.michaelshell.dto.CompanyReadDto;
import ru.michaelshell.entity.Company;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {
    @Override
    public CompanyReadDto mapFrom(Company object) {

        return new CompanyReadDto(object.getId(),
                object.getName(),
                object.getLocales());
    }
}
