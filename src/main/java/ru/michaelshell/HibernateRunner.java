package ru.michaelshell;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import ru.michaelshell.entity.User;
import ru.michaelshell.util.HibernateUtil;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Slf4j
public class HibernateRunner {

//    public static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    @Transactional
    public static void main(String[] args) throws SQLException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

//            TestDataImporter.importData(sessionFactory);

//            session.doWork(connection -> System.out.println(connection.getTransactionIsolation()));
            User user = null;
            try (var session = sessionFactory.openSession()) {
                session.beginTransaction();

                user = session.get(User.class, 1L);
                user.getCompany().getName();
                user.getUserChats().size();
                var user1 = session.get(User.class, 1L);
                user1.getCompany().getName();
                user1.getUserChats().size();

                session.getTransaction().commit();
            }

            try (var session2 = sessionFactory.openSession()) {
                session2.beginTransaction();

                var user2 = session2.get(User.class, 1L);
                user2.getCompany().getName();
                user2.getUserChats().size();

                session2.getTransaction().commit();
            }

        }


    }
}
