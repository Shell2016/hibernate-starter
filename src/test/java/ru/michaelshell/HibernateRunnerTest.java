package ru.michaelshell;

import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import ru.michaelshell.entity.Company;
import ru.michaelshell.entity.User;
import ru.michaelshell.util.HibernateUtil;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void orphanRemoval() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();

        Company company = session.get(Company.class, 5);
        company.getUsers().removeIf(user -> user.getId().equals(4L));

        session.getTransaction().commit();

    }

    @Test
    void deleteCompany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();

        Company company = session.get(Company.class, 6);
        session.delete(company);

        session.getTransaction().commit();

    }

    @Test
    void newCompanyAndUser() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();
        Company company = Company.builder()
                .name("Yandex")
                .build();
        User user = User.builder()
                .username("Shell123")
                .build();
        company.addUser(user);
        session.save(company);

        session.getTransaction().commit();

    }


    @Test
    void oneToMany() {
        @Cleanup SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();

        Company company = session.get(Company.class, 5);
        System.out.println(" ");


        session.getTransaction().commit();
    }


    @Test
    void checkReflectionApi() throws IllegalAccessException {

        User user = User.builder()
                .build();

        String sql = """
                insert into
                %s
                (%s)
                values
                (%s)
                """;

        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .sorted()
                .collect(joining(", "));
        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            System.out.println(declaredField.get(user));
        }
    }

}