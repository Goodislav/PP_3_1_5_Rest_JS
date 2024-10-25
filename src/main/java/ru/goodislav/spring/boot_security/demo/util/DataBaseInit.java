package ru.goodislav.spring.boot_security.demo.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.goodislav.spring.boot_security.demo.models.Role;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.services.RoleService;
import ru.goodislav.spring.boot_security.demo.services.UserService;

import java.util.ArrayList;
import java.util.List;

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
        if(!roleService.exist("ROLE_ADMIN")) {
            Role adminRole = new Role("ROLE_ADMIN");
            roleService.save(adminRole);
        }

        if(!roleService.exist("ROLE_USER")) {
            Role userRole = new Role("ROLE_USER");
            roleService.save(userRole);
        }

        if (!userService.exist("admin@mail.com")) {
            List<Role> adminRolesList = new ArrayList<>();
            adminRolesList.add(roleService.findByRole("ROLE_ADMIN"));
            adminRolesList.add(roleService.findByRole("ROLE_USER"));
            User admin = new User("admin", "admin", 35, "admin@mail.com",
                    "1111",adminRolesList);
            userService.save(admin);
        }

        if (!userService.exist("user@mail.com")) {
            List<Role> userRolesList = new ArrayList<>();
            userRolesList.add(roleService.findByRole("ROLE_USER"));
            User user = new User("user", "user", 25, "user@mail.com",
                    "2222", userRolesList);
            userService.save(user);
        }
    }
}
