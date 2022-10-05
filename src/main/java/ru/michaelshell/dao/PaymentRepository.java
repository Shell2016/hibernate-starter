package ru.michaelshell.dao;

import ru.michaelshell.entity.Payment;

import javax.persistence.EntityManager;

public class PaymentRepository extends BaseRepository<Long, Payment>{
    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }
}
