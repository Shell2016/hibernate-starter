package ru.michaelshell;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.michaelshell.dao.CompanyRepository;
import ru.michaelshell.dao.UserRepository;
import ru.michaelshell.dto.UserCreateDto;
import ru.michaelshell.entity.PersonalInfo;
import ru.michaelshell.entity.Role;
import ru.michaelshell.interceptor.TransactionInterceptor;
import ru.michaelshell.mapper.CompanyReadMapper;
import ru.michaelshell.mapper.UserCreateMapper;
import ru.michaelshell.mapper.UserReadMapper;
import ru.michaelshell.service.UserService;
import ru.michaelshell.util.HibernateUtil;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

//    public static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    @Transactional
    public static void main(String[] args) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

//            TestDataImporter.importData(sessionFactory);

//            session.doWork(connection -> System.out.println(connection.getTransactionIsolation()));
//            var session = sessionFactory.getCurrentSession();

            var session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
            session.beginTransaction();

            var userRepository = new UserRepository(session);
            var companyRepository = new CompanyRepository(session);
            var userCreateMapper = new UserCreateMapper(companyRepository);
            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
//            var userService = new UserService(userRepository, userReadMapper, userCreateMapper);

            var transactionInterceptor = new TransactionInterceptor(sessionFactory);

            var userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            userService.findById(1L).ifPresent(System.out::println);
            var userCreateDto = new UserCreateDto("Liza3@gmail.com",
                    PersonalInfo.builder()
                            .firstname("Liza3")
                            .lastname("Romanova")
                            .birthDate(LocalDate.now())
                            .build(),
                    null,
                    Role.USER,
                    2);
            userService.create(userCreateDto);


            session.getTransaction().commit();


        }


    }
}
