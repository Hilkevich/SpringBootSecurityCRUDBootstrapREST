package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController  // его методы возвращают json.
public class UserRestController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserRestController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    //get all
    @GetMapping("/admin")
    public List<User> allUsers() {
        return userServiceImpl.allUsers();
    }

    //get one
    @GetMapping("/admin/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userServiceImpl.findUserById(id);
    }

    //create
    @PostMapping("admin")
    public void createUser(@RequestBody User user) {
        userServiceImpl.saveUser(user);
    }

    //update
    @PutMapping("/admin")
    public User updateUser(@RequestBody User user) { return userServiceImpl.update(user); }

    //delete
    @DeleteMapping("/admin/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.deleteUser(id);
    }

    //получаем аутентифицированного юзера
    @GetMapping("/user-authentication")
    public User getAuthenticationUser(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return user;
    }
}
