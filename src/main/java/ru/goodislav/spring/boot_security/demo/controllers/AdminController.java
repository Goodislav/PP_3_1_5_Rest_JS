package ru.goodislav.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.goodislav.spring.boot_security.demo.models.User;
import ru.goodislav.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String allUsers(ModelMap model, Principal principal) {
        User admin = userService.findByEmail(principal.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("users", userService.getUsers());
        return "admin";
    }

    @PostMapping()
    public String addUser (@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult,
                           @RequestParam(value = "rolesList") String [] roles,
                           @ModelAttribute("pass") String pass) {

        if (bindingResult.hasErrors()){
            return "admin";
        }
        userService.save(user, roles, pass);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String update (@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult,
                          @PathVariable("id") int id,
                          @RequestParam(value = "rolesList") String [] roles,
                          @ModelAttribute("pass") String pass) {

        if (bindingResult.hasErrors()){
            return "admin";
        }
        userService.update(user, id, roles, pass);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete (@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
