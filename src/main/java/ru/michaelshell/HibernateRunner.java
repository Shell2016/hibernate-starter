package ru.michaelshell;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import ru.michaelshell.converter.BirthdayConverter;
import ru.michaelshell.entity.Birthday;
import ru.michaelshell.entity.Role;
import ru.michaelshell.entity.User;

import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
//        configuration.addAnnotatedClass(User.class);
//        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

//            User user = User.builder()
//                    .username("ivan7@gmail.com")
//                    .firstname("Ivan")
//                    .lastname("Ivanov")
//                    .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
//                    .role(Role.ADMIN)
//                    .info("""
//                            {
//                                "name": "Ivan",
//                                "id": 26
//                            }
//                            """)
//                    .build();
            User user = session.get(User.class, "ivan4@gmail.com");
            user.setLastname("Petrov");
            session.flush();
            System.out.println(session.isDirty());


            session.getTransaction().commit();
        }
    }
}
