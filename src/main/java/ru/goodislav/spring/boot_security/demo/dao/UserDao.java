package ru.goodislav.spring.boot_security.demo.dao;

import ru.goodislav.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserDao {
    User findByEmail(String email);
    List<User> getUsers();
    void save(User user);
    void update(User user);
    void delete(int id);
    boolean exist(String email);
}
