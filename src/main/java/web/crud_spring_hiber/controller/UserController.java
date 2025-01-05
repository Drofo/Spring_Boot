package web.crud_spring_hiber.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.crud_spring_hiber.model.User;
import web.crud_spring_hiber.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userInfo(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable int id, Model model) {
        User user = userService.getById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        model.addAttribute("user", user);
        return "user/profile";
    }
}
