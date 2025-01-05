package web.crud_spring_hiber.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web.crud_spring_hiber.model.User;
import web.crud_spring_hiber.service.UserService;
import web.crud_spring_hiber.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userService;

    public AdminController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String allUsers(Model model) {
        List<User> users = userService.allUsers();
        model.addAttribute("usersList", users);
        return "admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable int id, Model model) {
        User user = userService.getById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        model.addAttribute("user", user);
        return "admin/editPage";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userService.edit(user);
        } else {
            User existingUser = userService.getById(user.getId());
            if (existingUser != null) {
                existingUser.setUsername(user.getUsername());
                existingUser.setName(user.getName());
                existingUser.setYear(user.getYear());
                existingUser.setGender(user.getGender());
                existingUser.setOnline(user.isOnline());
                userService.edit(existingUser);
            }
        }
        return "redirect:/admin";
    }

    @GetMapping("/add")
    public String addPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin/editPage";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            return "admin/editPage";
        }
        userService.add(user);
        return "redirect:/admin";
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
        return "redirect:/admin";
    }
}