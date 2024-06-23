package ru.goodislav.spring.boot_security.demo.dao;

import ru.goodislav.spring.boot_security.demo.models.Role;

public interface RoleDao {
    Role findByRole (String role);
    void save (Role role);
    boolean exist(String role);

}
