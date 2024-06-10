package ru.goodislav.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.goodislav.spring.boot_security.demo.models.Role;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getAllUsers() {
        logger.info("Getting all users");
        List<User> users = userService.findAll();
        logger.info("Number of users got: {}", users.size());
        ModelAndView modelAndView = new ModelAndView("admin");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @GetMapping("/addUser")
    public ModelAndView addUserForm() {
        logger.info("Displaying add user form");
        User user = new User();
        ModelAndView mav = new ModelAndView("admin/addUser");
        mav.addObject("user", user);
        List<Role> roles = userService.findAllRoles();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute @Valid User user,
                          @RequestParam(value = "roles", required = false) List<Long> roleIds,
                          BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Error adding user: " + result.getAllErrors());
            return "admin/addUser";
        }
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                roles.add(userService.findRoleById(roleId));
            }
        }
        user.setRoles(roles);
        userService.save(user);
        logger.info("User added successfully: {}", user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public ModelAndView editUser(@RequestParam Long id) {
        logger.info("Editing user with id: {}", id);
        User user = userService.findById(id);
        if (user != null) {
            logger.info("User found: {}", user);
        } else {
            logger.info("User with id: {} not found", id);
        }
        ModelAndView mav = new ModelAndView("admin/editUser");
        mav.addObject("user", user);
        List<Role> roles = userService.findAllRoles();
        mav.addObject("allRoles", roles);
        return mav;
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        logger.info("Displaying edit form for user ID: " + id);
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", userService.findAllRoles());
        return "editUser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("lastname") String lastname,
                             @RequestParam("age") int age,
                             @RequestParam("email") String email,
                             @RequestParam("password") String password,
                             @RequestParam(value = "roles", required = false) List<Long> roleIds,
                             @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("Binding result has errors: {}", bindingResult);
            return "admin/editUser";
        }
        logger.info("Updating user with id: {}", id);
        User updateUser = userService.findById(id);
        if (updateUser != null) {
            updateUser.setName(name);
            updateUser.setLastname(lastname);
            updateUser.setAge(age);
            updateUser.setEmail(email);

            if (!password.isEmpty()) {
                updateUser.setPassword(password);
            }

            Set<Role> roles = new HashSet<>();
            if (roleIds != null) {
                for (Long roleId : roleIds) {
                    roles.add(userService.findRoleById(roleId));
                }
            }

            if (roles.isEmpty()) {
                Role userRole = userService.findRoleByRoleName("ROLE_USER");
                roles.add(userRole);
            }

            updateUser.setRoles(roles);
            userService.update(updateUser);
            logger.info("User updated successfully: {}", updateUser);
        } else {
            logger.info("User with id: {} not found", id);
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        logger.info("Deleting user with id \"{}\"", id);
        userService.deleteById(id);
        logger.info("User deleted successfully, ID: " + id);
        return "redirect:/admin";
    }
}
