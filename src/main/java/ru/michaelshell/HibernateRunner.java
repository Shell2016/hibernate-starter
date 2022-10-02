package ru.michaelshell;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import ru.michaelshell.entity.User;
import ru.michaelshell.util.HibernateUtil;

import java.sql.SQLException;

@Slf4j
public class HibernateRunner {

//    public static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) throws SQLException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var user = session.get(User.class, 1L);
            System.out.println(user.getPayments().size());
            System.out.println(user.getCompany().getName());

            session.getTransaction().commit();
        }
    }
}
