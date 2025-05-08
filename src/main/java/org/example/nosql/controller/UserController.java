package org.example.nosql.controller;

import lombok.RequiredArgsConstructor;
import org.example.nosql.model.User;
import org.example.nosql.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Головна сторінка
    @GetMapping("/")
    public String index(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "index";
    }

    // Форма додавання користувача
    @GetMapping("/add")
    public String addUserForm() {
        return "user-form";
    }

    // Збереження користувача
    @PostMapping("/user/save")
    public String saveUser(@RequestParam String name,
                           @RequestParam int age,
                           @RequestParam String email,  // Додаємо параметр для email
                           @RequestParam String interests) {
        // Перевірка чи користувач з такою поштою вже існує
        Optional<User> existingUser = userService.getUserByEmail(email);
        if (existingUser.isPresent()) {
            // Якщо такий користувач є, перенаправляємо назад з повідомленням про помилку
            return "redirect:/add?error=emailExists";
        }

        List<String> interestsList = Arrays.asList(interests.split(","));
        User user = new User(null, name, age, email, interestsList, new ArrayList<>());
        userService.saveUser(user);
        return "redirect:/";
    }

    // Видалення користувача
    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    // Перегляд конкретного користувача
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable String id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            // Перетворюємо масив інтересів в список
            List<String> interestsList = user.get().getInterests();
            model.addAttribute("user", user.get());
            model.addAttribute("interestsList", interestsList);  // Передаємо список інтересів в шаблон
            return "user-details";  // Можна створити шаблон для деталей користувача
        } else {
            return "redirect:/";
        }
    }
}
