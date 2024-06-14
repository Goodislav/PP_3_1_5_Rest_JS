package ru.goodislav.spring.boot_security.demo.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.goodislav.spring.boot_security.demo.models.Role;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.services.RoleService;
import ru.goodislav.spring.boot_security.demo.services.UserService;

@Component
public class DataBaseInit {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public DataBaseInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void postConstruct() {
        roleService.save(new Role("ROLE_ADMIN"));
        roleService.save(new Role("ROLE_USER"));

        String[] rolesAdmin = {"ROLE_ADMIN", "ROLE_USER"};
        userService.save(new User("admin", "admin", 35, "admin@mail.com"),
                rolesAdmin, "1111");

        String[] rolesUser = {"ROLE_USER"};
        userService.save(new User("user", "user", 25, "user@mail.com"),
                rolesUser, "2222");
    }
}
