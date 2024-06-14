package ru.goodislav.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.goodislav.spring.boot_security.demo.dao.UserDao;
import ru.goodislav.spring.boot_security.demo.models.Role;
import ru.goodislav.spring.boot_security.demo.models.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException(String.format("Email '%' not found", user.getEmail()));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), getAuthority(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthority(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }
}
