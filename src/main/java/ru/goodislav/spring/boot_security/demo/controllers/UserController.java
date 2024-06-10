package ru.goodislav.spring.boot_security.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String getUser(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        logger.info("Getting user");
        User user = userService.findByEmail(currentUser.getUsername());
        logger.info("Showing user with email {}", user.getEmail());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/user")
    public String userHome(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        logger.info("Displaying user home page for user: {}", currentUser.getUsername());
        User user = userService.findByEmail(currentUser.getUsername());
        model.addAttribute("user", user);
        return "user";
    }
}
