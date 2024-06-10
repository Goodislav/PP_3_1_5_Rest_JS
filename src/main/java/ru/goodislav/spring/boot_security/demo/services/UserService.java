package ru.goodislav.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.goodislav.spring.boot_security.demo.models.Role;
import ru.goodislav.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;

@Service
public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    List<User> findAll();
    List<Role> findAllRoles();
    Role findRoleById(Long id);
    User findById(Long id);
    void save(User user);
    void update(User user);
    void deleteById(Long id);
    User findByEmail(String email);
    void setRoles(User user, Set<Role> roles);

    Set<Role> findRolesByIds(List<Long> roleIds);

    Role findRoleByRoleName(String roleUser);

}
