package web.crud_spring_hiber.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.crud_spring_hiber.dto.RoleDTO;
import web.crud_spring_hiber.dto.UserDTO;
import web.crud_spring_hiber.model.User;
import web.crud_spring_hiber.model.Role;
import web.crud_spring_hiber.service.UserService;
import web.crud_spring_hiber.service.RoleServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final UserService userService;
    private final RoleServiceImpl roleService;

    public AdminRestController(UserService userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setName(user.getName());
        userDTO.setYear(user.getYear());
        userDTO.setGender(user.getGender());
        userDTO.setOnline(user.isOnline());
        userDTO.setRoles(user.getRoles().stream().map(this::convertToDTO).collect(Collectors.toSet()));
        return userDTO;
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.allUsers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(user));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getAllRoles().stream()
                        .filter(r -> r.getId().equals(role.getId()))
                        .findFirst().orElse(null))
                .collect(Collectors.toSet()));
        userService.add(user);
        return ResponseEntity.ok(convertToDTO(user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        User existingUser = userService.getById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setName(updatedUser.getName());
        existingUser.setYear(updatedUser.getYear());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setOnline(updatedUser.isOnline());
        existingUser.setRoles(updatedUser.getRoles().stream()
                .map(role -> roleService.getAllRoles().stream()
                        .filter(r -> r.getId().equals(role.getId()))
                        .findFirst().orElse(null))
                .collect(Collectors.toSet()));

        userService.edit(existingUser);
        return ResponseEntity.ok(convertToDTO(existingUser));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roleDTOs = roleService.getAllRoles().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roleDTOs);
    }
}