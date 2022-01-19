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


    private final UserServiceImpl userServiceImpl;                    //подключена зависимость через конструктор.

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    // домашняя страница.
    @GetMapping("/")
    public String homePage() {    //+
        return "home";
    }


//    //admin page. (если бы небыло MvcConfig)
//    @GetMapping("/admin")
//    public String adminPage() {
//        return "admin";
//    }


    // user page с данными.
    @GetMapping("/user")
    public String userPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }


    // показать всех юзеров из БД.
    @GetMapping("/admin/users")
    public String allUsers(Model model) {
        model.addAttribute("allUsers", userServiceImpl.allUsers());
        return "all_users";
    }


    // получить одного из людей по id из БД через Service и передать на отбражение в представление.
    @GetMapping("/admin/{id}")
    public String findUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServiceImpl.findUserById(id));
        return "findUserById";   // show
    }


    // создать-сохранить юзера. bindingResult - объект в котором будут лежать все ошибки заполнения формы.
    @PostMapping("/admin/users")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors())                  // если есть ошибка заполнения - сразу возврат.
            return "new";

        userServiceImpl.saveUser(user);
        return "redirect:/admin/users";
    }


    // заполнить данные для нового юзера.
    @GetMapping("/admin/new")
    public String newPerson(@ModelAttribute("user") User user) {
        return "new";
    }


    // изменить юзера в БД.
    @GetMapping("/admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {

        model.addAttribute("user", userServiceImpl.findUserById(id));
        return "edit";
    }


    // обновить измененного юзера в БД.
    @PatchMapping("/admin/users/{id}")
    public String update(@Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "edit";

        userServiceImpl.update(user);
        return "redirect:/admin/users";
    }


    // удалить юзера из БД по id.
    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {

        userServiceImpl.deleteUser(id);
        return "redirect:/admin/users";
    }
}
