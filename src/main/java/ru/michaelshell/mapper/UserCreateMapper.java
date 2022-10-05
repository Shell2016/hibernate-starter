package ru.michaelshell.mapper;

import lombok.RequiredArgsConstructor;
import ru.michaelshell.dao.CompanyRepository;
import ru.michaelshell.dto.UserCreateDto;
import ru.michaelshell.entity.User;

@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .username(object.username())
                .role(object.role())
                .company(companyRepository.findById(object.companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }

}
