package ru.michaelshell;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.michaelshell.entity.Birthday;
import ru.michaelshell.entity.Company;
import ru.michaelshell.entity.PersonalInfo;
import ru.michaelshell.entity.User;
import ru.michaelshell.util.HibernateUtil;

import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

//    public static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) throws SQLException {
        Company company = Company.builder()
                .name("Google")
                .build();
        User user = User.builder()
                .personalInfo(PersonalInfo.builder()
                        .firstname("Michael")
                        .lastname("Shell1")
                        .birthDate(new Birthday(LocalDate.of(1982, 8, 3)))
                        .build())
                .username("Myst2020")
                .company(company)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction = session.beginTransaction();
//
//                session.save(company);
//                session.save(user);
                session.get(User.class, 3L);


                session.getTransaction().commit();
            }

        }
    }
}
