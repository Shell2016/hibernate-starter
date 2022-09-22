package ru.michaelshell.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo{

    private String firstname;
    private String lastname;

//    @Column(name = "birth_date")
//    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;
}
