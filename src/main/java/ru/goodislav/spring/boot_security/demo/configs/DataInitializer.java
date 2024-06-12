package ru.goodislav.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.goodislav.spring.boot_security.demo.models.Role;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.repositories.RoleRepository;
import ru.goodislav.spring.boot_security.demo.repositories.UserRepository;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@mail.com") == null) {
            Role adminRole = roleRepository.findByRole("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }

            User admin = new User();
            admin.setName("admin");
            admin.setLastname("admin");
            admin.setAge(35);
            admin.setEmail("admin@mail.com");
            admin.setPassword(passwordEncoder.encode("1111"));
            admin.setRoles(List.of(adminRole));
            userRepository.save(admin);
        }
    }
}

