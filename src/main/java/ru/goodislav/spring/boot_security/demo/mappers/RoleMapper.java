package ru.goodislav.spring.boot_security.demo.mappers;

import org.springframework.stereotype.Component;
import ru.goodislav.spring.boot_security.demo.dto.RoleDTO;
import ru.goodislav.spring.boot_security.demo.models.Role;

@Component
public class RoleMapper {

    public RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getId(), role.getRole());
    }

    public Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setRole(roleDTO.getRole());
        return role;
    }
}
