package web.crud_spring_hiber.service;

import org.springframework.stereotype.Service;
import web.crud_spring_hiber.model.User;

import java.util.List;

@Service
public interface UserService {
    List<User> allUsers();
    void add(User user);
    void delete(User user);
    void edit(User user);
    User getById(int id);
}