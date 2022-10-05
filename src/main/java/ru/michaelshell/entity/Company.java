package ru.michaelshell.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SortNatural;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;




@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(of = "name")
@Builder
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    @MapKey(name = "username")
    private Map<String, User> users = new TreeMap<>();


    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale",
            joinColumns = @JoinColumn(name = "company_id")) //joinColumns не обязательно здесь
//    private List<LocaleInfo> locales = new ArrayList<>();
    @MapKeyColumn(name = "lang")
    @Column(name = "description")
    @NotAudited
    private Map<String, String> locales = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }
}
