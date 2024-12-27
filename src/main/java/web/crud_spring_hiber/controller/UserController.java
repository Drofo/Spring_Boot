package web.crud_spring_hiber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.crud_spring_hiber.model.User;
import web.crud_spring_hiber.service.UserService;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String allUsers(Model model) {
        List<User> users = userService.allUsers();
        model.addAttribute("usersList", users);
        return "users";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable int id, Model model) {
        User user = userService.getById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        model.addAttribute("user", user);
        return "editPage";
    }

    @PostMapping("/edit")
    public String editUser(User user) {
        userService.edit(user);
        return "redirect:/";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "editPage";
    }

    @PostMapping("/add")
    public String addUser(User user) {
        userService.add(user);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        User user = userService.getById(id);
        if (user != null) {
            userService.delete(user);
            redirectAttributes.addFlashAttribute("success", "User successfully deleted!");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found!");
        }
        return "redirect:/";
    }
}