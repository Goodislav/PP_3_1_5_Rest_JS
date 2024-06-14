package ru.goodislav.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import ru.goodislav.spring.boot_security.demo.models.User;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findByEmail(String email) {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = : e");
        query.setParameter("e", email);
        return (User) query.getSingleResult();
    }

    @Override
    public List<User> getUsers() {
        return entityManager.createQuery("SELECT u FROM User u").getResultList();
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findUser(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(int id) {
        User user = entityManager.find(User.class, id);
        System.out.println(user);
        entityManager.remove(user);
    }
}
