package ru.goodislav.spring.boot_security.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/info")
public class RestUserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(RestUserController.class);

    @Autowired
    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<User> userInfo(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        if (user == null) {
            logger.warn("User with email {} not found", principal.getName());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("User {} accessed their info", principal.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
