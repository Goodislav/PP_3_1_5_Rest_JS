package ru.goodislav.spring.boot_security.demo.services;

import ru.goodislav.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    void save(User user, String[] roles, String pass);
    User findUser(int id);
    User findByEmail(String email);
    void update(User user, int id, String[] roles, String pass);
    void delete(int id);
    boolean exist(String email);
}

