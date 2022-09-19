package ru.michaelshell.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.michaelshell.converter.BirthdayConverter;


import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@TypeDef(name = "testType", typeClass = JsonBinaryType.class)
public class User {

    @Id
    private String username;
    private String firstname;
    private String lastname;

    @Column(name = "birth_date")
    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;

    @Type(type = "testType")
    private String info;

    @Enumerated(EnumType.STRING)
    private Role role;
}
