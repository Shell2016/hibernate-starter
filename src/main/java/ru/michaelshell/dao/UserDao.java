package ru.michaelshell.dao;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import ru.michaelshell.entity.Payment;
import ru.michaelshell.entity.User;

import java.util.List;

import static ru.michaelshell.entity.QCompany.company;
import static ru.michaelshell.entity.QPayment.payment;
import static ru.michaelshell.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
//
//        return session.createQuery("select u from User u", User.class)
//                .list();

//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<User> criteria = cb.createQuery(User.class);
//        Root<User> user = criteria.from(User.class);
//        criteria.select(user);
//
//        return session.createQuery(criteria).list();
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
//        return session.createQuery("select u from User u where u.personalInfo.firstname = :firstName", User.class)
//                .setParameter("firstName", firstName)
//                .list();

//        var cb = session.getCriteriaBuilder();
//        var criteriaQuery = cb.createQuery(User.class);
//        var user = criteriaQuery.from(User.class);
//        criteriaQuery.select(user).where(
//                cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName)
//        );
//
//        return session.createQuery(criteriaQuery).list();
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.personalInfo.firstname.eq(firstName))
                .fetch();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
//        return session.createQuery("select u from User u order by u.personalInfo.birthDate", User.class)
//                .setMaxResults(limit)
//                .list();
        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .orderBy(user.personalInfo.birthDate.asc())
                .limit(limit)
                .fetch();

    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
//        return session.createQuery("select u from User u where u.company.name = :companyName", User.class)
//                .setParameter("companyName", companyName)
//                .list();
        return new JPAQuery<User>(session)
                .select(user)
                .from(company)
                .join(company.users, user)
                .where(company.name.eq(companyName))
                .fetch();

    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
//        return session.createQuery("select p from Payment p " +
//                                   "join p.receiver u " +
//                                   "join u.company c " +
//                                   "where c.name = :companyName " +
//                                   "order by u.personalInfo.firstname, p.amount", Payment.class)
//                .setParameter("companyName", companyName)
//                .list();
        return new JPAQuery<Payment>(session)
                .select(payment)
                .from(payment)
                .join(payment.receiver, user)
                .join(user.company, company)
                .where(company.name.eq(companyName))
                .orderBy(user.personalInfo.firstname.asc(), payment.amount.asc())
                .fetch();

    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
//        return session.createQuery("select avg(p.amount) from Payment p " +
//                                   "join p.receiver u " +
//                                   "where u.personalInfo.firstname = :firstName and u.personalInfo.lastname = :lastName", Double.class)
//                .setParameter("firstName", firstName)
//                .setParameter("lastName", lastName)
//                .uniqueResult();
        return new JPAQuery<Double>(session)
                .select(payment.amount.avg())
                .from(payment)
                .join(payment.receiver, user)
                .where(user.personalInfo.firstname.eq(firstName).and(user.personalInfo.lastname.eq(lastName)))
                .fetchOne();

//        var cb = session.getCriteriaBuilder();
//        var criteria = cb.createQuery(Double.class);
//
//        var payment = criteria.from(Payment.class);
//        var user = payment.join(Payment_.receiver);
//
//        List<Predicate> predicates = new ArrayList<>();
//        if (firstName != null) {
//            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
//        }
//        if (lastName != null) {
//            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));
//        }
//
//        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
//                predicates.toArray(Predicate[]::new)
//        );
//
//        return session.createQuery(criteria).uniqueResult();


    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
//        return session.createQuery("select c.name, avg(p.amount) from Company c " +
//                                   "join c.users u " +
//                                   "join u.payments p " +
//                                   "group by c.name " +
//                                   "order by c.name", Object[].class)
//                .list();
        return new JPAQuery<Tuple>(session)
                .select(company.name, payment.amount.avg())
                .from(company)
                .join(company.users, user)
                .join(user.payments, payment)
                .groupBy(company.name)
                .orderBy(company.name.asc())
                .fetch();

    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session) {
//        return session.createQuery("select u, avg(p.amount) from User u " +
//                                   "join u.payments p " +
//                                   "group by u " +
//                                   "having avg(p.amount) > (select avg(p.amount) from Payment p)" +
//                                   " order by u.personalInfo.firstname", Object[].class)
//                .list();
        return new JPAQuery<Tuple>(session)
                .select(user, payment.amount.avg())
                .from(user)
                .join(user.payments, payment)
                .groupBy(user.id)
                .having(payment.amount.avg().gt(new JPAQuery<Double>(session)
                        .select(payment.amount.avg())
                        .from(payment)))
                .orderBy(user.personalInfo.firstname.asc())
                .fetch();
    }


}
