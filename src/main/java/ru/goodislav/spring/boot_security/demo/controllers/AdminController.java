package ru.goodislav.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.goodislav.spring.boot_security.demo.models.Role;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.services.UserService;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String allUsers(ModelMap model, Principal principal) {
        logger.info("Getting all users");
        List<User> users = userService.getUsers();
        logger.info("Number of users got: {}", users.size());
        User admin = userService.findByEmail(principal.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("users", userService.getUsers());
        logger.info("Got all users: {}", userService.getUsers());
        return "admin";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult,
                          @RequestParam(value = "rolesList", required = false) String[] roles,
                          @RequestParam(value = "pass") String pass) {
        logger.info("Adding new user: {}", user);
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred while adding user: {}", bindingResult.getAllErrors());
            return "admin";
        }
        if (roles == null) {
            roles = new String[]{};
        }
        userService.save(user, roles, pass);
        logger.info("User added successfully: {}", user);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") int id,
                         @RequestParam(value = "rolesList", required = false) String[] roles,
                         @RequestParam(value = "pass") String pass) {
        logger.info("Updating user with id {}: {}", id, user);
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred while updating user: {}", bindingResult.getAllErrors());
            return "admin";
        }
        if (roles == null || roles.length == 0) {
            logger.info("Roles not provided, retrieving existing roles for user with id: {}", id);
            User existingUser = userService.findUser(id);
            roles = existingUser.getRoles().stream().map(Role::getRole).toArray(String[]::new);
        }
        userService.update(user, id, roles, pass);
        logger.info("User updated successfully: {}", user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        logger.info("Deleting user with id: {}", id);
        userService.delete(id);
        logger.info("User deleted successfully, ID: {}", id);
        return "redirect:/admin";
    }
}
