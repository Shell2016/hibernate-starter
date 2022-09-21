package ru.michaelshell.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.michaelshell.converter.BirthdayConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 8751230680435415621L;

    private String firstname;
    private String lastname;

//    @Column(name = "birth_date")
//    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;
}
