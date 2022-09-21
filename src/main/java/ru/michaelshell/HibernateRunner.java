package ru.michaelshell;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.michaelshell.entity.Birthday;
import ru.michaelshell.entity.PersonalInfo;
import ru.michaelshell.entity.User;
import ru.michaelshell.util.HibernateUtil;

import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

//    public static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) throws SQLException {

        User user = User.builder()
                .personalInfo(PersonalInfo.builder()
                        .firstname("Michael")
                        .lastname("Shell1")
                        .birthDate(new Birthday(LocalDate.of(1982, 8, 3)))
                        .build())
                .username("Myst2019")
                .build();
        log.info("user entity is in transient state: {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try (session) {
                Transaction transaction = session.beginTransaction();
                log.trace("Transaction created: {}", transaction);

                session.saveOrUpdate(user);
                log.trace("User is in persistent state: {}, session: {}", user, session);

                session.getTransaction().commit();
            }
            log.warn("User is in detached state: {}, session {} is closed", user, session);
        } catch (Exception e) {
            log.error("Exception occurred", e);
        }
    }
}
