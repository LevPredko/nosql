package org.example.nosql.controller;

import lombok.RequiredArgsConstructor;
import org.example.nosql.model.User;
import org.example.nosql.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "index";
    }

    @GetMapping("/add")
    public String addUserForm() {
        return "user-form";
    }

    @PostMapping("/user/save")
    public String saveUser(@RequestParam String name,
                           @RequestParam int age,
                           @RequestParam String email,  // Додаємо параметр для email
                           @RequestParam String interests) {
        Optional<User> existingUser = userService.getUserByEmail(email);
        if (existingUser.isPresent()) {
            return "redirect:/add?error=emailExists";
        }

        List<String> interestsList = Arrays.asList(interests.split(","));
        User user = new User(null, name, age, email, interestsList, new ArrayList<>());
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable String id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            List<String> interestsList = user.get().getInterests();
            model.addAttribute("user", user.get());
            model.addAttribute("interestsList", interestsList);
            return "user-details";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/filter")
    public String filterUsersByInterest(@RequestParam(required = false) String interest, Model model) {
        if (interest == null || interest.trim().length() < 2) {
            model.addAttribute("error", "Інтерес має містити щонайменше 2 символи.");
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("filterInterest", "");
        } else {
            List<User> filteredUsers = userService.findByInterest(interest);
            model.addAttribute("users", filteredUsers);
            model.addAttribute("filterInterest", interest);
        }
        return "index";
    }


    @GetMapping("/top-interests")
    public String topInterests(Model model) {
        Map<String, Long> topInterests = userService.getTopInterests(5);
        model.addAttribute("topInterests", topInterests);
        return "top-interests";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "index";
    }
}
