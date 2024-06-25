package ru.goodislav.spring.boot_security.demo.services;

import ru.goodislav.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    void save(User user);
    User findByEmail(String email);
    void update(User user);
    void delete(int id);
    boolean exist(String email);
}

