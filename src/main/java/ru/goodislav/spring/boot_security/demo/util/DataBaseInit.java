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
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DataBaseInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    private void postConstruct() {
        String roleAdmin = "ROLE_ADMIN";
        if(!roleService.exist(roleAdmin)) {
            Role adminRole = new Role(roleAdmin);
            roleService.save(adminRole);
        }

        String roleUser = "ROLE_USER";
        if(!roleService.exist(roleUser)) {
            Role userRole = new Role(roleUser);
            roleService.save(userRole);
        }

        if (!userService.exist("admin@mail.com")) {
            List<Role> adminRolesList = new ArrayList<>();
            adminRolesList.add(roleService.findByRole(roleAdmin));
            adminRolesList.add(roleService.findByRole(roleUser));
            User admin = new User("admin", "admin", 35, "admin@mail.com",
                    "1111",adminRolesList);
            userService.save(admin);
        }

        if (!userService.exist("user@mail.com")) {
            List<Role> userRolesList = new ArrayList<>();
            userRolesList.add(roleService.findByRole(roleUser));
            User user = new User("user", "user", 25, "user@mail.com",
                    "2222", userRolesList);
            userService.save(user);
        }
    }
}
