package ru.michaelshell;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import ru.michaelshell.entity.Payment;
import ru.michaelshell.util.HibernateUtil;
import ru.michaelshell.util.TestDataImporter;

import javax.transaction.Transactional;
import java.sql.SQLException;

@Slf4j
public class HibernateRunner {

//    public static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    @Transactional
    public static void main(String[] args) throws SQLException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {

            TestDataImporter.importData(sessionFactory);

//            session.doWork(connection -> System.out.println(connection.getTransactionIsolation()));
            session.beginTransaction();


            var payment = session.get(Payment.class, 1L);
            payment.setAmount(10000);




            session.getTransaction().commit();

        }
    }
}
