package ru.goodislav.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.goodislav.spring.boot_security.demo.dto.UserDTO;
import ru.goodislav.spring.boot_security.demo.mappers.UserMapper;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.services.UserService;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class RestAdminController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(RestAdminController.class);
    private final UserMapper userMapper;

    @Autowired
    public RestAdminController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> allUsersRest() {
        List<User> users = userService.getUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(userMapper::toDTO)
                .toList();
        logger.info("Got all users: {}", userDTOs);
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> navBar(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        UserDTO userDTO = userMapper.toDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO) {
        logger.info("Adding new user: {}", userDTO);
        User user = userMapper.toEntity(userDTO);
        userService.save(user);
        UserDTO createdUserDTO = userMapper.toDTO(user);
        logger.info("User added successfully: {}", createdUserDTO);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserDTO userDTO) {
        logger.info("Updating user: {}", userDTO);
        User user = userMapper.toEntity(userDTO);
        userService.update(user);
        UserDTO updatedUserDTO = userMapper.toDTO(user);
        logger.info("User updated successfully: {}", updatedUserDTO);
        return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable("id") int id) {
        logger.info("Deleting user with id: {}", id);
        userService.delete(id);
        logger.info("User deleted successfully, ID: {}", id);
        return new ResponseEntity<>(id, HttpStatus.NO_CONTENT);
    }
}
