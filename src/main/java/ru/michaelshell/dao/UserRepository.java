package ru.michaelshell.dao;

import ru.michaelshell.entity.User;

import javax.persistence.EntityManager;

public class UserRepository extends BaseRepository<Long, User>{
    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
