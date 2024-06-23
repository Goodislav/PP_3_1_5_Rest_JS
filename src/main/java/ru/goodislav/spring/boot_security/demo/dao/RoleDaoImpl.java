package ru.goodislav.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import ru.goodislav.spring.boot_security.demo.models.Role;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role findByRole(String role) {
        Query query = entityManager.createQuery("SELECT r FROM Role r WHERE r.role = : role");
        query.setParameter("role", role);
        return (Role) query.getSingleResult();
    }

    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Override
    public boolean exist(String role) {
        Query query = entityManager.createQuery("SELECT r FROM Role r WHERE r.role = : role");
        query.setParameter("role", role);
        if (((org.hibernate.query.Query)query).list().isEmpty()) {
            return false;
        }
        return true;
    }
}
