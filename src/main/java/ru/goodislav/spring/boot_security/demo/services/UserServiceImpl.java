package ru.goodislav.spring.boot_security.demo.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.goodislav.spring.boot_security.demo.models.Role;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.repositories.RoleRepository;
import ru.goodislav.spring.boot_security.demo.repositories.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Override
    @Transactional
    public void save(User user) {
        if (user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByRole("ROLE_USER");
            if (userRole != null) {
                user.getRoles().add(userRole);
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        existingUser.setName(user.getName());
        existingUser.setLastname(user.getLastname());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());

        if (user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByRole("ROLE_USER");
            if (userRole != null) {
                user.getRoles().add(userRole);
            }
        }

        existingUser.setRoles(user.getRoles());
        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void setRoles(User user, Set<Role> roles) {
        user.setRoles(roles);
    }

    @Override
    public Set<Role> findRolesByIds(List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        for (Long id : roleIds) {
            roles.add(roleRepository.findById(id)
                 .orElseThrow(() -> new IllegalArgumentException("Invalid role ID: " + id)));
        }
        return roles;
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByRole(roleName);
    }

    @PostConstruct
    public void init() {
        if (roleRepository.findByRole("ROLE_ADMIN") == null) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        if (roleRepository.findByRole("ROLE_USER") == null) {
            roleRepository.save(new Role("ROLE_USER"));
        }
    }
}
