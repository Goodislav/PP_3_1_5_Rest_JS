package ru.goodislav.spring.boot_security.demo.services;

import ru.goodislav.spring.boot_security.demo.models.Role;


public interface RoleService {
    Role findByRole (String role);
    void save (Role role);
}

