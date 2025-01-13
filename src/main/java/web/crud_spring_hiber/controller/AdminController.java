package web.crud_spring_hiber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.crud_spring_hiber.model.User;
import web.crud_spring_hiber.model.Role;
import web.crud_spring_hiber.service.RoleServiceImpl;
import web.crud_spring_hiber.service.UserServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(PasswordEncoder passwordEncoder, UserServiceImpl userService, RoleServiceImpl roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPanel(Model model) {
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/admin_panel";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") List<Long> roleIds) {
        List<Role> roles = roleService.getRolesByIds(roleIds);
        user.setRoles(roles.stream().collect(Collectors.toSet()));
        userService.add(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        User user = userService.getById(id);
        if (user != null) {
            userService.delete(user);
        }
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model) {
        User user = userService.getById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/admin_panel";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("roles") List<Long> roleIds) {
        User existingUser = userService.getById(user.getId());
        if (existingUser == null) {
            return "redirect:/admin";
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        List<Role> roles = roleService.getRolesByIds(roleIds);
        user.setRoles(roles.stream().collect(Collectors.toSet()));

        userService.edit(user);
        return "redirect:/admin";
    }

}
