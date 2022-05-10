package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;

@Controller
public class AdminController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/")
    public String pageHome() {
        return "homePage";
    }

    @GetMapping("/user")
    public String pageUser(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "userPage";
    }

    @GetMapping("/admin")
    public String allUsers(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("allUsers", userServiceImpl.allUsers());
        return "adminPage";
    }

    @PostMapping("/admin/create")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userServiceImpl.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/update")
    public String updateUser(@Valid User user, BindingResult bindingResult) {
        userServiceImpl.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }
}
