package ru.michaelshell.entity;

import lombok.*;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(of = "name")
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "company_id")
//    @OrderBy("personalInfo.lastname ASC")
    @SortNatural
    private Set<User> users = new TreeSet<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale",
            joinColumns = @JoinColumn(name = "company_id")) //joinColumns не обязательно здесь
    private List<LocaleInfo> locales = new ArrayList<>();
//    @Column(name = "description")
//    private List<String> description = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }
}
